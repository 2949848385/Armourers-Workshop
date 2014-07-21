package riskyken.armorersWorkshop.common.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import riskyken.armorersWorkshop.common.lib.LibModInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTabArmorersWorkshop extends CreativeTabs {

	public CreativeTabArmorersWorkshop(int id,String label) {
		super(id,label);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
		return  Item.getItemFromBlock(Blocks.dirt); 
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel()
    {
        return LibModInfo.NAME;
    }
}
