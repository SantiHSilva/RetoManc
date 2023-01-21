package carrot.mc.mancchallenge.Utils;

import carrot.mc.mancchallenge.Data.PersistentData;
import carrot.mc.mancchallenge.Discord.DiscordBot;
import carrot.mc.mancchallenge.Main;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static carrot.mc.mancchallenge.Data.PersistentData.INT;
import static carrot.mc.mancchallenge.Data.PersistentData.STRING;
import static carrot.mc.mancchallenge.Discord.DiscordBot.sendCompleteReto;
import static carrot.mc.mancchallenge.Utils.Day.getDay;
import static carrot.mc.mancchallenge.Utils.RetoUtils.regen1Cora;

public class PlayerData {

    public static void set(Player target, String id, String value){
        PersistentData.set(target, id, STRING, value);
    }

    public static String get(Player target, String id, String defaultValue){
        if(PersistentData.get(target, id, STRING) == null){
            set(target, id, defaultValue);
        }
        return PersistentData.get(target, id, STRING);
    }

    public static int getNotchUse(Player target){
        if(PersistentData.has(target, "notchUse", INT)) return PersistentData.get(target, "notchUse", INT);
        else return 0;
    }

    public static void setNotchUse(Player target, int value){
        PersistentData.set(target, "notchUse", INT, value);
    }

    public static int getUseTotem(Player target){
        if(PersistentData.has(target, "totem", INT)) return PersistentData.get(target, "totem", INT);
        else return 0;
    }

    public static int getDamage(Player target){
        if(PersistentData.has(target, "damage", INT)) return PersistentData.get(target, "damage", INT);
        else return 0;
    }

    public static void addDamage(Player target, int damage){
        PersistentData.set(target, "damage", INT, getDamage(target) + damage);
    }

    public static void setTotem(Player target, int value){
        PersistentData.set(target, "totem", INT, value);
    }

    public static int getRegean(Player target){
        if(PersistentData.has(target, "regean", INT)) return PersistentData.get(target, "regean", INT);
        else return 0;
    }

    public static boolean canRegean(Player target){
        return getRegean(target) > 0;
    }

    public static void setRegean(Player target, int seconds){
        PersistentData.set(target, "regean", INT, seconds);
        new BukkitRunnable(){
            @Override
            public void run() {
                if(canRegean(target))
                    setRegean(target, getRegean(target) - 1);
                else cancel();
            }
        }.runTaskLater(Main.getPlugin(), 20L);
    }

    public static void setDeath(Player target){
        set(target, "death", "true");
        target.setHealth(0);
        DiscordBot.sendNotCompleteReto(target);
    }

    public static boolean isDeath(Player target){
        return Boolean.parseBoolean(get(target, "death", "false"));
    }

    public static void updateDay(Player target, int day){
        int newDay = day - 1;
        target.sendMessage("dia: " + day);
        target.sendMessage("actualizando dia sobrevivido: " + newDay);
        set(target, "daySurvived", String.valueOf(newDay));
    }

    public static int getDaySurvived(Player target){
        return Integer.parseInt(get(target, "daySurvived", String.valueOf(getDay() - 1)));
    }

    public static void completeReto(Player target, int reto){
        if(isComplete(target, reto)) return;
        set(target, "reto" + reto, "true");
        Chat.broadCast("&a" + target.getName() + " ha completado el reto " + reto + "!");
        sendCompleteReto(target);
        regen1Cora(target);
    }

    public static boolean isComplete(Player target, int reto){
        return Boolean.parseBoolean(get(target, "reto" + reto, "false"));
    }

    public static int getCountMobs(Player target, int reto, String mob){
        return Integer.parseInt(get(target, reto + "mobs" + mob, "0"));
    }

    public static void setCountMobs(Player target, int reto, String mob, int amount){
        int contador = getCountMobs(target, reto, mob) + amount;
        target.sendMessage(mob + "-killed: " + contador);
        set(target, reto + "mobs" + mob, String.valueOf(contador));
    }

}
