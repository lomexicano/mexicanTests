package me.lomexicano.module.events;

import me.lomexicano.ModuleInfo;
import me.lomexicano.base.BaseEventProvider;
import me.lomexicano.utils.ReflectionUtils;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacroEvent;
import net.eq2online.macros.scripting.api.IMacroEventDispatcher;
import net.eq2online.macros.scripting.api.IMacroEventManager;
import net.eq2online.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.*;

@APIVersion(ModuleInfo.API_VERSION)
public class EventProviderOnTitle extends BaseEventProvider implements IMacroEventDispatcher {
    private IMacroEvent onTitle;
    private final Map<String, Object> vars = new HashMap<>();
    private static final List<String> help;
    public static int titlesTimer = 0;
    public static int lastTitlesTimer = 0;
    public static int titleFadeIn = 0;
    public static int titleDisplayTime = 0;
    public static int titleFadeOut = 0;
    public static String displayedTitle = "";
    public static String displayedSubTitle = "";
    public static String lastDisplayedTitle = "";
    public static String lastDisplayedSubTitle = "";

    /* Executed once when the class is initialised. Used to create the help list. */
    static {
        List<String> helpList = new ArrayList<>();
        helpList.add(Util.convertAmpCodes("&f<onTitle>"));
        helpList.add(Util.convertAmpCodes("&6This event is triggered whenever a title is displayed."));
        helpList.add(Util.convertAmpCodes("&6Available variables are: "));
        helpList.add(Util.convertAmpCodes("  &cTITLE &6and &cSUBTITLE&6, retrieve information on the text."));
        helpList.add(Util.convertAmpCodes("  &cFADEIN&6, &cDISPLAYTIME&6, &cFADEOUT&6 and &cTOTALTIME&6, retrieve information on the time."));
        help = Collections.unmodifiableList(helpList);
    }

    @Override
    public @Nonnull String getEventName() {
        return "onTitle";
    }

    @Override
    public void registerEvents(IMacroEventManager manager) {
        this.onTitle = manager.registerEvent(this, getMacroEventDefinition());
        this.onTitle.setVariableProviderClass(getClass());
    }

    @Override
    public void initInstance(String[] instanceVariables) {
        vars.put("TITLE", instanceVariables[0]);
        vars.put("SUBTITLE", instanceVariables[1]);
        vars.put("FADEIN", instanceVariables[2]);
        vars.put("DISPLAYTIME", instanceVariables[3]);
        vars.put("FADEOUT", instanceVariables[4]);
        vars.put("TOTALTIME", instanceVariables[5]);
    }

    @Override
    public void onTick(IMacroEventManager manager, Minecraft minecraft) {

        try {
            GuiIngame myGUI = Minecraft.getMinecraft().ingameGUI;

            Field titlesTimerField = GuiIngame.class.getDeclaredField(ReflectionUtils.getFieldName("titlesTimer", "x", "field_175195_w"));
            Field displayedTitleField = GuiIngame.class.getDeclaredField(ReflectionUtils.getFieldName("displayedTitle", "y", "field_175201_x"));
            Field displayedSubTitleField = GuiIngame.class.getDeclaredField(ReflectionUtils.getFieldName("displayedSubTitle", "z", "field_175200_y"));
            Field titleFadeInField = GuiIngame.class.getDeclaredField(ReflectionUtils.getFieldName("titleFadeIn", "C", "field_175199_z"));
            Field titleDisplayTimeField = GuiIngame.class.getDeclaredField(ReflectionUtils.getFieldName("titleDisplayTime", "D", "field_175192_A"));
            Field titleFadeOutField = GuiIngame.class.getDeclaredField(ReflectionUtils.getFieldName("titleFadeOut", "E", "field_175193_B"));

            titlesTimerField.setAccessible(true);
            displayedTitleField.setAccessible(true);
            displayedSubTitleField.setAccessible(true);
            titleFadeInField.setAccessible(true);
            titleDisplayTimeField.setAccessible(true);
            titleFadeOutField.setAccessible(true);

            titlesTimer = (int) titlesTimerField.get(myGUI);
            displayedTitle = (String) displayedTitleField.get(myGUI);
            displayedSubTitle = (String) displayedSubTitleField.get(myGUI);
            titleFadeIn = (int) titleFadeInField.get(myGUI);
            titleDisplayTime = (int) titleDisplayTimeField.get(myGUI);
            titleFadeOut = (int) titleFadeOutField.get(myGUI);

        } catch (Exception ignore) {
            titlesTimer = 0;
            displayedTitle = "";
            displayedSubTitle = "";
        }

        if( ((!displayedTitle.equals(""))||(!displayedSubTitle.equals(""))) &&
                ((!displayedTitle.equals(lastDisplayedTitle)||(!displayedSubTitle.equals(lastDisplayedSubTitle))) ||
                        ((titlesTimer > lastTitlesTimer)))) {

            lastDisplayedTitle = displayedTitle;
            lastDisplayedSubTitle = displayedSubTitle;
            manager.sendEvent(this.onTitle, displayedTitle, displayedSubTitle, String.valueOf(titleFadeIn), String.valueOf(titleDisplayTime), String.valueOf(titleFadeOut), String.valueOf(titlesTimer));
        }
        lastTitlesTimer = titlesTimer;
    }

    @Override
    public IMacroEventDispatcher getDispatcher() {
        return this;
    }

    @Nonnull
    @Override
    public List<String> getHelp(IMacroEvent macroEvent) {
        return help;
    }

    @Override
    public Map<String, Object> getInstanceVarsMap() {
        return vars;
    }

    public EventProviderOnTitle() {}
    public EventProviderOnTitle(IMacroEvent e) {}

}
