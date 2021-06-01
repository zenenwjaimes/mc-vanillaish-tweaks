package zenenwjaimes.vanillaishtweaks.mixin.EnhancedTrident;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zenenwjaimes.vanillaishtweaks.client.StateManager;

@Environment(EnvType.CLIENT)
@Mixin(TridentEntity.class)
public class EnhancedTridentEntity {
    @Shadow
    private ItemStack tridentStack;

    @Inject(method = "onPlayerCollision", at = @At(value = "HEAD"), cancellable = true)
    public void onPlayerCollision(PlayerEntity player, CallbackInfo ci) {
        try {
            Entity entity = ((TridentEntity) (Object) this).getOwner();
            if (entity == null || entity.getUuid() == player.getUuid()) {
                TridentEntity te = (TridentEntity) (Object) this;
                boolean isInGround = ((MixinPersistentProjectileEntity) (Object) this).getInGround();
                int slot = EnchantmentHelper.getLoyalty(tridentStack) > 0 ? StateManager.getInstance().getThrownFromSlot() : -1;

                // Avoid deleting item stacks
                if (slot != -1 && !player.getInventory().main.get(slot).isEmpty()) {
                    slot = -1;
                }

                if (!te.world.isClient && (isInGround || te.isNoClip()) && te.shake <= 0) {
                    boolean bl = te.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED || te.pickupType == PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY && player.getAbilities().creativeMode || te.isNoClip() && te.getOwner().getUuid() == player.getUuid();
                    if (te.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED && !player.getInventory().insertStack(slot, tridentStack.copy())) {
                        bl = false;
                    }

                    if (bl) {
                        player.sendPickup(te, 1);
                        te.remove(Entity.RemovalReason.DISCARDED);
                    }
                }
            }

            ci.cancel();
        } catch (ClassCastException cce) {
            System.out.println(this.getClass().toString() + cce);
        }
    }
}