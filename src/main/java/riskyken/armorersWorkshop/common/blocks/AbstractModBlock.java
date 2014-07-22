package riskyken.armorersWorkshop.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import riskyken.armorersWorkshop.ArmorersWorkshop;
import riskyken.armorersWorkshop.common.lib.LibModInfo;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class AbstractModBlock extends Block {

	public AbstractModBlock(String name) {
		super(Material.iron);
		setCreativeTab(ArmorersWorkshop.tabArmorersWorkshop);
		setHardness(3.0F);
		setStepSound(soundTypeMetal);
		setBlockName(name);
	}
	
	@Override
	public Block setBlockName(String name) {
		GameRegistry.registerBlock(this, "block." + name);
		return super.setBlockName(name);
	}
	
	public AbstractModBlock(String name, Material material, SoundType soundType) {
		super(material);
		setCreativeTab(ArmorersWorkshop.tabArmorersWorkshop);
		setHardness(3.0F);
		setStepSound(soundType);
		setBlockName(name);
	}
	
	@Override
	public String getUnlocalizedName() {
		return getModdedUnlocalizedName(super.getUnlocalizedName());
	}
	
	protected String getModdedUnlocalizedName(String unlocalizedName) {
		String name = unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
		return "tile." + LibModInfo.ID.toLowerCase() + ":" + name;
	}
}
