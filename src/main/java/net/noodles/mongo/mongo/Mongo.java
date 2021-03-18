package net.noodles.mongo.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public final class Mongo extends JavaPlugin implements Listener {

    @Getter
    public static Mongo instance;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> serverCollection;
    private ProfileManager profileManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        System.setProperty("DEBUG.GO", "true");
        System.setProperty("DB.TRACE", "true");
        Logger mongoLobber = Logger.getLogger("org.mongodb.driver");
        mongoLobber.setLevel(Level.WARNING);
        connect();
        profileManager = new ProfileManager(this);
        getServer().getPluginManager().registerEvents(this, this);
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


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        getProfileManager().handleProfileCreation(player.getUniqueId(), player.getName());
        Profile profile = getProfileManager().getProfile(player.getUniqueId());
        profile.getData().load();
        Bukkit.broadcastMessage("Loaded " + player.getName() + " data!");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Profile profile = getProfileManager().getProfile(player.getUniqueId());
        profile.getData().getBlocksMined().increaseAmount(1);
        profile.getData().save();
        Bukkit.broadcastMessage("Saved " + player.getName() + " data!");
    }
}
