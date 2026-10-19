package com.vcwdfca.randomutil.util;

import java.util.UUID;

import net.minecraft.item.ItemStack;

import appeng.api.storage.StorageName;
import appeng.client.gui.implementations.GuiPatternTerm;
import appeng.client.gui.slots.VirtualMEPatternSlot;
import appeng.client.gui.slots.VirtualMESlot;
import appeng.items.misc.ItemTunnelPattern;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class AEClientUtil {

    private AEClientUtil() {}

    public static boolean isNotTunnelPattern(ItemStack stack) {
        return !ItemTunnelPattern.isTunnelPattern(stack);
    }

    public static UUID getTunnelUuid(ItemStack stack) {
        return ItemTunnelPattern.getTunnelUuid(stack);
    }

    public static VirtualMEPatternSlot getHoveredInputSlot(GuiPatternTerm terminal) {
        VirtualMESlot hovered = terminal.getVirtualMESlotUnderMouse();
        if (!(hovered instanceof VirtualMEPatternSlot slot)) {
            return null;
        }
        return slot.getStorageName() == StorageName.CRAFTING_INPUT ? slot : null;
    }

    public static void putIntoSlot(VirtualMEPatternSlot slot, ItemStack stack, int mouseButton, boolean extraAction) {
        slot.handleMouseClicked(stack, extraAction, mouseButton);
    }
}
