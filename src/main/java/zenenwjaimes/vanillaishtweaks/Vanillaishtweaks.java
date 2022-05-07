package zenenwjaimes.vanillaishtweaks;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Vanillaishtweaks implements ModInitializer {
    public static String MOD_ID = "vanillaishtweaks";
    public static String MOD_NAME = "Vanillaishtweaks";

    @Override
    public void onInitialize() {
        System.out.println("pickling!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(Registry.BLOCK.getKeys());
        Block boneBlock = Registry.BLOCK.get(Identifier.tryParse("minecraft:bone_block"));
        System.out.println(boneBlock);
    }

    public String getModId() {
        return Vanillaishtweaks.MOD_ID;
    }
}
