package zenenwjaimes.vanillaishtweaks.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class EnhancedSkyProperties {
    @Redirect(
            method = "renderClouds(Lnet/minecraft/client/util/math/MatrixStack;FDDD)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/SkyProperties;getCloudsHeight()F"
            )
    )
    private float getCloudsHeight(SkyProperties skyProperties) {
        if (skyProperties.getCloudsHeight() == 128.F) {
            return 256.0F;
        }

        return skyProperties.getCloudsHeight();
    }
}
