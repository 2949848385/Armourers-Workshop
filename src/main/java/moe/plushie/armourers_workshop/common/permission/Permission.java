package moe.plushie.armourers_workshop.common.permission;

import moe.plushie.armourers_workshop.common.lib.LibModInfo;
import net.minecraftforge.server.permission.DefaultPermissionLevel;

public class Permission {
    
    public final String node;
    public final DefaultPermissionLevel level;
    public final String description;
    
    public Permission(String node, DefaultPermissionLevel level, String description) {
        this.node = node;
        this.level = level;
        this.description = description;
    }
    
    public Permission(String node, DefaultPermissionLevel level) {
        this(LibModInfo.ID + "." + node, level, "");
    }

    @Override
    public String toString() {
        return "Permission [node=" + node + ", level=" + level + ", description=" + description + "]";
    }
}
