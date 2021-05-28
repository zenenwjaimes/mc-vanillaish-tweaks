package zenenwjaimes.vanillaishtweaks.mixin.EnhancedHud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InGameHud.class)
public interface MixinInGameHud {
    @Accessor("client")
    public MinecraftClient getClient();
}
