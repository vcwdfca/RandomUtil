package com.vcwdfca.randomutil.util;

import java.util.UUID;

import net.minecraft.item.ItemStack;

import appeng.client.gui.slots.VirtualMEPatternSlot;
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

    public static void putIntoSlot(VirtualMEPatternSlot slot, ItemStack stack, int mouseButton, boolean extraAction) {
        slot.handleMouseClicked(stack, extraAction, mouseButton);
    }
}
