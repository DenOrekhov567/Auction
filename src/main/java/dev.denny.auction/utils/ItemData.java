package dev.denny.auction.utils;

import lombok.Getter;

public class ItemData {

    @Getter
    public Integer id;
    @Getter
    public String seller;
    @Getter
    public Integer identifier;
    @Getter
    public Integer meta;
    @Getter
    public Integer amount;
    @Getter
    public Integer cost;
    @Getter
    public Integer endTime;

    public ItemData(AuctionData data) {
        id = data.id;
        seller = data.seller;
        identifier = data.identifier;
        meta = data.meta;
        amount = data.amount;
        cost = data.cost;
        endTime = data.endTime;
    }
}