package moe.plushie.armourers_workshop.utils;

import java.util.ArrayList;
import java.util.Calendar;

import moe.plushie.armourers_workshop.common.capability.wardrobe.player.IPlayerWardrobeCap;
import moe.plushie.armourers_workshop.common.capability.wardrobe.player.PlayerWardrobeCap;
import moe.plushie.armourers_workshop.common.config.ConfigHandler;
import moe.plushie.armourers_workshop.common.items.ItemGiftSack;
import moe.plushie.armourers_workshop.common.items.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public final class HolidayHelper {
    
    private static ArrayList<Holiday> holidayList;
    
    // Spooky scary skeletons.
    public static final Holiday halloween = new Holiday(31, Calendar.OCTOBER, 0, 24);
    public static final Holiday halloween_season = new Holiday(24, Calendar.OCTOBER, 8, 0);
    
    // Some guy was born or something.
    public static final Holiday christmas = new Holiday(25, Calendar.DECEMBER, 0, 24);
    public static final Holiday christmas_season = new Holiday(1, Calendar.DECEMBER, 31, 0);
    
    // Forever alone.
    public static final Holiday valentines = new Holiday(14, Calendar.FEBRUARY, 1, 0);
    
    // year++
    public static final Holiday newYears = new Holiday(1, Calendar.JANUARY, 1, 0);
    
    // The best holiday!
    public static final Holiday ponytailDay = new Holiday(7, Calendar.JULY, 1, 0);
    
    // Should be 12 but making it 24 so more people can see it.
    public static final Holiday aprilFools = new Holiday(1, Calendar.APRIL, 1, 0);
    
    static {
        holidayList = new ArrayList<HolidayHelper.Holiday>();
        holidayList.add(halloween);
        holidayList.add(halloween_season);
        holidayList.add(christmas);
        holidayList.add(christmas_season);
        holidayList.add(valentines);
        holidayList.add(newYears);
        holidayList.add(ponytailDay);
        holidayList.add(aprilFools);
    }
    
    public static ArrayList<Holiday> getActiveHolidays() {
        ArrayList<Holiday> activeList = new ArrayList<HolidayHelper.Holiday>();
        for (int i = 0; i < holidayList.size(); i++) {
            if (holidayList.get(i).isHolidayActive()) {
                activeList.add(holidayList.get(i));
            }
        }
        return activeList;
    }
    
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
    
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        giftPlayer(event.player);
    }
    
    public static void giftPlayer(EntityPlayer player) {
        for (Holiday holiday : holidayList) {
            if (holiday.isHolidayActive()) {
                ItemStack giftSack = ((ItemGiftSack)ModItems.giftSack).createStackForHoliday(holiday);
                if (!giftSack.isEmpty()) {
                    if (!player.inventory.addItemStackToInventory(giftSack)) {
                        player.sendMessage(new TextComponentTranslation("chat.armourersworkshop:inventoryGiftFail"));
                    } else {
                        //playerData.lastXmasYear = getYear();
                    }
                    IPlayerWardrobeCap wardrobeCap = PlayerWardrobeCap.get(player);
                    
                    if (wardrobeCap != null) {
                        
                        /*
                        if (playerData.lastXmasYear < getYear()) {
                            ItemStack giftSack = new ItemStack(ModItems.giftSack, 1, 1000);

                        }
                        */
                    }
                }
            }
        }
    }
    
    public static class Holiday {
        
        private final Calendar startDate;
        private final Calendar endDate;
        
        /**
         * Creates and new holiday that spans x number of hours.
         * @param name Name of the holiday.
         * @param dayOfMonth The day of the month this holiday takes place on.
         * @param month Month this holiday takes place on. Zero based 0 = Jan, 11 = Dec
         * @param lengthInHours Number of hours this holiday lasts.
         */
        public Holiday(int dayOfMonth, int month, int lengthInDays, int lengthInHours) {
            startDate = Calendar.getInstance();
            startDate.set(Calendar.MINUTE, 0);
            startDate.set(Calendar.HOUR_OF_DAY, 0);
            startDate.set(Calendar.MONTH, month);
            startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            endDate = (Calendar) startDate.clone();
            endDate.add(Calendar.HOUR_OF_DAY, (lengthInDays * 24) + lengthInHours);
        }
        
        public boolean isHolidayActive() {
            Calendar current = Calendar.getInstance();
            if (ConfigHandler.enableHolidayEvents) {
                if (current.after(startDate) & current.before(endDate)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return "Holiday [startDate=" + startDate + ", endDate=" + endDate + "]";
        }
    }
}
