package zenenwjaimes.vanillaishtweaks.mixin.EnhancedBlockBreaking;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(net.minecraft.item.ShearsItem.class)
public class ShearsItem {
    @Inject(at = @At("RETURN"), method = "isSuitableFor", cancellable = true)
    public void isSuitableFor(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getMaterial() == Material.GLASS) {
            cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("RETURN"), method = "postMine", cancellable = true)
    public void postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable<Boolean> cir) {
        if (state.getMaterial() == Material.GLASS) {
            cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("HEAD"), method = "getMiningSpeedMultiplier", cancellable = true)
    public void getMiningSpeedMultiplier(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
        if (state.isIn(BlockTags.WOOL) || state.getMaterial() == Material.GLASS) {
            cir.setReturnValue(5.0F);
        }
    }
}