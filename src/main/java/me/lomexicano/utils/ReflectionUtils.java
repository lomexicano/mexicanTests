package me.lomexicano.utils;

import com.mumfrey.liteloader.util.ObfuscationUtilities;
import net.minecraft.client.Minecraft;

import java.util.Arrays;

public class ReflectionUtils {

    // Require mcName (non-obfuscated), obfuscated name and forge name;
    // Forge one can be found here: https://github.com/KevyPorter/Minecraft-Forge-Utils/blob/master/fields.csv
    // It's used to pick the correct name of the field, to be used with reflection;
    public static String getFieldName(String mcName, String Notch, String Forge){
        if(ObfuscationUtilities.fmlIsPresent()) return Forge;
        boolean vanillaNames = Arrays.stream(Minecraft.getMinecraft().ingameGUI.getClass().getMethods()).map(m -> m.getName().equals("getBossOverlay")).findFirst().orElse(false);
        if(vanillaNames) return mcName;
        return Notch;
    }
}
