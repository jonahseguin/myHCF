package com.shawckz.myhcf.database;

import java.util.Arrays;

import org.bukkit.plugin.Plugin;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.shawckz.myhcf.configuration.Configuration;
import com.shawckz.myhcf.configuration.annotations.ConfigData;


/**
 * The DatabaseManager class
 * Used to handle the connection the MongoClient.
 */
public class DatabaseManager extends Configuration {

    protected static boolean instantiated = false;
    @ConfigData("database.name")
    private static String databaseName = "xxx";
    @ConfigData("database.authName")
    private static String authDatabaseName = "xxx";
    @ConfigData("database.host")
    private static String host = "xxx";
    @ConfigData("database.port")
    private static int port = 3309;
    @ConfigData("database.credentials.username")
    private static String username = "xxx";
    @ConfigData("database.credentials.password")
    private static String password = "xxx";
    @ConfigData("database.useAuth")
    private static boolean useAuth = false;
    private MongoClient mongoClient;
    private MongoDatabase db;


    public DatabaseManager(Plugin plugin) {
        super(plugin, "database.yml");
        if (!instantiated) {
            instantiated = true;
        } else {
            throw new RuntimeException("DatabaseManager instance already exists");
        }
        load();
        save();
        setup();
    }

    private void setup() {

        if (!useAuth) {
            mongoClient = new MongoClient(new ServerAddress(host, port));
        } else {
            MongoCredential credential = MongoCredential.createCredential(username, authDatabaseName, password.toCharArray());
            mongoClient = new MongoClient(new ServerAddress(host, port), Arrays.asList(credential));
        }
        db = mongoClient.getDatabase(databaseName);
    }

    public void shutdown() {
        mongoClient.close();
        db = null;
        mongoClient = null;
    }

    public MongoDatabase getDatabase() {
        return db;
    }

    public void setDb(MongoDatabase db) {
        this.db = db;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }
}