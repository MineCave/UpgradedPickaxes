package com.minecave.pickaxes.listener;

import com.minecave.pickaxes.EnhancedPicks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuListener implements Listener {

    private EnhancedPicks plugin = EnhancedPicks.getInstance();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
//        Player player = (Player) event.getWhoClicked();
//        ClickType clickType = event.getClick();
//        Menu menu = Menu.get(event.getInventory().getTitle());
//        if (menu == null) {
//            return;
//        }
//
//        event.setCancelled(true);
//        event.setResult(Result.DENY);
//
//        PlayerInfo info = plugin.getPlayerManager().get(player);
//        if (info == null) {
//            throw new RuntimeException(player.getName() + " doesn't have PlayerInfo.");
//        }
//        if (menu instanceof Page) {
//            menu = ((Page) menu).getMenu();
//        }
//
//        if (menu instanceof PickMenu) {
//            PickMenu pickMenu = (PickMenu) menu;
//            Button button = menu.getButton(event.getRawSlot());
//            if (event.getRawSlot() < event.getView().getTopInventory().getSize() &&
//                    button != null) {
//                if (!(button instanceof PickaxeButton)) {
//                    button.click(player, clickType);
//                } else {
//                    ((PickaxeButton) button).click(player, clickType, event.getRawSlot(), pickMenu);
//                }
//            }
//            Inventory inventory = event.getView().getBottomInventory();
//            ItemStack inhand = event.getCurrentItem();
//            if (inhand == null || inhand.getType() != Material.DIAMOND_PICKAXE) {
//                return;
//            }
//            int slot = event.getSlot();
//            inventory.setItem(slot, null);
//            pickMenu.addButton(new PickaxeButton(inhand), player);
//            info.addPickaxe(plugin.getPItemManager().getPItem(BlockBreakEvent.class, inhand));
//            pickMenu.update(player);
//            return;
//        }
//
//        if (menu instanceof SwordMenu) {
//            SwordMenu swordMenu = (SwordMenu) menu;
//            Button button = menu.getButton(event.getRawSlot());
//            if (event.getRawSlot() < event.getView().getTopInventory().getSize() &&
//                    button != null) {
//                if (!(button instanceof SwordButton)) {
//                    button.click(player, clickType);
//                } else {
//                    ((SwordButton) button).click(player, clickType, event.getRawSlot(), swordMenu);
//                }
//            }
//            Inventory inventory = event.getView().getBottomInventory();
//            ItemStack inhand = event.getCurrentItem();
//            if (inhand == null || inhand.getType() != Material.DIAMOND_SWORD) {
//                return;
//            }
//            int slot = event.getSlot();
//            inventory.setItem(slot, null);
//            swordMenu.addButton(new SwordButton(inhand), player);
//            info.addSword(plugin.getPItemManager().getPItem(EntityDamageByEntityEvent.class, inhand));
//            swordMenu.update(player);
//            return;
//        }
//
//        Button button = menu.getButton(event.getRawSlot());
//        if (button == null) {
//            return;
//        }
//
//        button.click(player, clickType);
    }
}
