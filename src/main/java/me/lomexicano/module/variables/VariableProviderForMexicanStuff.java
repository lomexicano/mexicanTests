package me.lomexicano.module.variables;

import me.lomexicano.ModuleInfo;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.variable.VariableCache;
import net.minecraft.client.Minecraft;

@APIVersion(ModuleInfo.API_VERSION)
public class VariableProviderForMexicanStuff extends VariableCache {

    @Override
    public void updateVariables(boolean clock) {
        if (!clock) {
            return;
        }


        float yaw = Minecraft.getMinecraft().player.rotationYaw % 360.0F;
        while (yaw < 0)
            yaw += 360.0;
        String yawF = String.format("%.2f", yaw);
        String cardinalYawF = yaw > 180.0 ? String.format("%.2f", yaw - 180.0F) : String.format("%.2f", yaw + 180.0F);

        float pitch = Minecraft.getMinecraft().player.rotationPitch % 360.0F;
        while (pitch < 0) {
            pitch += 360;
        }
        String pitchF = String.format("%.2f", pitch);

        this.storeVariable("YAWF", yawF);
        this.storeVariable("CARDINALYAWF", cardinalYawF);
        this.storeVariable("PITCHF", pitchF);
    }

    @Override
    public Object getVariable(String variableName) {
        return this.getCachedValue(variableName);
    }

    @Override
    public void onInit() {
        ScriptContext.MAIN.getCore().registerVariableProvider(this);
    }

}