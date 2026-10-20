package com.vcwdfca.randomutil.feature.nei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import com.asdflj.ae2thing.client.gui.GuiWirelessDualInterfaceTerminal;
import com.vcwdfca.randomutil.util.AEClientUtil;
import com.vcwdfca.randomutil.util.Mods;

import appeng.api.storage.StorageName;
import appeng.client.gui.AEBaseGui;
import appeng.client.gui.implementations.GuiPatternTerm;
import appeng.client.gui.slots.VirtualMEPatternSlot;
import appeng.client.gui.slots.VirtualMESlot;
import codechicken.nei.api.INEIGuiHandler;

public final class PatternSlotDropHandler implements INEIGuiHandler {

    public static final PatternSlotDropHandler INSTANCE = new PatternSlotDropHandler();

    private static final List<Class<?>> supportGuis;

    static {
        List<Class<?>> list = new ArrayList<>();
        list.add(GuiPatternTerm.class);
        if (Mods.ae2thing.available()) {
            list.add(GuiWirelessDualInterfaceTerminal.class);
        }
        supportGuis = Collections.unmodifiableList(list);
    }

    private static boolean isSupportGui(GuiContainer gui) {
        for (Class<?> c : supportGuis) {
            if (c.isInstance(gui)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handleDragNDrop(GuiContainer gui, int mouseX, int mouseY, ItemStack draggedStack, int button) {
        if (draggedStack == null || AEClientUtil.isNotTunnelPattern(draggedStack)) {
            return false;
        }
        if (!(gui instanceof AEBaseGui terminal && isSupportGui(gui))) {
            return false;
        }

        VirtualMESlot hovered = terminal.getVirtualMESlotUnderMouse();
        if (!(hovered instanceof VirtualMEPatternSlot slot && slot.getStorageName() == StorageName.CRAFTING_INPUT)) {
            return false;
        }

        AEClientUtil.putIntoSlot(slot, draggedStack, button, GuiScreen.isCtrlKeyDown());
        return true;
    }
}
