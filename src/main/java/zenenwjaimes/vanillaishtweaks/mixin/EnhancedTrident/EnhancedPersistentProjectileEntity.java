package zenenwjaimes.vanillaishtweaks.mixin.EnhancedTrident;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zenenwjaimes.vanillaishtweaks.client.StateManager;

@Mixin(PersistentProjectileEntity.class)
public class EnhancedPersistentProjectileEntity {
    @Inject(method = "setOwner", at = @At(value = "HEAD"), cancellable = true)
    public void setOwner(Entity entity, CallbackInfo ci) {
        try {
            if (entity instanceof ClientPlayerEntity) {
                StateManager.getInstance().setThrownFromSlot(((ClientPlayerEntity) (Object) entity).inventory.selectedSlot);
            }
        } catch (ClassCastException cce) {
            System.out.println(this.getClass().toString() + cce);
        }
    }
}
