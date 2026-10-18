package com.vcwdfca.randomutil.feature.thaum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;

import com.vcwdfca.randomutil.RandomUtil;
import com.vcwdfca.randomutil.command.Chat;
import com.vcwdfca.randomutil.command.Opts;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

public final class ResearchRenderer {

    private ResearchRenderer() {}

    public static void render(ICommandSender target, ResearchSnapshot snapshot, Opts opts) {
        if (opts.raw()) {
            Chat.send(target, rawKeys(snapshot));
            return;
        }

        Chat.send(
            target,
            Chat.tr(
                "randomutil.thaum.research.header",
                snapshot.target(),
                ThaumFacade.chapterName(snapshot.chapterKey())));
        Chat.send(
            target,
            Chat.tr("randomutil.thaum.research.summary", snapshot.missing(), snapshot.total(), snapshot.done()));

        if (snapshot.hiddenMissing() > 0) {
            Chat.send(target, Chat.tr("randomutil.thaum.research.hidden", snapshot.hiddenMissing()));
        }

        if (opts.countOnly()) {
            if (opts.dump()) {
                dump(target, snapshot);
            }
            return;
        }

        if ("*".equals(snapshot.chapterKey())) {
            renderAll(target, snapshot, opts);
        } else {
            renderChapter(target, snapshot, opts);
        }

        if (opts.dump()) {
            dump(target, snapshot);
        }
    }

    private static void renderAll(ICommandSender target, ResearchSnapshot snapshot, Opts opts) {
        for (ResearchSnapshot.CatStat category : snapshot.cats()) {
            if (category.total() == 0) {
                continue;
            }

            Chat.send(
                target,
                Chat.tr(
                    "randomutil.thaum.research.category",
                    category.key(),
                    category.displayName(),
                    category.missing(),
                    category.total(),
                    category.available()));
        }

        ObjectList<ResearchSnapshot.Entry> available = new ObjectArrayList<>();
        for (ResearchSnapshot.CatStat category : snapshot.cats()) {
            for (ResearchSnapshot.Entry entry : category.entries()) {
                if (entry.has(ResearchSnapshot.AVAILABLE)) {
                    available.add(entry);
                }
            }
        }

        if (available.isEmpty()) {
            Chat.send(target, Chat.tr("randomutil.thaum.research.none"));
            return;
        }

        available.sort(Comparator.comparing(ResearchSnapshot.Entry::key));
        Chat.send(target, Chat.tr("randomutil.thaum.research.availableHeader", available.size()));
        page(target, available, opts);
    }

    private static void renderChapter(ICommandSender target, ResearchSnapshot snapshot, Opts opts) {
        ObjectList<ResearchSnapshot.Entry> entries = new ObjectArrayList<>();
        for (ResearchSnapshot.CatStat category : snapshot.cats()) {
            entries.addAll(category.entries());
        }

        if (entries.isEmpty()) {
            Chat.send(target, Chat.tr("randomutil.thaum.research.none"));
            return;
        }

        entries.sort(
            Comparator.comparingInt(ResearchRenderer::rank)
                .thenComparing(ResearchSnapshot.Entry::key));
        page(target, entries, opts);
    }

    private static void page(ICommandSender target, ObjectList<ResearchSnapshot.Entry> entries, Opts opts) {
        int pages = Math.max(1, (entries.size() + opts.limit() - 1) / opts.limit());
        int page = Math.min(opts.page(), pages);
        int from = (page - 1) * opts.limit();
        int to = Math.min(entries.size(), from + opts.limit());

        for (int i = from; i < to; i++) {
            printEntry(target, entries.get(i));
        }

        if (pages <= 1) {
            return;
        }

        if (page < pages) {
            Chat.send(target, Chat.tr("randomutil.thaum.research.page.more", page, pages, entries.size(), page + 1));
        } else {
            Chat.send(target, Chat.tr("randomutil.thaum.research.page.end", page, pages, entries.size()));
        }
    }

    private static void printEntry(ICommandSender target, ResearchSnapshot.Entry entry) {
        String name = ResearchNames.name(entry.key());
        String tags = tags(entry.flags());
        boolean available = entry.has(ResearchSnapshot.AVAILABLE);

        Chat.send(
            target,
            Chat.tr(
                available ? "randomutil.thaum.research.entry.available" : "randomutil.thaum.research.entry.locked",
                entry.key(),
                name,
                tags));
    }

    private static String tags(int flags) {
        StringBuilder tags = new StringBuilder();
        if ((flags & ResearchSnapshot.CLUE) != 0) {
            tags.append(Chat.tr("randomutil.thaum.research.tag.clue"));
        }
        if ((flags & ResearchSnapshot.HIDDEN) != 0) {
            tags.append(Chat.tr("randomutil.thaum.research.tag.hidden"));
        }
        if ((flags & ResearchSnapshot.LOST) != 0) {
            tags.append(Chat.tr("randomutil.thaum.research.tag.lost"));
        }
        if ((flags & ResearchSnapshot.CONCEALED) != 0) {
            tags.append(Chat.tr("randomutil.thaum.research.tag.concealed"));
        }
        if ((flags & ResearchSnapshot.SECONDARY) != 0) {
            tags.append(Chat.tr("randomutil.thaum.research.tag.secondary"));
        }
        if ((flags & ResearchSnapshot.SPECIAL) != 0) {
            tags.append(Chat.tr("randomutil.thaum.research.tag.special"));
        }
        if ((flags & ResearchSnapshot.STUB) != 0) {
            tags.append(Chat.tr("randomutil.thaum.research.tag.stub"));
        }
        return tags.toString();
    }

    private static int rank(ResearchSnapshot.Entry entry) {
        if (entry.has(ResearchSnapshot.AVAILABLE)) {
            return 0;
        }
        if (entry.has(ResearchSnapshot.CLUE)) {
            return 1;
        }
        return 2;
    }

    private static String rawKeys(ResearchSnapshot snapshot) {
        ObjectList<String> keys = new ObjectArrayList<>();
        for (ResearchSnapshot.CatStat category : snapshot.cats()) {
            for (ResearchSnapshot.Entry entry : category.entries()) {
                keys.add(entry.key());
            }
        }
        return String.join(" ", keys);
    }

    private static void dump(ICommandSender target, ResearchSnapshot snapshot) {
        File directory = new File(Minecraft.getMinecraft().mcDataDir, "logs");
        String chapter = "*".equals(snapshot.chapterKey()) ? "all" : snapshot.chapterKey();
        File file = new File(directory, "randomutil-" + snapshot.target() + "-" + chapter + ".txt");
        int written = 0;

        try (PrintWriter writer = new PrintWriter(
            new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            for (ResearchSnapshot.CatStat category : snapshot.cats()) {
                for (ResearchSnapshot.Entry entry : category.entries()) {
                    writer.println(entry.key() + "\t" + category.key() + "\t" + entry.flags());
                    written++;
                }
            }
        } catch (Exception e) {
            RandomUtil.LOG.error("Could not write research dump to " + file, e);
            Chat.error(target, Chat.tr("randomutil.thaum.research.dumpFailed", file.getAbsolutePath()));
            return;
        }

        Chat.send(target, Chat.tr("randomutil.thaum.research.dump", written, file.getAbsolutePath()));
    }
}
