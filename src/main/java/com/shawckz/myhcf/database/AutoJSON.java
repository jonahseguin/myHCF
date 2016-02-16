/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.database;

import com.google.common.primitives.Primitives;
import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.AbstractSerializer;
import com.shawckz.myhcf.database.annotations.DBColumn;
import com.shawckz.myhcf.database.annotations.DatabaseSerializer;
import com.shawckz.myhcf.database.search.DBSearch;
import com.shawckz.myhcf.util.HCFException;
import org.apache.commons.lang.ClassUtils;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class AutoJSON implements AutoDB {

    private static final JSONParser parser = new JSONParser();

    private File getFile(String id) {
        return new File(Factions.getInstance().getDataFolder() + File.separator + "players" + File.separator + id.toLowerCase() + ".json");
    }

    private Document getDocumentFromFile(File file) {
        try {
            Object obj = parser.parse(new FileReader(file));
            return Document.parse(((JSONObject)obj).toJSONString());
        }
        catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void saveToFile(Document document, File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            else {
                new PrintWriter(file).close();
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(document.toJson());
            fileWriter.flush();
            fileWriter.close();

        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void push(AutoDBable a) {
        AutoDBValue value = new AutoDBValue(a);
        Document document = new Document(value.getIdentifier(), value.getIdentifierValue());
        document.putAll(value.getValues());

        if(value.getIdentifierValue() instanceof String) {
            String id = (String) value.getIdentifierValue();
            saveToFile(document, getFile(id));
        }
        else{
            throw new HCFException("AutoJSON identifier value must be a String");
        }
    }

    @Override
    public void fetch(AutoDBable a, DBSearch search) {
        if(search.getValue() instanceof String) {
            String id = (String) search.getValue();
            Document document = getDocumentFromFile(getFile(id));
            fromDocument(a, document);
        }
        else{
            throw new HCFException("AutoJSON identifier value must be a String");
        }
    }

    @Override
    public void delete(AutoDBable a) {
        AutoDBValue value = new AutoDBValue(a);
        if(value.getIdentifierValue() instanceof String) {
            String id = (String) value.getIdentifierValue();
            getFile(id).delete();
        }
        else{
            throw new HCFException("AutoJSON identifier value must be a String");
        }
    }

    @Override
    public void fromDocument(AutoDBable a, Document doc) {
        for (Field field : a.getClass().getDeclaredFields()) {
            DBColumn mongoColumn = field.getAnnotation(DBColumn.class);
            if (mongoColumn != null) {
                String columnName = mongoColumn.name();
                if (columnName.equals("")) {
                    columnName = field.getName();
                }
                Object value = doc.get(columnName);
                if (value != null) {
                    setValue(a, value, field.getType(), field);
                }
            }
        }
    }

    @Override
    public Document toDocument(AutoDBable a) {
        AutoDBValue value = new AutoDBValue(a);
        Document document = new Document(value.getIdentifier(), value.getIdentifierValue());
        document.putAll(value.getValues());
        return document;
    }

    private void setValue(AutoDBable a, Object value, Class<?> type, Field field) {
        try {
            if (type.isPrimitive()) {
                type = ClassUtils.primitiveToWrapper(type);
            }
            field.setAccessible(true);
            if (value == null || type.equals(value.getClass())) {
                field.set(a, value);
            }
            else if (field.isAnnotationPresent(DatabaseSerializer.class)) {
                AbstractSerializer serializer = field.getAnnotation(DatabaseSerializer.class).serializer().newInstance();
                field.set(a, serializer.fromString(value));
            }
            else if (type.equals(UUID.class)) {
                field.set(a, type.getDeclaredMethod("fromString", String.class).invoke(null, value.toString()));
            }
            else if (type.equals(String.class)) {
                field.set(a, String.valueOf(value));
            }
            else if (!Primitives.isWrapperType(type) && !type.equals(String.class) && !type.equals(Long.class) && !type.isPrimitive()) {
                field.set(a, type.getDeclaredMethod("valueOf", String.class).invoke(null, value.toString()));
            }
            else {
                field.set(a, type.getDeclaredMethod("valueOf", value.getClass()).invoke(null, value.toString()));
            }
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
