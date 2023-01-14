package zenenwjaimes.vanillaishtweaks.mixin.EnhancedBeaconBlock;

import com.google.common.collect.Lists;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Mixin(BeaconBlockEntity.class)
public class EnhancedBeaconBlockEntity {
    @Shadow
    int level;
    @Shadow
    private int minY;
    @Shadow
    StatusEffect primary;
    @Shadow
    StatusEffect secondary;
    @Shadow
    private List<BeaconBlockEntity.BeamSegment> field_19178 = Lists.newArrayList();

    @Inject(at = @At(value = "HEAD"), method = "tick", cancellable = true)
    private static void tick(World world, BlockPos pos, BlockState state, BeaconBlockEntity blockEntity, CallbackInfo ci) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        BlockPos blockPos2 = new BlockPos(i, j + 1, k);
        BlockState blockState = world.getBlockState(blockPos2);
        Block block = blockState.getBlock();

        int l = world.getTopY(Heightmap.Type.WORLD_SURFACE, i, k);

        if (block instanceof TintedGlassBlock) {
            int n = ((EnhancedBeaconBlockEntity) (Object) blockEntity).level;

            if (world.getTime() % 80L == 0L) {
                ((EnhancedBeaconBlockEntity) (Object) blockEntity).level = updateLevel(world, i, j, k);

                if (((EnhancedBeaconBlockEntity) (Object) blockEntity).level > 0) {
                    applyPlayerEffects(world, pos, ((EnhancedBeaconBlockEntity) (Object) blockEntity).level, ((EnhancedBeaconBlockEntity) (Object) blockEntity).primary, ((EnhancedBeaconBlockEntity) (Object) blockEntity).secondary);
                    BeaconBlockEntity.playSound(world, pos, SoundEvents.BLOCK_BEACON_AMBIENT);
                    ((EnhancedBeaconBlockEntity) (Object) blockEntity).field_19178.clear();
                }
            }

            if (((EnhancedBeaconBlockEntity) (Object) blockEntity).minY >= l) {
                ((EnhancedBeaconBlockEntity) (Object) blockEntity).minY = world.getBottomY() - 1;
                boolean bl = n > 0;

                if (!world.isClient) {
                    boolean bl2 = ((EnhancedBeaconBlockEntity) (Object) blockEntity).level > 0;
                    if (!bl && bl2) {
                        BeaconBlockEntity.playSound(world, pos, SoundEvents.BLOCK_BEACON_ACTIVATE);
                        Iterator var17 = world.getNonSpectatingEntities(ServerPlayerEntity.class, (new Box((double) i, (double) j, (double) k, (double) i, (double) (j - 4), (double) k)).expand(10.0D, 5.0D, 10.0D)).iterator();

                        while (var17.hasNext()) {
                            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) var17.next();
                            Criteria.CONSTRUCT_BEACON.trigger(serverPlayerEntity, ((EnhancedBeaconBlockEntity) (Object) blockEntity).level);
                        }
                    } else if (bl && !bl2) {
                        BeaconBlockEntity.playSound(world, pos, SoundEvents.BLOCK_BEACON_DEACTIVATE);
                    }
                }
            }

            ci.cancel();
            return;
        }
    }

    @Shadow
    private static void applyPlayerEffects(World world, BlockPos pos, int beaconLevel, @Nullable StatusEffect primaryEffect, @Nullable StatusEffect secondaryEffect) {}

    @Shadow
    private static int updateLevel(World world, int x, int y, int z) {
        int i = 0;

        for(int j = 1; j <= 4; i = j++) {
            int k = y - j;
            if (k < world.getBottomY()) {
                break;
            }

            boolean bl = true;

            for(int l = x - j; l <= x + j && bl; ++l) {
                for(int m = z - j; m <= z + j; ++m) {
                    if (!world.getBlockState(new BlockPos(l, k, m)).isIn(BlockTags.BEACON_BASE_BLOCKS)) {
                        bl = false;
                        break;
                    }
                }
            }

            if (!bl) {
                break;
            }
        }

        return i;
    }
}
