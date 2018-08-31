package riskyken.armourers_workshop.common.command;

import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import riskyken.armourers_workshop.common.skin.EquipmentWardrobeData;
import riskyken.armourers_workshop.common.skin.ExPropsPlayerSkinData;

public class CommandSetWardrobeOption extends ModCommand {

    private static final String[] SUB_OPTIONS = new String[] {"showHeadArmour", "showChestArmour", "showLegArmour", "showFootArmour", "showHeadOverlay"};
    
    @Override
    public String getName() {
        return "setWardrobeOption";
    }
    
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
        if (args.length == 2) {
            return getListOfStringsMatchingLastWord(args, getPlayers(server));
        }
        if (args.length == 3) {
            return getListOfStringsMatchingLastWord(args, SUB_OPTIONS);
        }
        if (args.length == 4) {
            return getListOfStringsMatchingLastWord(args, new String[] {"true", "false"});
        }
        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 4) {
            throw new WrongUsageException(getUsage(sender), (Object)args);
        }
        EntityPlayerMP player = getPlayer(server, sender, args[1]);
        if (player == null) {
            return;
        }
        
        String subOption = args[2];
        boolean value = parseBoolean(args[3]);
        int subOptionIndex = -1;
        for (int i = 0; i < SUB_OPTIONS.length; i++) {
            if (subOption.equals(SUB_OPTIONS[i])) {
                subOptionIndex = i;
                break;
            }
        }
        if (subOptionIndex == -1) {
            throw new WrongUsageException(getUsage(sender), (Object)args);
        }
        
        ExPropsPlayerSkinData playerEquipmentData = ExPropsPlayerSkinData.get(player);
        if (playerEquipmentData != null) {
            EquipmentWardrobeData ewd = playerEquipmentData.getEquipmentWardrobeData();
            if (subOptionIndex < 4) {
                ewd.armourOverride.set(subOptionIndex, !value);
            }
            if (subOptionIndex == 4) {
                ewd.headOverlay = !value;
            }
            playerEquipmentData.setSkinInfo(ewd, true);
        }
    }
}
