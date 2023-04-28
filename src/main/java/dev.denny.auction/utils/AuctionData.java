package dev.denny.auction.utils;

import lombok.Getter;

public class AuctionData {

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
}
