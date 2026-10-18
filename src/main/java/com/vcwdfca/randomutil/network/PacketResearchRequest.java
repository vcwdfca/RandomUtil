package com.vcwdfca.randomutil.network;

import net.minecraft.entity.player.EntityPlayerMP;

import com.vcwdfca.randomutil.util.ServerTickQueue;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketResearchRequest implements IMessage {

    private int id;
    private String target;
    private String chapter;
    private boolean includeHidden;
    private boolean onlyAvailable;

    @SuppressWarnings("unused")
    public PacketResearchRequest() {}

    public PacketResearchRequest(int id, String target, String chapter, boolean includeHidden, boolean onlyAvailable) {
        this.id = id;
        this.target = target;
        this.chapter = chapter;
        this.includeHidden = includeHidden;
        this.onlyAvailable = onlyAvailable;
    }

    public int id() {
        return id;
    }

    public String target() {
        return target;
    }

    public String chapter() {
        return chapter;
    }

    public boolean includeHidden() {
        return includeHidden;
    }

    public boolean onlyAvailable() {
        return onlyAvailable;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readInt();
        target = ByteBufUtils.readUTF8String(buf);
        chapter = ByteBufUtils.readUTF8String(buf);
        int flags = buf.readUnsignedByte();
        includeHidden = (flags & 1) != 0;
        onlyAvailable = (flags & 2) != 0;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(id);
        ByteBufUtils.writeUTF8String(buf, target == null ? "" : target);
        ByteBufUtils.writeUTF8String(buf, chapter == null ? "*" : chapter);
        buf.writeByte((includeHidden ? 1 : 0) | (onlyAvailable ? 2 : 0));
    }

    public static class Handler implements IMessageHandler<PacketResearchRequest, IMessage> {

        @Override
        public IMessage onMessage(PacketResearchRequest message, MessageContext context) {
            EntityPlayerMP player = context.getServerHandler().playerEntity;
            ServerTickQueue.post(() -> QueryServer.handle(player, message));
            return null;
        }
    }
}
