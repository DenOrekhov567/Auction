package dev.denny.auction.manager;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import dev.denny.auction.inventory.AuctionInventory;
import dev.denny.auction.inventory.InventoryFactory;
import dev.denny.auction.item.AuctionItem;
import dev.denny.auction.utils.AuctionData;
import dev.denny.auction.utils.ItemData;
import dev.denny.auction.utils.Page;
import dev.denny.database.DatabasePlugin;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuctionManager {


    @Getter
    private Map<Integer, Page> poolPages = new HashMap<>();
    @Getter
    private String prefixMessage = "§f[§aАукцион§f] ";

    public AuctionManager() {
        DatabaseManager.initTable();

        formingItems();
    }

    public void formingItems() {
        String request = "SELECT * FROM auction;";

        List<AuctionData> response = DatabasePlugin.getDatabase().query(request, AuctionData.class);

        if (response == null) {
            return;
        }

        int pageNumber = 1;
        Map<Integer, AuctionItem> currentPageContents = new HashMap<>();

        for (int i = 0; i < response.size(); i++) {
            AuctionData data = response.get(i);
            ItemData itemData = new ItemData(data);

            AuctionItem item = new AuctionItem(data.getIdentifier(), data.getMeta(), data.getAmount());
            item.setData(itemData);
            currentPageContents.put(i % 45, item);

            if (i % 45 == 44 || i == response.size() - 1) {
                poolPages.put(pageNumber, new Page(pageNumber, currentPageContents));
                pageNumber++;
                currentPageContents = new HashMap<>();
            }
        }

        for (Map.Entry<Integer, Page> entry : poolPages.entrySet()) {
            Integer number = entry.getKey();

            entry.getValue().addDefault(entry.getKey() == 1 ? false : true, number < poolPages.size() ? true : false);
        }
    }

    public void openAuction(Player player) {
        AuctionInventory auction = InventoryFactory.createAuction(player);
        auction.build();
        auction.send();
    }

    public void sellItem(Player player, Item item, Integer cost) {
        player.getInventory().setItemInHand(Item.get(Item.AIR, 0, 1));

        String request = "INSERT INTO auction(seller, identifier, meta, amount, cost, endTime) VALUES ('%1$s', %2$s, %3$s, %4$s, %5$s, %6$s);";
        request = String.format(request, player.getName(), item.getId(), item.getDamage(), item.getCount(), cost, System.currentTimeMillis());

        DatabasePlugin.getDatabase().query(request);

        formingItems();
    }
}