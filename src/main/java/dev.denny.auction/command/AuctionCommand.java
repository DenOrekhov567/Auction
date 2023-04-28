package dev.denny.auction.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import dev.denny.auction.AuctionPlugin;
import dev.denny.pattern.command.CommandBase;
import ru.nukkitx.forms.elements.CustomForm;
import ru.nukkitx.forms.elements.ModalForm;
import ru.nukkitx.forms.elements.SimpleForm;

public class AuctionCommand extends CommandBase {

    public AuctionCommand() {
        super("auction", "", "");

        setDescription("Открыть аукцион");
        setUsage("§7> §fИспользовать: §a/auction");

        setPermission("command.auction.use", 0);

        getCommandParameters().clear();
    }

    @Override
    public boolean executeSafe(CommandSender sender, String[] args) {
        if(!sender.isPlayer()) {
            sender.sendMessage("Данную команду можно использовать только игроку!");
        }
        openAuctionMenu((Player) sender);

        return true;
    }


    public void openAuctionMenu(Player player) {
        SimpleForm form = new SimpleForm("Аукцион");
        form.setContent("§aГлавное меню" +
                "\n\n§7• §aКупить §f— купить предмет в руке" +
                "\n§7• §aПродать §f— продай предмет в руке");
        form.addButton("Купить");
        form.addButton("Продать");
        form.send(player, (targetPlayer, targetForm, data) -> {
            if (data == -1) return;

            if (data == 0) {
                AuctionPlugin.getInstance().getManager().openAuction(targetPlayer);
            } else if (data == 1) {
                openSellItemMenu(player, player.getInventory().getItemInHand());

                return;
            }
        });
    }

    public void openSellItemMenu(Player player, Item item) {
        if (item.getId() == Item.AIR) {
            ModalForm form = new ModalForm("Аукцион","§aПродажа предмета"+
            "\n\n§fУ §aтебя §fв руке нет предмета",
            "Вернуться в главное меню",
            "Закрыть"
            );
            form.send(player, (targetPlayer1, targetForm1, data1) -> {
                if (data1 == -1) return;

                if (data1 == 0) {
                    openAuctionMenu(targetPlayer1);
                }
            });

            return;
        }

        CustomForm form2 = new CustomForm("Аукцион");

        form2.addLabel("§aПродажа предмета" +
                "\n\n§7• §fНазвание: §a" + item.getName() +
                "\n§7• §fАйди: §a" + item.getId() +
                "\n§7• §fМета: §a" + item.getDamage() +
                "\n§7• §fКоличество: §a" + item.getCount()
        );
        form2.addSlider("§fУкажи цену", 0, 1000, 10);
        form2.send(player, (targetPlayer2, targetForm2, data2) -> {
            if (data2 == null) return;

            ModalForm form3 = new ModalForm("Аукцион", "§aПодтверждение продажи" +
                    "\n\n§fПредмет:" +
                    "\n§7• §fНазвание: §a" + item.getName() +
                    "\n§7• §fАйди: §a" + item.getId() +
                    "\n§7• §fМета: §a" + item.getDamage() +
                    "\n§7• §fКоличество: §a" + item.getCount() +
                    "\n\n§7• §fЦена: §a" + Float.valueOf(data2.get(1).toString()).intValue(),
                    "Подтвердить", "Закрыть"
            );
            form3.send(player, (targetPlayer3, targetForm3, data3) -> {
                if (data3 == -1) return;

                if (data3 == 0) AuctionPlugin.getInstance().getManager().sellItem(targetPlayer3, item, Float.valueOf(data2.get(1).toString()).intValue());
            });
        });
    }
}