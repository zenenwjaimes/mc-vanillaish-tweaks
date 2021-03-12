package zenenwjaimes.vanillaishtweaks.mixin.EnhancedTrident;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import zenenwjaimes.vanillaishtweaks.client.StateManager;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class EnhancedClientPlayNetworkHandler {
    @Redirect(method = "onEntitySpawn", require = -1, at = @At(remap = false, ordinal = 2, value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setOwner(Lnet/minecraft/entity/Entity;)V"))
    public void setOwner(PersistentProjectileEntity persistentProjectileEntity, Entity entity) {
        try {
            StateManager.getInstance().setThrownFromSlot(((ClientPlayerEntity) (Object) entity).inventory.selectedSlot);
        } catch (ClassCastException cce) {
            System.out.println(this.getClass().toString() + cce);
        }
    }
}
