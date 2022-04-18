package zenenwjaimes.vanillaishtweaks.mixin.EnhancedHud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.CompassItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

import static net.minecraft.client.gui.DrawableHelper.GUI_ICONS_TEXTURE;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class EnhancedInGameHud {
    private static final String COORD_FORMAT = "%d, %d, %d";
    private static final String TIME_FORMAT = "Time Of Day: %s";

    @Inject(method = "render", require = -1, at = @At(value = "TAIL"))
    public void renderItem(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        MixinInGameHud hud = (MixinInGameHud) (Object) this;
        ClientPlayerEntity player = hud.getClient().player;
        ItemStack offhand = player.getOffHandStack();
        ItemStack mainhand = player.getMainHandStack();
        boolean showingCoords = false;

        if ((!mainhand.isEmpty() && mainhand.getItem() == Items.COMPASS && CompassItem.hasLodestone(mainhand)) || (!offhand.isEmpty() && offhand.getItem() == Items.COMPASS && CompassItem.hasLodestone(offhand))) {
            ItemStack stack = CompassItem.hasLodestone(mainhand) ? mainhand : offhand;
            NbtCompound compoundTag = stack.getOrCreateNbt();

            if (compoundTag.contains("LodestoneTracked") && !compoundTag.getBoolean("LodestoneTracked")) {
                return;
            }
            Optional<RegistryKey<World>> optional = World.CODEC.parse(NbtOps.INSTANCE, compoundTag.get("LodestoneDimension")).result();
            
            if (optional.isPresent() && compoundTag.contains("LodestonePos")) {
                BlockPos pos = NbtHelper.toBlockPos(compoundTag.getCompound("LodestonePos"));

                if (pos.getX() == 0 && pos.getZ() == 0) {
                    showingCoords = true;
                    this.renderHudText(matrices, String.format(COORD_FORMAT, hud.getClient().player.getBlockPos().getX(), hud.getClient().player.getBlockPos().getY(), hud.getClient().player.getBlockPos().getZ()), 0);
                }
            }
        }

        if ((!mainhand.isEmpty() && mainhand.getItem() == Items.CLOCK) || (!offhand.isEmpty() && offhand.getItem() == Items.CLOCK)) {
            long timeOfDay = player.getEntityWorld().getTimeOfDay();

            this.renderHudText(matrices, String.format(TIME_FORMAT, timeOfDay), showingCoords ? 10 : 0);
        }
    }

    private void renderHudText(MatrixStack matrices, String overlayMessage, int offset) {
        InGameHud oldHud = (InGameHud) (Object) this;
        MixinInGameHud hud = (MixinInGameHud) (Object) this;
        ClientPlayerEntity player = hud.getClient().player;
        TextRenderer textRenderer = oldHud.getTextRenderer();
        int strWidth = textRenderer.getWidth(overlayMessage);

        hud.getClient().getProfiler().push("overlayCoords");
        int l = 255;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        matrices.push();
        matrices.translate(10.0f + (strWidth/2), 10.0f + offset, 0.0f);
        int m = 0xFFFFFF;
        int n = l << 24 & 0xFF000000;
        this.drawTextBackground(matrices, textRenderer, -4, strWidth, 0xFFFFFF | n);
        textRenderer.draw(matrices, overlayMessage, (float) (-strWidth / 2), -4.0f, m | n);
        RenderSystem.disableBlend();
        matrices.pop();
        hud.getClient().getProfiler().pop();
    }

    private void drawTextBackground(MatrixStack matrices, TextRenderer textRenderer, int yOffset, int width, int color) {
        MixinInGameHud hud = (MixinInGameHud) (Object) this;

        int i = hud.getClient().options.getTextBackgroundColor(0.0f);
        if (i != 0) {
            int j = -width / 2;
            InGameHud.fill(matrices, j - 2, yOffset - 2, j + width + 2, yOffset + 9 + 2, ColorHelper.Argb.mixColor(i, color));
        }
    }
}
