package me.lomexicano.module.events;

import me.lomexicano.ModuleInfo;
import me.lomexicano.base.BaseEventProvider;
import me.lomexicano.utils.ReflectionUtils;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacroEvent;
import net.eq2online.macros.scripting.api.IMacroEventDispatcher;
import net.eq2online.macros.scripting.api.IMacroEventManager;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;
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
    public static int titlesTimer;
    public static int lastTitlesTimer;
    public static int titleFadeIn;
    public static int titleDisplayTime;
    public static int titleFadeOut;
    public static String displayedTitle;
    public static String displayedSubTitle;
    public static String lastDisplayedTitle;
    public static String lastDisplayedSubTitle;

    private Field titlesTimerField;
    private Field displayedTitleField;
    private Field displayedSubTitleField;
    private Field titleFadeInField;
    private Field titleDisplayTimeField;
    private Field titleFadeOutField;

    public static boolean varsWereLoaded = false;

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

        if (!varsWereLoaded && Minecraft.getMinecraft().ingameGUI != null) {
            try {

                this.titlesTimerField = GuiIngame.class.getDeclaredField(ReflectionUtils.getFieldName("titlesTimer", "x", "field_175195_w"));
                this.displayedTitleField = GuiIngame.class.getDeclaredField(ReflectionUtils.getFieldName("displayedTitle", "y", "field_175201_x"));
                this.displayedSubTitleField = GuiIngame.class.getDeclaredField(ReflectionUtils.getFieldName("displayedSubTitle", "z", "field_175200_y"));
                this.titleFadeInField = GuiIngame.class.getDeclaredField(ReflectionUtils.getFieldName("titleFadeIn", "C", "field_175199_z"));
                this.titleDisplayTimeField = GuiIngame.class.getDeclaredField(ReflectionUtils.getFieldName("titleDisplayTime", "D", "field_175192_A"));
                this.titleFadeOutField = GuiIngame.class.getDeclaredField(ReflectionUtils.getFieldName("titleFadeOut", "E", "field_175193_B"));

                this.titlesTimerField.setAccessible(true);
                this.displayedTitleField.setAccessible(true);
                this.displayedSubTitleField.setAccessible(true);
                this.titleFadeInField.setAccessible(true);
                this.titleDisplayTimeField.setAccessible(true);
                this.titleFadeOutField.setAccessible(true);

                varsWereLoaded = true;

            } catch (Exception ignore) {
            }
        } else if (varsWereLoaded) {
            try {
                GuiIngame myGUI = Minecraft.getMinecraft().ingameGUI;
                titlesTimer = (int) this.titlesTimerField.get(myGUI);
                displayedTitle = (String) this.displayedTitleField.get(myGUI);
                displayedSubTitle = (String) this.displayedSubTitleField.get(myGUI);
                titleFadeIn = (int) this.titleFadeInField.get(myGUI);
                titleDisplayTime = (int) this.titleDisplayTimeField.get(myGUI);
                titleFadeOut = (int) this.titleFadeOutField.get(myGUI);

                if (((!displayedTitle.equals("")) || (!displayedSubTitle.equals(""))) &&
                        ((!displayedTitle.equals(lastDisplayedTitle) || (!displayedSubTitle.equals(lastDisplayedSubTitle))) ||
                                ((titlesTimer > lastTitlesTimer)))) {

                    lastDisplayedTitle = displayedTitle;
                    lastDisplayedSubTitle = displayedSubTitle;
                    manager.sendEvent(this.onTitle, displayedTitle, displayedSubTitle, String.valueOf(titleFadeIn), String.valueOf(titleDisplayTime), String.valueOf(titleFadeOut), String.valueOf(titlesTimer));
                }
                lastTitlesTimer = titlesTimer;
            } catch (Exception ignore) {
            }
        }
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
