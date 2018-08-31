package riskyken.armourers_workshop.common.skin.type.feet;

import java.util.ArrayList;

import riskyken.armourers_workshop.api.common.skin.data.ISkinProperty;
import riskyken.armourers_workshop.api.common.skin.type.ISkinPartType;
import riskyken.armourers_workshop.common.skin.data.SkinProperties;
import riskyken.armourers_workshop.common.skin.type.AbstractSkinTypeBase;

public class SkinFeet extends AbstractSkinTypeBase {

    private ArrayList<ISkinPartType> skinParts;
    
    public SkinFeet() {
        skinParts = new ArrayList<ISkinPartType>();
        skinParts.add(new SkinFeetPartLeftFoot(this));
        skinParts.add(new SkinFeetPartRightFoot(this));
    }
    
    @Override
    public ArrayList<ISkinPartType> getSkinParts() {
        return this.skinParts;
    }

    @Override
    public String getRegistryName() {
        return "armourers:feet";
    }
    
    @Override
    public String getName() {
        return "Feet";
    }

    @Override
    public int getVanillaArmourSlotId() {
        return 3;
    }
    
    @Override
    public ArrayList<ISkinProperty<?>> getProperties() {
        ArrayList<ISkinProperty<?>> properties = super.getProperties();
        properties.add(SkinProperties.PROP_ARMOUR_OVERRIDE);
        return properties;
    }
}
