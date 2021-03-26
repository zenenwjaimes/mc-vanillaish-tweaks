package zenenwjaimes.vanillaishtweaks.mixin.EnhancedCompass;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.CompassItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class EnhancedInGameHud {
    @Inject(method = "render", require = -1, at = @At(value = "TAIL"))
    public void renderItem(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        MixinInGameHud hud = (MixinInGameHud) (Object) this;
        ClientPlayerEntity player = hud.getClient().player;
        ItemStack offhand = player.getOffHandStack();
        ItemStack mainhand = player.getMainHandStack();

        if ((!mainhand.isEmpty() && mainhand.getItem() == Items.COMPASS && CompassItem.hasLodestone(mainhand)) || (!offhand.isEmpty() && offhand.getItem() == Items.COMPASS && CompassItem.hasLodestone(offhand))) {
            ItemStack stack = CompassItem.hasLodestone(mainhand) ? mainhand : offhand;
            CompoundTag compoundTag = stack.getOrCreateTag();

            if (compoundTag.contains("LodestoneTracked") && !compoundTag.getBoolean("LodestoneTracked")) {
                return;
            }
            Optional<RegistryKey<World>> optional = CompassItem.getLodestoneDimension(compoundTag);

            if (optional.isPresent() && compoundTag.contains("LodestonePos")) {
                BlockPos pos = NbtHelper.toBlockPos(compoundTag.getCompound("LodestonePos"));

                if (pos.getX() == 0 && pos.getZ() == 0) {
                    this.renderPlayerPosition(matrices, hud.getClient().player.getBlockPos().getX(), hud.getClient().player.getBlockPos().getY(), hud.getClient().player.getBlockPos().getZ());
                }
            }
        }
    }

    private void renderPlayerPosition(MatrixStack matrices, int x, int y, int z) {
        InGameHud oldHud = (InGameHud) (Object) this;
        MixinInGameHud hud = (MixinInGameHud) (Object) this;
        ClientPlayerEntity player = hud.getClient().player;
        TextRenderer textRenderer = oldHud.getFontRenderer();
        String overlayMessage = String.format("%d %d %d", x, y, z);
        int strWidth = textRenderer.getWidth(overlayMessage);

        hud.getClient().getProfiler().push("overlayCoords");
        int l = 255;
        RenderSystem.pushMatrix();
        RenderSystem.translatef(10.0f + (strWidth/2), 10.0f, 0.0f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        int m = 0xFFFFFF;
        int n = l << 24 & 0xFF000000;
        this.drawTextBackground(matrices, textRenderer, -4, strWidth, 0xFFFFFF | n);
        textRenderer.draw(matrices, overlayMessage, (float) (-strWidth / 2), -4.0f, m | n);
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
        hud.getClient().getProfiler().pop();
    }

    private void drawTextBackground(MatrixStack matrices, TextRenderer textRenderer, int yOffset, int width, int color) {
        MixinInGameHud hud = (MixinInGameHud) (Object) this;

        int i = hud.getClient().options.getTextBackgroundColor(0.0f);
        if (i != 0) {
            int j = -width / 2;
            InGameHud.fill(matrices, j - 2, yOffset - 2, j + width + 2, yOffset + 9 + 2, BackgroundHelper.ColorMixer.mixColor(i, color));
        }
    }
}
