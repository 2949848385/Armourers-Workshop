package moe.plushie.armourers_workshop.common.command;

import moe.plushie.armourers_workshop.ArmourersWorkshop;
import moe.plushie.armourers_workshop.common.lib.LibGuiIds;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class CommandAdminPanel extends ModCommand {

    @Override
    public String getName() {
        return "adminPanel";
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        if (player == null) {
            return;
        }
        FMLNetworkHandler.openGui(player, ArmourersWorkshop.getInstance(), LibGuiIds.ADMIN_PANEL, player.getEntityWorld(), 0, 0, 0);
    }
}
