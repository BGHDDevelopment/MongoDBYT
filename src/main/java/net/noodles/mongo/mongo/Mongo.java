package net.noodles.mongo.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Mongo extends JavaPlugin {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> serverCollection;


    @Override
    public void onEnable() {
        // Plugin startup logic
        System.setProperty("DEBUG.GO", "true");
        System.setProperty("DB.TRACE", "true");
        Logger mongoLobber = Logger.getLogger("org.mongodb.driver");
        mongoLobber.setLevel(Level.WARNING);
        connect();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public void connect() {
        MongoCredential mongoCredential = MongoCredential.createCredential("USERNAME", "DATABASE", "PASSWORD".toCharArray());
        mongoClient = new MongoClient(new ServerAddress("HOST", 27017), Collections.singletonList(mongoCredential));
        mongoDatabase = mongoClient.getDatabase("DATABASE");
        serverCollection = mongoDatabase.getCollection("COLLECTIONNAME");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Connected to MongoDB!");
    }


}
