package dev.denny.auction.inventory;

import cn.nukkit.Player;

public class InventoryFactory {

    public static AuctionInventory createAuction(Player player) {
        return new AuctionInventory(player);
    }
}