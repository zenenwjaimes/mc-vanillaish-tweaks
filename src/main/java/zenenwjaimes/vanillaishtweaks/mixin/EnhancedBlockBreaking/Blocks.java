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
        System.out.println(block);
        System.out.println(id);
        /**
        switch (id) {
            case "bone_block":
                Block boneBlock = new PillarBlock(AbstractBlock.Settings.of(Material.STONE, Material.STONE.getColor()).requiresTool().strength(1.5F, 2.0F).sounds(BlockSoundGroup.BONE));
                cir.setReturnValue((Block) Registry.register(Registry.BLOCK, (String)id, boneBlock));
                break;
            case "glass":
                Block glassBlock = new GlassBlock(
                        AbstractBlock.Settings
                                .of(Material.GLASS)
                                .requiresTool()
                                .strength(0.3F)
                                .sounds(BlockSoundGroup.GLASS)
                                .nonOpaque()
                                .allowsSpawning((state, world, pos, type) -> false)
                                .solidBlock((state, world, pos) -> false)
                                .suffocates((state, world, pos) -> false)
                                .blockVision((state, world, pos) -> false)
                );
                cir.setReturnValue((Block) Registry.register(Registry.BLOCK, (String)id, glassBlock));
                break;
            case "smooth_sandstone":
                cir.setReturnValue((Block) Registry.register(Registry.BLOCK, (String)id, new Block(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.SANDSTONE))));
                break;
            case "smooth_quartz":
                cir.setReturnValue((Block) Registry.register(Registry.BLOCK, (String)id, new Block(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.QUARTZ_BLOCK))));
                break;
        }
         **/
    }
}
