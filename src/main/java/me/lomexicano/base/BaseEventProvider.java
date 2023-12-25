package me.lomexicano.base;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptContext;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BaseEventProvider implements IMacroEventProvider, IMacroEventVariableProvider {

    /* MacroEventDefinition is basically just the name of the event, and optionally, the permission group. */
    public IMacroEventDefinition getMacroEventDefinition() {
        return new IMacroEventDefinition() {
            @Override
            public String getName() {
                return getEventName();
            }

            @Override
            public String getPermissionGroup() {
                return null;
            }
        };
    }

    @Nonnull
    public abstract String getEventName();

    /* Used to setup instance variables, i.e. %CHAT%, %CHATCLEAN%, etc... */
    @Override
    public abstract void initInstance(String[] instanceVariables);

    @Override
    public abstract List<String> getHelp(IMacroEvent macroEvent);

    /* getVariable and getVariables methods are required if any instanceVariables should be able to be used in the event. */
    public Object getVariable(String variableName) {
        return getInstanceVarsMap().get(variableName);
    }

    public Set<String> getVariables() {
        return getInstanceVarsMap().keySet();
    }

    public abstract Map<String, Object> getInstanceVarsMap();

    /**
     * Register the EventProvider in each available {@link ScriptContext}
     * By default, there are two available ScriptContext's: {@link ScriptContext#MAIN} and {@link ScriptContext#CHATFILTER}
     */
    @Override
    public void onInit() {
        ScriptContext.getAvailableContexts().forEach(context -> context.getCore().registerEventProvider(this));
    }

    /* If the IMacroEventDispatcher interface is implemented, the method has to return 'this' instead of null. */
    @Override
    public IMacroEventDispatcher getDispatcher() {
        return null;
    }

    /**
     * @param clock Internal parameter to see whether variables should be updated or not.
     * This method has to be here due to the implemented interfaces, I never used this method in any event provider I created.
     * So just leave it here unless you have a use case for it.
     */
    @Override
    public void updateVariables(boolean clock) {}

}
