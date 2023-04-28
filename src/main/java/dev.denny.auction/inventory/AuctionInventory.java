package dev.denny.auction.inventory;

import cn.nukkit.Player;
import cn.nukkit.inventory.InventoryType;
import dev.denny.account.player.Account;
import dev.denny.account.player.AccountSource;
import dev.denny.account.player.Gamer;
import dev.denny.account.utils.Utils;
import dev.denny.auction.AuctionPlugin;
import dev.denny.auction.item.AuctionItem;
import dev.denny.auction.item.ItemFactory;
import dev.denny.auction.manager.AuctionManager;
import dev.denny.auction.utils.ItemData;
import dev.denny.auction.utils.Page;
import me.iwareq.fakeinventories.CustomInventory;

import java.util.Map;
import java.util.Objects;

public class AuctionInventory extends CustomInventory {

    private static AuctionInventory inventory;

    private final Player player;

    private Integer currentPage = 1;

    public AuctionInventory(Player _player) {
        super(InventoryType.DOUBLE_CHEST, "§aАукцион");

        player = _player;
    }

    public void build() {
        this.clearAll();

        openPage(currentPage);
    }

    public void openPage(Integer number) {
        currentPage = number;

        clearAll();

        AuctionManager manager = AuctionPlugin.getInstance().getManager();

        Map<Integer, Page> pages = AuctionPlugin.getInstance().getManager().getPoolPages();
        if (pages == null) {
            player.sendMessage(manager.getPrefixMessage() + "§fНет торгов на данный момент");

            return;
        }

        for (Map.Entry<Integer, Page> entry : pages.entrySet()) {
            if (Objects.equals(currentPage, entry.getKey())) {
                Page page = entry.getValue();
                Map<Integer, AuctionItem> contents = page.getContents();
                for (Map.Entry<Integer, AuctionItem> content : contents.entrySet()) {
                    Integer index = content.getKey();
                    AuctionItem item = content.getValue();

                    if(item.getData() == null) {
                        if(item.getCustomName().equals("§r§aПерейти на предыдущую страницу")) {
                            setItem(index, item, (targetItem, event) -> {
                                openPage(currentPage - 1);

                                event.setCancelled(true);
                            });
                        } else if(item.getCustomName().equals("§r§aПерейти на следующую страницу")) {
                            setItem(index, item, (targetItem, event) -> {
                                openPage(currentPage + 1);

                                event.setCancelled(true);
                            });
                        }

                    } else {
                        item.setLore(
                                "",
                                "§r§fПродавец: §a" + item.getData().getSeller(),
                                "",
                                "§fАйди §a" + item.getData().getIdentifier(),
                                "§fМета: §a" + item.getData().getMeta(),
                                "§fКоличество: §a" + item.getData().getAmount(),
                                "",
                                "§fЦена: §a" + item.getData().getCost()
                        );
                        setItem(index, item, (targetItem, event) -> {
                            close(player);

                            event.setCancelled(true);

                            ItemData itemData = item.getData();
                            if(itemData.getSeller().equalsIgnoreCase(player.getName())) {
                                player.sendMessage(manager.getPrefixMessage() + "§aТы §fне можешь купить свой предмет");

                                return;
                            }

                            Account account = ((Gamer) player).getAccount();
                            if(account.getData().getCoins() < itemData.getCost()) {
                                player.sendMessage(manager.getPrefixMessage() + "§fНедостаточно средств");

                                return;
                            }

                            AccountSource accountSeller = Utils.getAccount(itemData.getSeller());
                            if(accountSeller == null) {
                                player.sendMessage(manager.getPrefixMessage() + "§fАккаунт продавца не найден");

                                return;
                            }

                            accountSeller.getData().coins += itemData.getCost();
                            accountSeller.update(null);

                            account.getData().coins -= itemData.getCost();
                            accountSeller.update("coins");

                            player.getInventory().addItem(item);
                            item.delete();

                            manager.formingItems();

                            player.sendMessage(manager.getPrefixMessage() + "§aУспешная покупка!\n" +
                                    "§7• §fЛот: §a" + item.toString() + "\n" +
                                    "§7• §fПродавец: §a" + itemData.getSeller() + "\n" +
                                    "§7• §fЦена: §a" + itemData.getCost()
                            );

                            Player seller = player.getServer().getPlayer(itemData.getSeller());
                            if(seller != null) {
                                seller.sendMessage(manager.getPrefixMessage() + "§aУспешная продажа!\n" +
                                        "§7• §fЛот: §a" + item.toString() + "\n" +
                                        "§7• §fПродавец: §a" + itemData.getSeller() + "\n" +
                                        "§7• §fЦена: §a" + itemData.getCost()
                                );
                            }
                        });
                    }
                }
            }
        }
    }

    public void send() {
        player.addWindow(this);
    }
}