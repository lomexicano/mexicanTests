package me.lomexicano.base;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

public abstract class BaseScriptAction extends ScriptAction {

    /**
     * Simplified constructor which doesn't require a ScriptContext as parameter anymore,
     * as we register the ScriptAction for any available ScriptContext's anyways: {@link BaseScriptAction#onInit()}
     * @param actionName The name of the ScriptAction which is being created.
     */
    public BaseScriptAction(String actionName) {
        super(ScriptContext.MAIN, actionName);
    }

    /**
     * Register the ScriptAction in each available {@link ScriptContext}
     * By default, there are two available ScriptContext's: {@link ScriptContext#MAIN} and {@link ScriptContext#CHATFILTER}
     */
    @Override
    public void onInit() {
        for (ScriptContext scriptContext : ScriptContext.getAvailableContexts()) {
            scriptContext.getCore().registerScriptAction(this);
        }
    }

    /* This abstract method is not required at all, but it allows an easy generation of this method in all subclasses, at least while using IntelliJ IDEA */
    public abstract IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params);
}