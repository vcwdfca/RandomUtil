package com.vcwdfca.randomutil.feature.nei;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import codechicken.nei.ItemPanels;
import codechicken.nei.bookmark.BookmarkGrid;
import codechicken.nei.recipe.StackInfo;

final class PatternBookmarks {

    private PatternBookmarks() {}

    static boolean add(ItemStack stack) {
        ItemStack normalized = copySingle(stack);
        return normalized != null && ItemPanels.bookmarkPanel.addItem(normalized);
    }

    static boolean isBookmarked(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        return ItemPanels.bookmarkPanel.getGrid()
            .indexOf(BookmarkGrid.DEFAULT_GROUP_ID, copySingle(stack), null, false) != -1;
    }

    static ItemStack copySingle(ItemStack stack) {
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
