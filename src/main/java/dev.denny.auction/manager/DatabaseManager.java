package dev.denny.auction.manager;

import dev.denny.database.DatabasePlugin;
import dev.denny.database.utils.Database;

public class DatabaseManager {

    public static void initTable() {
        Database database = DatabasePlugin.getDatabase();

        String request =
                "CREATE TABLE IF NOT EXISTS auction("
                        + "id INT AUTO_INCREMENT PRIMARY KEY, "
                        + "seller VARCHAR(20) NOT NULL, "
                        + "identifier INTEGER NOT NULL, "
                        + "meta INTEGER NOT NULL, "
                        + "amount INTEGER NOT NULL, "
                        + "cost INTEGER NOT NULL, "
                        + "endTime BIGINT NOT NULL"
                        + ")";

        database.executeScheme(request);
    }
}