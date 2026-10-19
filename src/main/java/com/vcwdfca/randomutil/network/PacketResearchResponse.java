package com.vcwdfca.randomutil.network;

import com.vcwdfca.randomutil.feature.tc.ResearchSnapshot;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;

public class PacketResearchResponse implements IMessage {

    public static final byte OK = 0;
    public static final byte DENIED = 1;
    public static final byte NO_DATA = 2;
    public static final byte DISABLED = 3;
    public static final byte NO_THAUMCRAFT = 4;
    public static final byte RATE_LIMITED = 5;

    private int id;
    private byte status;
    private ResearchSnapshot snapshot;

    @SuppressWarnings("unused")
    public PacketResearchResponse() {}

    public PacketResearchResponse(int id, byte status) {
        this(id, status, null);
    }

    public PacketResearchResponse(int id, byte status, ResearchSnapshot snapshot) {
        this.id = id;
        this.status = status;
        this.snapshot = snapshot;
    }

    public int id() {
        return id;
    }

    public byte status() {
        return status;
    }

    public ResearchSnapshot snapshot() {
        return snapshot;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readInt();
        status = buf.readByte();
        if (!buf.readBoolean()) {
            snapshot = null;
            return;
        }

        String target = ByteBufUtils.readUTF8String(buf);
        String chapter = ByteBufUtils.readUTF8String(buf);
        int total = buf.readInt();
        int done = buf.readInt();
        int missing = buf.readInt();
        int hiddenMissing = buf.readInt();

        int categoryCount = buf.readShort();
        ObjectList<ResearchSnapshot.CatStat> categories = new ObjectArrayList<>(categoryCount);
        for (int i = 0; i < categoryCount; i++) {
            String key = ByteBufUtils.readUTF8String(buf);
            String displayName = ByteBufUtils.readUTF8String(buf);
            int catTotal = buf.readInt();
            int catDone = buf.readInt();
            int catMissing = buf.readInt();
            int catHidden = buf.readInt();
            int available = buf.readInt();

            int entryCount = buf.readShort();
            ObjectList<ResearchSnapshot.Entry> entries = new ObjectArrayList<>(entryCount);
            for (int j = 0; j < entryCount; j++) {
                entries.add(new ResearchSnapshot.Entry(ByteBufUtils.readUTF8String(buf), buf.readInt()));
            }

            categories.add(
                new ResearchSnapshot.CatStat(
                    key,
                    displayName,
                    catTotal,
                    catDone,
                    catMissing,
                    catHidden,
                    available,
                    ObjectLists.unmodifiable(entries)));
        }

        snapshot = new ResearchSnapshot(
            target,
            chapter,
            total,
            done,
            missing,
            hiddenMissing,
            ObjectLists.unmodifiable(categories));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(id);
        buf.writeByte(status);
        if (snapshot == null) {
            buf.writeBoolean(false);
            return;
        }

        buf.writeBoolean(true);
        ByteBufUtils.writeUTF8String(buf, snapshot.target());
        ByteBufUtils.writeUTF8String(buf, snapshot.chapterKey());
        buf.writeInt(snapshot.total());
        buf.writeInt(snapshot.done());
        buf.writeInt(snapshot.missing());
        buf.writeInt(snapshot.hiddenMissing());

        buf.writeShort(
            snapshot.cats()
                .size());
        for (ResearchSnapshot.CatStat category : snapshot.cats()) {
            ByteBufUtils.writeUTF8String(buf, category.key());
            ByteBufUtils.writeUTF8String(buf, category.displayName() == null ? "" : category.displayName());
            buf.writeInt(category.total());
            buf.writeInt(category.done());
            buf.writeInt(category.missing());
            buf.writeInt(category.hiddenMissing());
            buf.writeInt(category.available());

            buf.writeShort(
                category.entries()
                    .size());
            for (ResearchSnapshot.Entry entry : category.entries()) {
                ByteBufUtils.writeUTF8String(buf, entry.key());
                buf.writeInt(entry.flags());
            }
        }
    }

    public static class Handler implements IMessageHandler<PacketResearchResponse, IMessage> {

        @Override
        public IMessage onMessage(PacketResearchResponse message, MessageContext context) {
            com.vcwdfca.randomutil.util.ClientTickQueue.post(() -> QueryClient.onResponse(message));
            return null;
        }
    }
}
