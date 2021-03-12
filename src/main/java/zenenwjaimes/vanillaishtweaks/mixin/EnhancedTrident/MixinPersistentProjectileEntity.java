package zenenwjaimes.vanillaishtweaks.mixin.EnhancedTrident;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PersistentProjectileEntity.class)
public interface MixinPersistentProjectileEntity {
    @Accessor("inGround")
    public boolean getInGround();
}
