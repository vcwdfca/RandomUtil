package com.vcwdfca.randomutil.feature.nei;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import com.vcwdfca.randomutil.util.AEClientUtil;

import appeng.client.gui.implementations.GuiPatternTerm;
import appeng.client.gui.slots.VirtualMEPatternSlot;
import codechicken.nei.api.INEIGuiHandler;

public final class PatternSlotDropHandler implements INEIGuiHandler {

    public static final PatternSlotDropHandler INSTANCE = new PatternSlotDropHandler();

    @Override
    public boolean handleDragNDrop(GuiContainer gui, int mouseX, int mouseY, ItemStack draggedStack, int button) {
        if (draggedStack == null || !(gui instanceof GuiPatternTerm terminal)) {
            return false;
        }
        if (AEClientUtil.isNotTunnelPattern(draggedStack)) {
            return false;
        }

        VirtualMEPatternSlot slot = AEClientUtil.getHoveredInputSlot(terminal);
        if (slot == null) {
            return false;
        }

        draggedStack.stackSize = 1;

        AEClientUtil.putIntoSlot(slot, draggedStack, button, GuiScreen.isCtrlKeyDown());
        return true;
    }
}
