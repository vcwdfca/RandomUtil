package com.vcwdfca.randomutil.feature.nei;

import java.awt.*;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.ItemPanels;
import codechicken.nei.bookmark.BookmarkGrid;
import codechicken.nei.bookmark.BookmarkItem;
import codechicken.nei.recipe.StackInfo;

public final class PatternBookmarks {

    private PatternBookmarks() {}

    public static boolean toggle(ItemStack stack) {
        if (isBookmarked(stack)) {
            return remove(stack);
        }
        return add(stack);
    }

    public static boolean isBookmarked(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        return ItemPanels.bookmarkPanel.getGrid()
            .indexOf(BookmarkGrid.DEFAULT_GROUP_ID, copySingle(stack), null, false) != -1;
    }

    private static boolean add(ItemStack stack) {
        ItemStack normalized = copySingle(stack);
        return normalized != null && ItemPanels.bookmarkPanel.addItem(normalized);
    }

    private static boolean remove(ItemStack stack) {
        if (stack == null) {
            return false;
        }

        ItemStack normalized = copySingle(stack);
        if (normalized == null) {
            return false;
        }

        final Point mouse = GuiDraw.getMousePosition();
        if (ItemPanels.bookmarkPanel.removeSlot(mouse.x, mouse.y, false)) {
            return true;
        }

        if (ItemPanels.bookmarkPanel.removeItem(normalized, null, BookmarkGrid.DEFAULT_GROUP_ID)) {
            return true;
        }

        final BookmarkGrid grid = ItemPanels.bookmarkPanel.getGrid();
        for (int i = grid.size() - 1; i >= 0; i--) {
            final BookmarkItem item = grid.getBookmarkItem(i);

            if (item != null && item.groupId == BookmarkGrid.DEFAULT_GROUP_ID
                && item.type != BookmarkItem.BookmarkItemType.INGREDIENT
                && StackInfo.equalItemAndNBT(stack, item.itemStack, true)) {
                grid.removeRecipe(i, false);
                return true;
            }
        }

        return false;
    }

    private static ItemStack copySingle(ItemStack stack) {
        if (stack == null) {
            return null;
        }
        NBTTagCompound tag = StackInfo.itemStackToNBT(stack);
        if (tag == null) {
            ItemStack copy = stack.copy();
            copy.stackSize = 1;
            return copy;
        }
        return StackInfo.loadFromNBT(tag, 1);
    }
}
