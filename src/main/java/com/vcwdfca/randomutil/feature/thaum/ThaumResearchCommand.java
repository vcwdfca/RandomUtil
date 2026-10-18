package com.vcwdfca.randomutil.feature.thaum;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import com.vcwdfca.randomutil.command.Chat;
import com.vcwdfca.randomutil.command.Opts;
import com.vcwdfca.randomutil.command.SubCommand;
import com.vcwdfca.randomutil.network.QueryClient;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;

public final class ThaumResearchCommand implements SubCommand {

    @Override
    public String name() {
        return "thaumcraft";
    }

    @Override
    public ObjectList<String> aliases() {
        ObjectList<String> aliases = new ObjectArrayList<>();
        aliases.add("thaum");
        aliases.add("tc");
        return aliases;
    }

    @Override
    public String descriptionKey() {
        return "randomutil.thaum.description";
    }

    @Override
    public ObjectList<String> usageKeys() {
        ObjectList<String> usage = new ObjectArrayList<>();
        usage.add("randomutil.thaum.usage");
        return usage;
    }

    @Override
    public void execute(ICommandSender sender, ObjectList<String> args, Opts opts) {
        if (args.isEmpty() || !"research".equalsIgnoreCase(args.get(0))) {
            Chat.send(sender, Chat.tr("randomutil.thaum.usage"));
            return;
        }

        if (!ThaumFacade.available()) {
            Chat.error(sender, Chat.tr("randomutil.thaum.msg.noThaumcraft"));
            return;
        }

        String chapterInput = args.size() >= 2 ? args.get(1) : "all";
        Chapter chapter = ThaumFacade.resolveChapter(chapterInput);
        if (chapter == null) {
            Chat.error(sender, Chat.tr("randomutil.thaum.msg.unknownChapter", chapterInput));
            Chat.send(
                sender,
                Chat.tr("randomutil.thaum.msg.chapters", String.join(", ", ThaumFacade.chapterSuggestions())));
            return;
        }

        String self = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
        String target = args.size() >= 3 ? args.get(2) : self;

        if (!target.equalsIgnoreCase(self)) {
            QueryClient.request(sender, target, chapter, opts);
            return;
        }

        ObjectList<String> completed = ClientResearch.completedFor(self);
        if (completed == null) {
            Chat.send(sender, Chat.tr("randomutil.thaum.msg.notSynced"));
            return;
        }

        ResearchSnapshot snapshot = ThaumFacade
            .snapshot(completed, chapter, self, opts.includeHidden(), opts.onlyAvailable());
        ResearchRenderer.render(sender, snapshot, opts);
    }

    @Override
    public ObjectList<String> suggest(ICommandSender sender, ObjectList<String> args) {
        return switch (args.size()) {
            case 1 -> ObjectLists.singleton("research");
            case 2 -> ThaumFacade.available() ? ThaumFacade.chapterSuggestions() : ObjectLists.emptyList();
            case 3 -> playerNames();
            default -> ObjectLists.emptyList();
        };
    }

    private static ObjectList<String> playerNames() {
        ObjectList<String> names = new ObjectArrayList<>();
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.theWorld != null) {
            for (EntityPlayer player : minecraft.theWorld.playerEntities) {
                names.add(player.getCommandSenderName());
            }
        }
        return names;
    }
}
