package com.pouffydev.create_freezedown.foundation.climate;

import com.pouffydev.create_freezedown.Thermology;
import com.pouffydev.create_freezedown.foundation.CTPacketHandler;
import com.pouffydev.create_freezedown.foundation.util.ClientUtils;
import com.pouffydev.create_freezedown.foundation.util.GuiUtils;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Collection;
import java.util.function.Supplier;

import static com.pouffydev.create_freezedown.foundation.CTPacketHandler.getChannel;

public class CTTemperatureDisplayPacket {
    private final int[] temp;
    private final String langKey;
    private final boolean isStatus;
    private final boolean isAction;
    public CTTemperatureDisplayPacket(String format,int...data) {
        this.langKey=format;
        this.temp=data;
        for(int i=0;i<temp.length;i++)
            temp[i]*=10;
        isStatus=false;
        isAction=false;
    }
    public CTTemperatureDisplayPacket(String format,float...data) {
        this.langKey=format;
        temp=new int[data.length];
        for(int i=0;i<data.length;i++)
            temp[i]=(int) (data[i]*10);
        isStatus=false;
        isAction=false;
    }
    public CTTemperatureDisplayPacket(String format,boolean isAction,int...data) {
        this.langKey=format;
        this.temp=data;
        for(int i=0;i<temp.length;i++)
            temp[i]*=10;
        isStatus=true;
        this.isAction=isAction;
    }
    public CTTemperatureDisplayPacket(String format,boolean isAction,float...data) {
        this.langKey=format;
        temp=new int[data.length];
        for(int i=0;i<data.length;i++)
            temp[i]=(int) (data[i]*10);
        isStatus=true;
        this.isAction=isAction;
    }
    public CTTemperatureDisplayPacket(FriendlyByteBuf buffer) {
        langKey=buffer.readUtf();
        temp=buffer.readVarIntArray();
        boolean[] bs=SerializeUtil.readBooleans(buffer);
        isStatus=bs[0];
        isAction=bs[1];
    }
    
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(langKey);
        buffer.writeVarIntArray(temp);
        SerializeUtil.writeBooleans(buffer,isStatus,isAction);
    }
    
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientUtils::getPlayer);
            Object[] ss=new Object[temp.length];
            for(int i=0;i<ss.length;i++) {
                ss[i]= GuiUtils.toTemperatureIntString(temp[i]/10f);
            }
            Component tosend= Lang.translate("message." + Thermology.ID + "."+langKey,ss).component();
            if(isStatus)
                player.sendSystemMessage(tosend);
            else
                player.sendSystemMessage(tosend);
            
        });
        context.get().setPacketHandled(true);
    }
    public static void send(ServerPlayer pe, String format, int...temps) {
        getChannel().send(PacketDistributor.PLAYER.with(()->pe),new CTTemperatureDisplayPacket(format,temps));
    }
    public static void send(ServerPlayer pe,String format,float...temps) {
        getChannel().send(PacketDistributor.PLAYER.with(()->pe),new CTTemperatureDisplayPacket(format,temps));
    }
    public static void send(Collection<ServerPlayer> pe, String format, int...temps) {
        CTTemperatureDisplayPacket k=new CTTemperatureDisplayPacket(format,temps);
        for(ServerPlayer p:pe)
            getChannel().send(PacketDistributor.PLAYER.with(()->p),k);
    }
    public static void send(Collection<ServerPlayer> pe,String format,float...temps) {
        CTTemperatureDisplayPacket k=new CTTemperatureDisplayPacket(format,temps);
        for(ServerPlayer p:pe)
            getChannel().send(PacketDistributor.PLAYER.with(()->p),k);
    }
    public static void sendStatus(ServerPlayer pe,String format,boolean act,int...temps) {
        getChannel().send(PacketDistributor.PLAYER.with(()->pe),new CTTemperatureDisplayPacket(format,act,temps));
    }
    public static void sendStatus(ServerPlayer pe,String format,boolean act,float...temps) {
        getChannel().send(PacketDistributor.PLAYER.with(()->pe),new CTTemperatureDisplayPacket(format,act,temps));
    }
    public static void sendStatus(Collection<ServerPlayer> pe,String format,boolean act,int...temps) {
        CTTemperatureDisplayPacket k=new CTTemperatureDisplayPacket(format,act,temps);
        for(ServerPlayer p:pe)
            getChannel().send(PacketDistributor.PLAYER.with(()->p),k);
    }
    public static void sendStatus(Collection<ServerPlayer> pe,String format,boolean act,float...temps) {
        CTTemperatureDisplayPacket k=new CTTemperatureDisplayPacket(format,act,temps);
        for(ServerPlayer p:pe)
            getChannel().send(PacketDistributor.PLAYER.with(()->p),k);
    }
}
