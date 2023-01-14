package zenenwjaimes.vanillaishtweaks.mixin.EnhancedBlocks;

import net.minecraft.block.dispenser.BlockPlacementDispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FallibleItemDispenserBehavior.class)
public interface MixinBlockPlacementDispenserBehavior {
    @Accessor("success")
    public void setSuccess(boolean success);
}
