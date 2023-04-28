package dev.denny.auction.item;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import dev.denny.auction.utils.ItemData;
import dev.denny.database.DatabasePlugin;
import lombok.Getter;
import lombok.Setter;

public class AuctionItem extends Item {

    @Setter
    @Getter
    private ItemData data;

    public AuctionItem(int id, int meta, int count) {
        super(id, meta, count);
    }

    public void delete() {
        String request = "DELETE FROM auction WHERE id = '%1$s'";
        request = String.format(request, data.getId());

        DatabasePlugin.getDatabase().query(request);
    }

    public void sell(Player whom) {
    }

    public AuctionItem setData(ItemData _data) {
        data = _data;

        return this;
    }
}