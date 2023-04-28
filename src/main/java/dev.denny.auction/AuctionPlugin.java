package dev.denny.auction;

import cn.nukkit.plugin.PluginBase;
import dev.denny.auction.command.AuctionCommand;
import dev.denny.auction.manager.AuctionManager;
import lombok.Getter;

public class AuctionPlugin extends PluginBase {

    @Getter
    public static AuctionPlugin instance;

    @Getter
    private AuctionManager manager;

    @Override
    public void onEnable() {
        instance = this;
        manager = new AuctionManager();

        getServer().getCommandMap().register("", new AuctionCommand());
    }
}