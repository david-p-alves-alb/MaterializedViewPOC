package com.alticelabs;

import com.alticelabs.CDC.MongoCDC;
import com.alticelabs.database.MongoConnection;
import com.mongodb.client.MongoDatabase;

public class CDCMongoApplication {
    public static void main(String[] args) {
        MongoDatabase mongoDatabase = MongoConnection.connectToDatabase();

        MongoCDC mongoCDC = new MongoCDC(mongoDatabase);
        mongoCDC.startCDC();
    }




}