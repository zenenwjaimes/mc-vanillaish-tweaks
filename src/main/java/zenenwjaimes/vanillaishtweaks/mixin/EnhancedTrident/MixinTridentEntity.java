package zenenwjaimes.vanillaishtweaks.mixin.EnhancedTrident;

import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TridentEntity.class)
public interface MixinTridentEntity {
    @Accessor("tridentStack")
    public ItemStack getTridentStack();
}
