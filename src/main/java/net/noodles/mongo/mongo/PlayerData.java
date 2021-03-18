package net.noodles.mongo.mongo;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import org.bson.Document;

import java.util.UUID;

@Getter
public class PlayerData {

    private Mongo plugin = Mongo.getInstance();

    private java.util.UUID UUID;
    private String playerName;

    private Stat blocksMined = new Stat();

    public PlayerData(UUID uuid, String name) {
        this.UUID = uuid;
        this.playerName = name;
    }

    public void resetPlayer() {
        this.blocksMined.setAmount(0);
    }

    public void load() {
        Document document = plugin.getServerCollection().find(Filters.eq("uuid", getUUID().toString())).first();
        if(document !=null) {
            this.blocksMined.setAmount(document.getInteger("blocksMined"));
        }
    }

    public void save() {
        Document document = new Document();
        document.put("name", getPlayerName().toLowerCase());
        document.put("realName", getPlayerName());
        document.put("uuid", getUUID().toString());
        document.put("blocksMined", this.blocksMined.getAmount());
        plugin.getServerCollection().replaceOne(Filters.eq("uuid", getUUID().toString()), document, new UpdateOptions().upsert(true));
    }





}
