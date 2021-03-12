package zenenwjaimes.vanillaishtweaks.mixin.EnhancedTrident;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import zenenwjaimes.vanillaishtweaks.client.StateManager;

@Mixin(TridentItem.class)
public class EnhancedTridentItem {
    @Redirect(method = "onStoppedUsing", require = -1, at = @At(remap = false, ordinal = -1, value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;setProperties(Lnet/minecraft/entity/Entity;FFFFF)V"))
    public void spawnEntity(TridentEntity tridentEntity, Entity user, float pitch, float yaw, float roll, float modifierZ, float modifierXYZ) {
        try {
            if (tridentEntity instanceof TridentEntity && EnchantmentHelper.getLoyalty(((MixinTridentEntity) tridentEntity).getTridentStack()) > 0) {
                StateManager.getInstance().setThrownFromSlot(((PlayerEntity) (Object) user).inventory.selectedSlot);
            }
        } catch (ClassCastException cce) {
            System.out.println(this.getClass().toString() + cce);
        }
    }
}
