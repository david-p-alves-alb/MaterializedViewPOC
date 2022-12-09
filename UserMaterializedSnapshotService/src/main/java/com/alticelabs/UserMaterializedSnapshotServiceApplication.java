package com.alticelabs;

import com.alticelabs.controller.UserMaterializedController;
import com.alticelabs.repository.KafkaMaterializedView;

public class UserMaterializedSnapshotServiceApplication {

    public static void main(String[] args) {
        KafkaMaterializedView kafkaMaterializedView = new KafkaMaterializedView();
        UserMaterializedController userMaterializedController = new UserMaterializedController(kafkaMaterializedView);
        userMaterializedController.run();
        System.exit(1);
    }
}