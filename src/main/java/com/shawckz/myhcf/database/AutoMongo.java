/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.database;

import com.google.common.primitives.Primitives;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.AbstractSerializer;
import com.shawckz.myhcf.database.annotations.CollectionName;
import com.shawckz.myhcf.database.annotations.DBColumn;
import com.shawckz.myhcf.database.annotations.DatabaseSerializer;
import com.shawckz.myhcf.database.search.DBSearch;
import com.shawckz.myhcf.util.HCFException;
import org.apache.commons.lang.ClassUtils;
import org.bson.Document;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class AutoMongo implements AutoDB {

    @Override
    public boolean push(AutoDBable a) {
        if (!a.getClass().isAnnotationPresent(CollectionName.class)) {
            throw new HCFException("CollectionName not found while using AutoMongo");
        }
        String collection = a.getClass().getAnnotation(CollectionName.class).name();

        MongoCollection<Document> col = Factions.getInstance().getDatabaseManager().getDatabase().getCollection(collection);

        AutoDBValue value = new AutoDBValue(a);

        if(!value.isValid()) {
            throw new HCFException("Identifier not found while using AutoMongo");
        }

        Document doc = new Document(value.getIdentifier(), value.getIdentifierValue());
        BasicDBObject searchQuery = new BasicDBObject().append(value.getIdentifier(), value.getIdentifierValue());

        doc.putAll(value.getValues());

        if (documentExists(searchQuery, col)) {
            col.updateOne(searchQuery, new Document("$set", doc));
        }
        else {
            col.insertOne(doc);
        }
        return true;
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
    public boolean fetch(AutoDBable a, DBSearch search) {
        if (!a.getClass().isAnnotationPresent(CollectionName.class)) {
            throw new HCFException("CollectionName not found while using AutoMongo");
        }
        CollectionName collectionName = a.getClass().getAnnotation(CollectionName.class);
        if (collectionName == null) {
            throw new HCFException("CollectionName not found while using AutoMongo");
        }

        MongoCollection<Document> col = Factions.getInstance().getDatabaseManager().getDatabase().getCollection(collectionName.name());

        MongoCursor<Document> cursor = col.find(new Document(search.getKey(), search.getValue())).iterator();

        if(cursor.hasNext()) {
            fromDocument(a, cursor.next());
            return true;
        }
        return false;
    }

    @Override
    public Set<AutoDBable> fetchMultiple(AutoDBable type, DBSearch search) {
        if (!type.getClass().isAnnotationPresent(CollectionName.class)) {
            throw new HCFException("CollectionName not found while using AutoMongo");
        }
        CollectionName collectionName = type.getClass().getAnnotation(CollectionName.class);
        if (collectionName == null) {
            throw new HCFException("CollectionName not found while using AutoMongo");
        }

        MongoCollection<Document> col = Factions.getInstance().getDatabaseManager().getDatabase().getCollection(collectionName.name());

        MongoCursor<Document> cursor = col.find(new Document(search.getKey(), search.getValue())).iterator();

        Set<AutoDBable> result = new HashSet<>();

        while(cursor.hasNext()) {
            try {
                AutoDBable a = type.getClass().newInstance();
                fromDocument(a, cursor.next());
                result.add(a);
            }
            catch (InstantiationException | IllegalAccessException ex) {
                throw new HCFException("AutoMongo cannot instantiate AutoDBable");
            }
        }

        return result;
    }

    @Override
    public Set<AutoDBable> fetchAll(AutoDBable type) {
        if (!type.getClass().isAnnotationPresent(CollectionName.class)) {
            throw new HCFException("CollectionName not found while using AutoMongo");
        }
        CollectionName collectionName = type.getClass().getAnnotation(CollectionName.class);
        if (collectionName == null) {
            throw new HCFException("CollectionName not found while using AutoMongo");
        }

        MongoCollection<Document> col = Factions.getInstance().getDatabaseManager().getDatabase().getCollection(collectionName.name());

        MongoCursor<Document> cursor = col.find(new Document()).iterator();

        Set<AutoDBable> result = new HashSet<>();

        while(cursor.hasNext()) {
            try {
                AutoDBable a = type.getClass().newInstance();
                fromDocument(a, cursor.next());
                result.add(a);
            }
            catch (InstantiationException | IllegalAccessException ex) {
                throw new HCFException("AutoMongo cannot instantiate AutoDBable");
            }
        }

        return result;
    }

    @Override
    public Document toDocument(AutoDBable a) {
        AutoDBValue value = new AutoDBValue(a);
        Document document = new Document(value.getIdentifier(), value.getIdentifierValue());
        document.putAll(value.getValues());
        return document;
    }

    @Override
    public boolean delete(AutoDBable a) {
        if (!a.getClass().isAnnotationPresent(CollectionName.class)) {
            throw new HCFException("CollectionName not found while using AutoMongo");
        }
        String collection = a.getClass().getAnnotation(CollectionName.class).name();

        MongoCollection<Document> col = Factions.getInstance().getDatabaseManager().getDatabase().getCollection(collection);
        AutoDBValue value = new AutoDBValue(a);
        BasicDBObject searchQuery = new BasicDBObject().append(value.getIdentifier(), value.getIdentifierValue());
        DeleteResult result = col.deleteOne(searchQuery);
        if(result.wasAcknowledged() && result.getDeletedCount() > 0) {
            return true;
        }
        return false;
    }

    private boolean documentExists(BasicDBObject search, MongoCollection col) {
        FindIterable<Document> ret = col.find(search).limit(1);
        return ret.iterator().hasNext();
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