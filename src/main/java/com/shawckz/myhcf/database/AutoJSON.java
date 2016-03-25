/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.database;

import com.google.common.primitives.Primitives;
import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.AbstractSerializer;
import com.shawckz.myhcf.database.annotations.DBColumn;
import com.shawckz.myhcf.database.annotations.DatabaseSerializer;
import com.shawckz.myhcf.database.annotations.JSONDirectory;
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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AutoJSON implements AutoDB {

    private static final JSONParser parser = new JSONParser();

    private File getFile(String id, String folder) {
        return new File(Factions.getInstance().getDataFolder().getPath() + File.separator + folder + File.separator + id + ".json");
    }

    private File getAndCreate(String id, String folder) {
        {
            File dir = new File(Factions.getInstance().getDataFolder() + File.separator + folder);
            if(!dir.exists()){
                dir.mkdirs();
            }
        }

        File f = getFile(id, folder);
        if(!f.exists()) {
            try{
                f.createNewFile();
            }
            catch (IOException ex){
                throw new HCFException("Could not create file (getAndCreate AutoJSON)", ex);
            }
        }
        return f;
    }

    private Document getDocumentFromFile(File file) {
        if(file != null && file.exists()) {
            try {
                Object obj = parser.parse(new FileReader(file));
                return Document.parse(((JSONObject) obj).toJSONString());
            }
            catch (IOException | ParseException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    private void saveToFile(Document document, File file) {
        try {
            System.out.println("Saving file :: Path: " + file.getPath());
            if (!file.exists()) {
                throw new HCFException("File to save to does not exist");
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
    public boolean push(AutoDBable a) {
        AutoDBValue value = new AutoDBValue(a);
        Document document = new Document(value.getIdentifier(), value.getIdentifierValue());
        document.putAll(value.getValues());

        if(value.getIdentifierValue() instanceof String) {
            String id = (String) value.getIdentifierValue();
            saveToFile(document, getAndCreate(id, getDirectory(a).name()));
            return true;
        }
        else{
            throw new HCFException("AutoJSON identifier value must be a String");
        }
    }

    @Override
    public Set<AutoDBable> fetchMultiple(AutoDBable type, DBSearch search) {
        Set<AutoDBable> ret = new HashSet<>();
        if(fetch(type, search)) {
            ret.add(type);
        }
        return ret;
    }

    @Override
    public Set<AutoDBable> fetchAll(AutoDBable type) {
        Set<AutoDBable> ret = new HashSet<>();
        String directory = getDirectory(type).name();
        File dir = new File(Factions.getInstance().getDataFolder().getPath() + File.separator + directory);
        if(dir != null && dir.exists()) {
            for(File f : dir.listFiles()) {
                if(f != null && f.exists()) {
                    Document document = getDocumentFromFile(f);
                    if(document != null) {
                        try {
                            AutoDBable a = type.getClass().newInstance();
                            fromDocument(a, document);
                            ret.add(a);
                        }
                        catch (InstantiationException | IllegalAccessException ex) {
                            throw new HCFException("AutoJSON cannot instantiate AutoDBable");
                        }
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public boolean fetch(AutoDBable a, DBSearch search) {
        if(search.getValue() instanceof String) {

            String id = (String) search.getValue();
            File f = getFile(id, getDirectory(a).name());
            if(f != null && f.exists()) {
                Document document = getDocumentFromFile(f);
                if (document != null) {
                    fromDocument(a, document);
                    return true;
                }
            }
        }
        else{
            throw new HCFException("AutoJSON identifier value must be a String");
        }
        return false;
    }

    @Override
    public boolean delete(AutoDBable a) {
        AutoDBValue value = new AutoDBValue(a);
        if(value.getIdentifierValue() instanceof String) {
            String id = (String) value.getIdentifierValue();
            File f = getFile(id, getDirectory(a).name());
            if(f != null && f.exists()) {
                return f.delete();
            }
            return false;
        }
        else{
            throw new HCFException("AutoJSON identifier value must be a String");
        }
    }

    private JSONDirectory getDirectory(AutoDBable a) {
        if(a.getClass().isAnnotationPresent(JSONDirectory.class)) {
            return a.getClass().getAnnotation(JSONDirectory.class);
        }
        throw new HCFException("JSONDirectory not found for class " + a.getClass().getSimpleName());
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
