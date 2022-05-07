package zenenwjaimes.vanillaishtweaks.mixin.EnhancedBlockBreaking;


import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.block.Blocks.class)
public class Blocks {
    @Inject(at = @At("HEAD"), method = "register", cancellable = true)
    private static void register(String id, Block block, CallbackInfoReturnable<Block> cir) {
        if (Registry.BLOCK == null) {
            cir.cancel();
            return;
        }
    }
}
