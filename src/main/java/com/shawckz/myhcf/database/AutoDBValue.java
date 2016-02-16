/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.database;

import com.shawckz.myhcf.configuration.AbstractSerializer;
import com.shawckz.myhcf.database.annotations.DBColumn;
import com.shawckz.myhcf.database.annotations.DatabaseSerializer;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class AutoDBValue {

    private String identifier = null;
    private Object identifierValue = null;
    private final Map<String, Object> values = new HashMap<>();

    public AutoDBValue(AutoDBable a) {
        updateValues(a);
    }

    public AutoDBValue(Document document) {
        for(String s : document.keySet()) {
            values.put(s, document.get(s));
        }
    }

    public void updateValues(AutoDBable a) {
        for (Field field : a.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            DBColumn column = field.getAnnotation(DBColumn.class);
            if (column != null) {
                String columnName = column.name();
                if (columnName.equals("")) {
                    columnName = field.getName();
                }
                if (column.identifier()) {
                    identifier = columnName;
                    identifierValue = getValue(field, a);
                }
                else {
                    values.put(columnName, getValue(field, a));
                }
            }
        }
    }

    public boolean isValid() {
        return identifier != null;
    }

    public String getValue(Field field, AutoDBable a) {
        try {
            Object o = field.get(a);
            if (o != null) {
                String ret = o.toString();
                if (ret != null && !ret.equals("NULL") && field.isAnnotationPresent(DatabaseSerializer.class)) {
                    DatabaseSerializer serializer = field.getAnnotation(DatabaseSerializer.class);
                    ret = ((AbstractSerializer) serializer.serializer().newInstance()).toString(o);
                }
                return ret;
            }
        }
        catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

}
