package zenenwjaimes.vanillaishtweaks.mixin;

import net.minecraft.client.render.SkyProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SkyProperties.class)
public interface MixinSkyProperties {
    @Accessor("cloudsHeight")
    public float getCloudsHeight();
}
