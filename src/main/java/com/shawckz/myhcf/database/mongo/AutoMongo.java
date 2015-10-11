package com.shawckz.myhcf.database.mongo;

import com.google.common.primitives.Primitives;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.AbstractSerializer;
import com.shawckz.myhcf.database.mongo.annotations.CollectionName;
import com.shawckz.myhcf.database.mongo.annotations.DatabaseSerializer;
import com.shawckz.myhcf.database.mongo.annotations.MongoColumn;
import org.apache.commons.lang.ClassUtils;
import org.bson.Document;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;

public abstract class AutoMongo {

    public void update() {
        if (!this.getClass().isAnnotationPresent(CollectionName.class)) {
            Bukkit.getLogger().log(Level.SEVERE, "Table Name Class Not Found While Using AutoMongo (" + this.getClass().getSimpleName() + ")");
            return;
        }
        String tableName = this.getClass().getAnnotation(CollectionName.class).name();

        MongoCollection<Document> col = Factions.getInstance().getDatabaseManager().getDatabase().getCollection(tableName);

        String identifier = null;
        Object identifierValue = null;
        HashMap<String, Object> values = new HashMap<>();

        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            MongoColumn column = field.getAnnotation(MongoColumn.class);
            if (column != null) {
                String columnName = column.name();
                if (columnName.equals("")) {
                    columnName = field.getName();
                }
                Class<?> type = field.getType();
                if (type.isPrimitive()) {
                    type = ClassUtils.primitiveToWrapper(type);
                }
                if (column.identifier()) {
                    identifier = columnName;
                    identifierValue = getValue(field);
                } else {
                    values.put(columnName, getValue(field));
                }
            }
        }

        if (identifier == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Identifier Not Found While Using AutoMongo (" + this.getClass().getSimpleName() + ")");
            return;
        }
        Document doc = new Document(identifier, identifierValue);
        BasicDBObject searchQuery = new BasicDBObject().append(identifier, identifierValue);

        doc.putAll(values);

        if (documentExists(searchQuery, col)) {
            col.updateOne(searchQuery, new Document("$set", doc));
        } else {
            col.insertOne(doc);
        }
    }

    public static List<AutoMongo> select(BasicDBObject search, Class<? extends AutoMongo> type) {
        List<AutoMongo> vals = new ArrayList<>();
        CollectionName collectionName = type.getAnnotation(CollectionName.class);
        if (collectionName == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Table Name Class Not Found While Using AutoMongo (" + type.getSimpleName() + ")");
            return vals;
        }

        MongoCollection<Document> col = Factions.getInstance().getDatabaseManager().getDatabase().getCollection(collectionName.name());

        MongoCursor<Document> cursor = col.find(search).iterator();

        while (cursor.hasNext()) {
            Document doc = cursor.next();

            try {
                AutoMongo mongo = type.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    MongoColumn mongoColumn = field.getAnnotation(MongoColumn.class);
                    if (mongoColumn != null) {
                        String columnName = mongoColumn.name();
                        if (columnName.equals("")) {
                            columnName = field.getName();
                        }
                        Object value = doc.get(columnName);
                        if (value != null) {
                            mongo.setValue(value, field.getType(), field);
                        }
                    }
                }
                vals.add(mongo);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return vals;
    }

    public void delete() {
        if (!this.getClass().isAnnotationPresent(CollectionName.class)) {
            Bukkit.getLogger().log(Level.SEVERE, "Table Name Class Not Found While Using AutoMongo (" + this.getClass().getSimpleName() + ")");
            return;
        }
        String tableName = this.getClass().getAnnotation(CollectionName.class).name();

        MongoCollection<Document> col = Factions.getInstance().getDatabaseManager().getDatabase().getCollection(tableName);

        String identifier = null;
        Object identifierValue = null;

        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            MongoColumn column = field.getAnnotation(MongoColumn.class);
            if (column != null) {
                Class<?> type = field.getType();
                if (type.isPrimitive()) {
                    type = ClassUtils.primitiveToWrapper(type);
                }
                if (column.identifier()) {
                    String columnName = column.name();
                    if (columnName.equals("")) {
                        columnName = field.getName();
                    }
                    identifier = columnName;
                    identifierValue = getValue(field);
                }
            }
        }
        BasicDBObject searchQuery = new BasicDBObject().append(identifier, identifierValue);
        col.deleteOne(searchQuery);
    }

    private boolean documentExists(BasicDBObject search, MongoCollection col) {
        FindIterable<Document> ret = col.find(search).limit(1);
        for (Document found : ret) {
            return true;
        }
        return false;
    }

    public String getValue(Field field) {
        try {
            Object o = field.get(this);
            if (o == null) {
                o = "NULL";
            }
            String ret = o.toString();
            if (field.isAnnotationPresent(DatabaseSerializer.class)) {
                DatabaseSerializer serializer = field.getAnnotation(DatabaseSerializer.class);
                ret = ((AbstractSerializer) serializer.serializer().newInstance()).toString(o);
            }
            return ret;
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setValue(Object value, Class<?> type, Field field) {
        try {
            if (type.isPrimitive()) {
                type = ClassUtils.primitiveToWrapper(type);
            }
            field.setAccessible(true);
            if (value == null || type.equals(value.getClass())) {
                field.set(this, value);
            } else if (field.isAnnotationPresent(DatabaseSerializer.class)) {
                AbstractSerializer serializer = field.getAnnotation(DatabaseSerializer.class).serializer().newInstance();
                field.set(this, serializer.fromString(value));
            } else if (type.equals(UUID.class)) {
                field.set(this, type.getDeclaredMethod("fromString", String.class).invoke(null, value.toString()));
            } else if (!Primitives.isWrapperType(type) && !type.equals(String.class) && !type.equals(Long.class) && !type.isPrimitive()) {
                field.set(this, type.getDeclaredMethod("valueOf", String.class).invoke(null, value.toString()));
            } else {
                field.set(this, type.getDeclaredMethod("valueOf", value.getClass()).invoke(null, value.toString()));
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}