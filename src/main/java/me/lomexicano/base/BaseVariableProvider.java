package me.lomexicano.base;

import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.variable.VariableCache;

public abstract class BaseVariableProvider extends VariableCache {

    /**
     * Internal method, I think this doesn't even have to return the exact thing which is being returned here,
     * but as the 'old' example module does it like this, I keep it, probably good practice.
     */
    @Override
    public Object getVariable(String variableName) {
        return this.getCachedValue(variableName);
    }

    /**
     * Register the VariableProvider in each available {@link ScriptContext}
     * By default, there are two available ScriptContext's: {@link ScriptContext#MAIN} and {@link ScriptContext#CHATFILTER}
     */
    @Override
    public void onInit() {
        for (ScriptContext context : ScriptContext.getAvailableContexts()) {
            context.getCore().registerVariableProvider(this);
        }
    }
}
