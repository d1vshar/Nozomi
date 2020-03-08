package io.github.adorableskullmaster.nozomi.core.mongo;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoBotConnection {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<AllianceConfig> allianceConfigMongoCollection;
    private MongoCollection<TrackedWar> trackedWarMongoCollection;

    public MongoBotConnection(MongoClientSettings mongoClientSettings) {
        this.mongoClient = MongoClients.create(mongoClientSettings);
        this.mongoDatabase = mongoClient.getDatabase("posca");
        this.allianceConfigMongoCollection = mongoDatabase.getCollection("aaCfg", AllianceConfig.class);
        this.trackedWarMongoCollection = mongoDatabase.getCollection("trackedWar", TrackedWar.class);
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public MongoCollection<AllianceConfig> getAllianceConfigMongoCollection() {
        return allianceConfigMongoCollection;
    }

    public MongoCollection<TrackedWar> getTrackedWarMongoCollection() {
        return trackedWarMongoCollection;
    }
}
