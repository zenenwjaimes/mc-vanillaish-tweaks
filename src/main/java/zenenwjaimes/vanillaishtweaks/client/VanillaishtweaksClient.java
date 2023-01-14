package zenenwjaimes.vanillaishtweaks.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import zenenwjaimes.vanillaishtweaks.Vanillaishtweaks;

@Environment(EnvType.CLIENT)
public class VanillaishtweaksClient implements ClientModInitializer {
    public static String MOD_ID = "vanillaishtweaks";
    public static String MOD_NAME = "Vanillaishtweaks";

    @Override
    public void onInitializeClient() {

    }

    public String getModId() {
        return Vanillaishtweaks.MOD_ID;
    }

}
