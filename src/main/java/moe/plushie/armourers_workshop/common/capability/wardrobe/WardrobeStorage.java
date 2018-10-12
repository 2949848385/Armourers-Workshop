package moe.plushie.armourers_workshop.common.capability.wardrobe;

import moe.plushie.armourers_workshop.common.capability.wardrobe.ExtraColours.ExtraColourType;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.Constants.NBT;

public class WardrobeStorage implements IStorage<IWardrobeCapability> {

    private static final String TAG_EXTRA_COLOUR = "extra-colour-";
    private static final String TAG_ARMOUR_OVERRIDE = "armourOverride";
    /*
    private static final String TAG_SLOTS_UNLOCKED = "slotsUnlocked";
    private static final String TAG_SLOT_KEY = "slotKey";
    private static final String TAG_SLOT_VALUE = "slotValue";
    */
    
    @Override
    public NBTBase writeNBT(Capability<IWardrobeCapability> capability, IWardrobeCapability instance, EnumFacing side) {
        NBTTagCompound compound = new NBTTagCompound();
        for (int i = 0; i < ExtraColourType.values().length; i++) {
            ExtraColourType type = ExtraColourType.values()[i];
            compound.setInteger(TAG_EXTRA_COLOUR + type.toString().toLowerCase(), instance.getExtraColours().getColour(type));
        }
        for (int i = 0; i < 4; i++) {
            compound.setBoolean(TAG_ARMOUR_OVERRIDE + i, instance.getArmourOverride().get(i));
        }
        return compound;
    }

    @Override
    public void readNBT(Capability<IWardrobeCapability> capability, IWardrobeCapability instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound compound = (NBTTagCompound) nbt;
        for (int i = 0; i < ExtraColourType.values().length; i++) {
            ExtraColourType type = ExtraColourType.values()[i];
            if (compound.hasKey(TAG_EXTRA_COLOUR + type.toString().toLowerCase(), NBT.TAG_INT)) {
                instance.getExtraColours().setColour(type, compound.getInteger(TAG_EXTRA_COLOUR + type.toString().toLowerCase()));
            }
        }
        for (int i = 0; i < 4; i++) {
            instance.getArmourOverride().set(i, compound.getBoolean(TAG_ARMOUR_OVERRIDE + i));
        }
    }
}
