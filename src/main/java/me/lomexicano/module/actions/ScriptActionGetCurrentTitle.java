package me.lomexicano.module.actions;

import me.lomexicano.ModuleInfo;
import me.lomexicano.base.BaseScriptAction;
import me.lomexicano.utils.ReflectionUtils;
import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import java.lang.reflect.Field;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionGetCurrentTitle extends BaseScriptAction {

    public ScriptActionGetCurrentTitle() {
        super("getCurrentTitle");
    }

    public static String syntax = "[&title =] getCurrentTitle([&title],[&subtitle],[#timefadein],[#timedisplay],[#timefadeout],[#timeramaining])";
    public static int titleTotalTime = 0;
    public static int titleFadeIn = 0;
    public static int titleDisplayTime = 0;
    public static int titleFadeOut = 0;
    public static String displayedTitle = "";
    public static String displayedSubTitle = "";

    @Override
    public boolean isThreadSafe() {
        return false;
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {

        try {
            GuiIngame myGUI = Minecraft.getMinecraft().ingameGUI;

            Field displayedTitleField = GuiIngame.class.getDeclaredField(ReflectionUtils.getFieldName("displayedTitle", "y", "field_175201_x"));
            Field displayedSubTitleField = GuiIngame.class.getDeclaredField(ReflectionUtils.getFieldName("displayedSubTitle", "z", "field_175200_y"));
            Field titleFadeInField = GuiIngame.class.getDeclaredField(ReflectionUtils.getFieldName("titleFadeIn", "C", "field_175199_z"));
            Field titleDisplayTimeField = GuiIngame.class.getDeclaredField(ReflectionUtils.getFieldName("titleDisplayTime", "D", "field_175192_A"));
            Field titleFadeOutField = GuiIngame.class.getDeclaredField(ReflectionUtils.getFieldName("titleFadeOut", "E", "field_175193_B"));
            Field titlesTimerField = GuiIngame.class.getDeclaredField(ReflectionUtils.getFieldName("titlesTimer", "x", "field_175195_w"));

            displayedTitleField.setAccessible(true);
            displayedSubTitleField.setAccessible(true);
            titleFadeInField.setAccessible(true);
            titleDisplayTimeField.setAccessible(true);
            titleFadeOutField.setAccessible(true);
            titlesTimerField.setAccessible(true);

            displayedTitle = (String) displayedTitleField.get(myGUI);
            displayedSubTitle = (String) displayedSubTitleField.get(myGUI);
            titleFadeIn = (int) titleFadeInField.get(myGUI);
            titleDisplayTime = (int) titleDisplayTimeField.get(myGUI);
            titleFadeOut = (int) titleFadeOutField.get(myGUI);
            titleTotalTime = (int) titlesTimerField.get(myGUI);

        } catch (Exception ignore) {
        }

        String firstParam = "";
        String secondParam = "";
        String thirdParam = "";
        String fourthParam = "";
        String fifthParam = "";
        String sixthParam = "";

        try {
            firstParam = ScriptCore.parseVars(provider, macro, params[0], false);
            secondParam = ScriptCore.parseVars(provider, macro, params[1], false);
            thirdParam = ScriptCore.parseVars(provider, macro, params[2], false);
            fourthParam = ScriptCore.parseVars(provider, macro, params[3], false);
            fifthParam = ScriptCore.parseVars(provider, macro, params[4], false);
            sixthParam = ScriptCore.parseVars(provider, macro, params[5], false);
        } catch (Exception ignore) {
        }

        if (Variable.isValidVariableName(firstParam)) {
            ScriptCore.setVariable(provider, macro, firstParam, displayedTitle);
        }
        if (Variable.isValidVariableName(secondParam)) {
            ScriptCore.setVariable(provider, macro, secondParam, displayedSubTitle);
        }
        if (Variable.isValidVariableName(thirdParam)) {
            ScriptCore.setVariable(provider, macro, thirdParam, titleFadeIn);
        }
        if (Variable.isValidVariableName(fourthParam)) {
            ScriptCore.setVariable(provider, macro, fourthParam, titleDisplayTime);
        }
        if (Variable.isValidVariableName(fifthParam)) {
            ScriptCore.setVariable(provider, macro, fifthParam, titleFadeOut);
        }
        if (Variable.isValidVariableName(sixthParam)) {
            ScriptCore.setVariable(provider, macro, sixthParam, titleTotalTime);
        }

        return new ReturnValue(displayedTitle);
    }
}
