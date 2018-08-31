package riskyken.armourers_workshop.common.network.messages.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import riskyken.armourers_workshop.common.data.TextureType;
import riskyken.armourers_workshop.common.inventory.ContainerMannequin;
import riskyken.armourers_workshop.common.tileentities.TileEntityMannequin;

public class MessageClientGuiMannequinData implements IMessage, IMessageHandler<MessageClientGuiMannequinData, IMessage> {

    private float xOffset;
    private float yOffset;
    private float zOffset;
    private int skinColour;
    private int hairColour;
    private String username;
    private boolean renderExtras;
    private boolean flying;
    private boolean visible;
    private TextureType textureType;
    
    public MessageClientGuiMannequinData() {
    }
    
    public MessageClientGuiMannequinData(float xOffset, float yOffset,
            float zOffset, int skinColour, int hairColour, String username,
            boolean renderExtras, boolean flying, boolean visible, TextureType textureType) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.skinColour = skinColour;
        this.hairColour = hairColour;
        this.username = username;
        this.renderExtras = renderExtras;
        this.flying = flying;
        this.visible = visible;
        this.textureType = textureType;
    }


    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(xOffset);
        buf.writeFloat(yOffset);
        buf.writeFloat(zOffset);
        buf.writeInt(skinColour);
        buf.writeInt(hairColour);
        ByteBufUtils.writeUTF8String(buf, username);
        buf.writeBoolean(renderExtras);
        buf.writeBoolean(flying);
        buf.writeBoolean(visible);
        buf.writeByte(textureType.ordinal());
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        xOffset = buf.readFloat();
        yOffset = buf.readFloat();
        zOffset = buf.readFloat();
        skinColour = buf.readInt();
        hairColour = buf.readInt();
        username = ByteBufUtils.readUTF8String(buf);
        renderExtras = buf.readBoolean();
        flying = buf.readBoolean();
        visible = buf.readBoolean();
        textureType = TextureType.values()[buf.readByte()];
    }

    @Override
    public IMessage onMessage(MessageClientGuiMannequinData message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        if (player == null) {
            return null;
        }
        Container container = player.openContainer;
        if (container != null && container instanceof ContainerMannequin) {
            TileEntityMannequin tileEntity = ((ContainerMannequin)container).getTileEntity();
            tileEntity.gotUpdateFromClient(message.xOffset, message.yOffset, message.zOffset,
                    message.skinColour, message.hairColour, message.username, message.renderExtras, message.flying, message.visible, message.textureType);
        }
        return null;
    }
}
