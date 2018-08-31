package riskyken.armourers_workshop.common.blocks;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import riskyken.armourers_workshop.common.lib.LibBlockNames;
import riskyken.armourers_workshop.common.lib.LibModInfo;
import riskyken.armourers_workshop.common.tileentities.TileEntityArmourer;
import riskyken.armourers_workshop.common.tileentities.TileEntityBoundingBox;
import riskyken.armourers_workshop.common.tileentities.TileEntityColourMixer;
import riskyken.armourers_workshop.common.tileentities.TileEntityColourable;
import riskyken.armourers_workshop.common.tileentities.TileEntityDyeTable;
import riskyken.armourers_workshop.common.tileentities.TileEntityGlobalSkinLibrary;
import riskyken.armourers_workshop.common.tileentities.TileEntityHologramProjector;
import riskyken.armourers_workshop.common.tileentities.TileEntityMannequin;
import riskyken.armourers_workshop.common.tileentities.TileEntitySkinLibrary;
import riskyken.armourers_workshop.common.tileentities.TileEntitySkinnable;
import riskyken.armourers_workshop.common.tileentities.TileEntitySkinnableChild;
import riskyken.armourers_workshop.common.tileentities.TileEntitySkinningTable;

public class ModBlocks {

    public static ArrayList<Block> BLOCKS = new ArrayList<Block>();
    
    public static Block armourerBrain;
    //public static Block miniArmourer;
    public static Block armourLibrary;
    public static Block globalSkinLibrary;
    public static Block boundingBox;
    public static Block colourable;
    public static Block colourableGlowing;
    public static Block colourableGlass;
    public static Block colourableGlassGlowing;
    public static Block colourMixer;
    public static Block mannequin;
    public static Block doll;
    public static Block skinningTable;
    public static Block skinnable;
    public static Block skinnableGlowing;
    public static Block skinnableChild;
    public static Block skinnableChildGlowing;
    public static Block dyeTable;
    public static Block hologramProjector;
    
    public ModBlocks() {
        MinecraftForge.EVENT_BUS.register(this);
        armourerBrain = new BlockArmourer();
        //miniArmourer = new BlockMiniArmourer();
        armourLibrary = new BlockSkinLibrary();
        globalSkinLibrary = new BlockGlobalSkinLibrary();
        boundingBox = new BlockBoundingBox();
        colourable = new BlockColourable(LibBlockNames.COLOURABLE, false);
        colourableGlowing = new BlockColourable(LibBlockNames.COLOURABLE_GLOWING, true);
        colourableGlass = new BlockColourableGlass(LibBlockNames.COLOURABLE_GLASS, false);
        colourableGlassGlowing = new BlockColourableGlass(LibBlockNames.COLOURABLE_GLASS_GLOWING, true);
        colourMixer = new BlockColourMixer();
        mannequin = new BlockMannequin();
        doll = new BlockDoll();
        skinningTable = new BlockSkinningTable();
        skinnable = new BlockSkinnable();
        skinnableGlowing = new BlockSkinnableGlowing();
        skinnableChild = new BlockSkinnableChild();
        skinnableChildGlowing = new BlockSkinnableChildGlowing();
        dyeTable = new BlockDyeTable();
        hologramProjector = new BlockHologramProjector();
    }
    

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> reg = event.getRegistry();
        for (int i = 0; i < BLOCKS.size(); i++) {
            reg.register(BLOCKS.get(i));
        }
    }
    
    @SubscribeEvent
    public void registerItemBlocks(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> reg = event.getRegistry();
        for (int i = 0; i < BLOCKS.size(); i++) {
            reg.register(new ItemBlock(BLOCKS.get(i)).setRegistryName(BLOCKS.get(i).getRegistryName()));
        }
    }

    public void registerTileEntities() {
        registerTileEntity(TileEntityArmourer.class, LibBlockNames.ARMOURER_BRAIN);
        //registerTileEntity(TileEntityMiniArmourer.class, LibBlockNames.MINI_ARMOURER);
        registerTileEntity(TileEntitySkinLibrary.class, LibBlockNames.ARMOUR_LIBRARY);
        registerTileEntity(TileEntityGlobalSkinLibrary.class, LibBlockNames.GLOBAL_SKIN_LIBRARY);
        registerTileEntity(TileEntityColourable.class, LibBlockNames.COLOURABLE);
        registerTileEntity(TileEntityColourMixer.class, LibBlockNames.COLOUR_MIXER);
        registerTileEntity(TileEntityBoundingBox.class, LibBlockNames.BOUNDING_BOX);
        registerTileEntity(TileEntityMannequin.class, LibBlockNames.MANNEQUIN);
        registerTileEntity(TileEntitySkinningTable.class, LibBlockNames.SKINNING_TABLE);
        registerTileEntity(TileEntitySkinnable.class, LibBlockNames.SKINNABLE);
        registerTileEntity(TileEntityDyeTable.class, LibBlockNames.DYE_TABLE);
        registerTileEntity(TileEntitySkinnableChild.class, LibBlockNames.SKINNABLE_CHILD);
        registerTileEntity(TileEntityHologramProjector.class, LibBlockNames.HOLOGRAM_PROJECTOR);
    }

    private void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String id) {
        GameRegistry.registerTileEntity(tileEntityClass, new ResourceLocation(LibModInfo.ID, "tileentity." + id));
    }
}
