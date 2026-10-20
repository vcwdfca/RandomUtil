package com.vcwdfca.randomutil.feature.nei;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import com.vcwdfca.randomutil.RandomUtil;
import com.vcwdfca.randomutil.util.AEClientUtil;

import codechicken.nei.KeyManager;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.guihook.IContainerInputHandler;

public final class PatternBookmarkKeyHandler implements IContainerInputHandler {

    public static final PatternBookmarkKeyHandler INSTANCE = new PatternBookmarkKeyHandler();

    @Override
    public boolean keyTyped(GuiContainer gui, char keyChar, int keyCode) {
        if (!NEIClientConfig.isLoaded()) {
            return false;
        }

        int bookmarkKey = KeyManager.getKeyCode(NEIIntegration.KEY_BOOKMARK);
        if (bookmarkKey == Keyboard.KEY_NONE || keyCode != bookmarkKey) {
            return false;
        }

        ItemStack hovered = GuiContainerManager.getStackMouseOver(gui);
        if (AEClientUtil.isNotTunnelPattern(hovered)) {
            return false;
        }

        final boolean wasBookmarked = PatternBookmarks.isBookmarked(hovered);
        if (!PatternBookmarks.toggle(hovered)) {
            return false;
        }

        if (RandomUtil.debugEnabled()) {
            RandomUtil.LOG.info(
                "{} tunnel pattern {}",
                wasBookmarked ? "Unbookmarked" : "Bookmarked",
                AEClientUtil.getTunnelUuid(hovered));
        }
        return true;
    }

    @Override
    public void onKeyTyped(GuiContainer gui, char keyChar, int keyID) {}

    @Override
    public boolean lastKeyTyped(GuiContainer gui, char keyChar, int keyID) {
        return false;
    }

    @Override
    public boolean mouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
        return false;
    }

    @Override
    public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button) {}

    @Override
    public void onMouseUp(GuiContainer gui, int mousex, int mousey, int button) {}

    @Override
    public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
        return false;
    }

    @Override
    public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {}

    @Override
    public void onMouseDragged(GuiContainer gui, int mousex, int mousey, int button, long heldTime) {}
}
