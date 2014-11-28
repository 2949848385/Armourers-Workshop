package riskyken.armourersWorkshop.utils;

import java.util.Calendar;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import riskyken.armourersWorkshop.common.equipment.ExtendedPropsPlayerEquipmentData;
import riskyken.armourersWorkshop.common.items.ModItems;

public final class HolidayHelper {
    
    public enum EnumHoliday {
        NONE(0,0),
        HALLOWEEN(Calendar.OCTOBER, 31),
        CHRISTMAS(Calendar.DECEMBER, 25);
        
        public final int month;
        public final int day;
        
        private EnumHoliday(int month, int day) {
            this.month = month;
            this.day = day;
        }
    }
    
    public static EnumHoliday getHoliday() {
        return getHoliday(3);
    }
    
    public static EnumHoliday getHoliday(int range) {
        Calendar c = Calendar.getInstance();
        
        for (int i = 1; i < EnumHoliday.values().length; i++) {
            EnumHoliday holiday = EnumHoliday.values()[i];
            c.set(Calendar.MONTH, holiday.month);
            c.set(Calendar.DAY_OF_MONTH, holiday.day);
            
            Calendar cUpperRange = (Calendar)c.clone();
            Calendar cLowerRange = (Calendar)c.clone();
            
            cUpperRange.add(Calendar.DAY_OF_MONTH, range);
            cUpperRange.add(Calendar.DAY_OF_MONTH, -range);
            
            if (c.after(cLowerRange) && c.before(cLowerRange)) {
                return holiday;
            }
        }
        
        return EnumHoliday.NONE;
    }
    
    
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
    
    public static void giftPlayer(EntityPlayerMP player) {
        if (getHoliday() == EnumHoliday.CHRISTMAS) {
            ExtendedPropsPlayerEquipmentData playerData = ExtendedPropsPlayerEquipmentData.get(player);
            if (playerData.lastXmasYear < getYear()) {
                Random rnd = new Random();
                ItemStack giftSack = new ItemStack(ModItems.equipmentSkinTemplate, 1, 1000);
                if (!player.inventory.addItemStackToInventory(giftSack)) {
                    player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("chat.armourersworkshop:inventoryGiftFail")));
                } else {
                    playerData.lastXmasYear = getYear();
                }
            }
        } 
    }
}
