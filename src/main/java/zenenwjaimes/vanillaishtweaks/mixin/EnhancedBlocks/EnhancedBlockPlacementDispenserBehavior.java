package zenenwjaimes.vanillaishtweaks.mixin.EnhancedBlocks;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.BlockPlacementDispenserBehavior;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(BlockPlacementDispenserBehavior.class)
public class EnhancedBlockPlacementDispenserBehavior {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Inject(at = @At("HEAD"), method = "dispenseSilently", cancellable = true)
    protected void dispenseSilently(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        ((MixinBlockPlacementDispenserBehavior) this).setSuccess(false);
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            Direction direction = (Direction)pointer.getBlockState().get(DispenserBlock.FACING);
            BlockPos blockPos = pointer.getPos().offset(direction);
            Direction direction2;

            // Try facing forward
            if (pointer.getWorld().isSpaceEmpty(ShulkerEntity.calculateBoundingBox(direction, 0.0F, 0.5F).offset(blockPos).contract(1.0E-6))) {
                direction2 = direction;
            // Try facing down, this is for comparators, red stone, repeaters, slabs or anything not a full block
            } else if (pointer.getWorld().isSpaceEmpty(ShulkerEntity.calculateBoundingBox(Direction.DOWN, 0.0F, 0.5F).offset(blockPos).contract(1.0E-6))) {
                direction2 = Direction.DOWN;
            } else {
                direction2 = Direction.UP;
            }

            try {
                ((MixinBlockPlacementDispenserBehavior) this).setSuccess(((BlockItem)item).place(new AutomaticItemPlacementContext(pointer.getWorld(), blockPos, direction, stack, direction2)).isAccepted());
            } catch (Exception var8) {
                LOGGER.error("Error trying to place shulker box at {}", blockPos, var8);
            }
        }

        cir.setReturnValue(stack);
    }
}
