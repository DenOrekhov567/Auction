package dev.denny.auction.item;

import dev.denny.auction.utils.ItemData;

public class ItemFactory {

    public static AuctionItem get(Integer id, Integer meta, Integer count) {
        return new AuctionItem(id, meta, count);
    }

    public static AuctionItem get(ItemData data) {
        return new AuctionItem(data.getIdentifier(), data.getMeta(), data.getAmount()).setData(data);
    }
}