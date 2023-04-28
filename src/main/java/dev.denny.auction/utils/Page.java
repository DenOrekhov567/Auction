package dev.denny.auction.utils;

import dev.denny.auction.item.AuctionItem;
import dev.denny.auction.item.ItemFactory;
import lombok.Getter;

import java.util.Map;

public class Page {

    @Getter
    private Integer number;
    @Getter
    private Map<Integer, AuctionItem> contents;

    public Page(Integer _number, Map<Integer, AuctionItem> _contents) {
        number = _number;
        contents = _contents;
    }

    public void addDefault(Boolean addedLast, Boolean addedNext) {
        if(addedLast) {
            AuctionItem item = ItemFactory.get(1, 0, 1);
            item.setCustomName("§r§aПерейти на предыдущую страницу");
            contents.put(47, item);
        }

        if(addedNext) {
            AuctionItem item = ItemFactory.get(1, 0, 1);
            item.setCustomName("§r§aПерейти на следующую страницу");
            contents.put(51, item);
        }
    }
}