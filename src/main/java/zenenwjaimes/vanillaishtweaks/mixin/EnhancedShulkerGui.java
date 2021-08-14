package zenenwjaimes.vanillaishtweaks.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public class EnhancedShulkerGui extends DrawableHelper {
    @Shadow
    protected TextRenderer textRenderer;
    @Shadow
    protected ItemRenderer itemRenderer;
    @Shadow
    protected MinecraftClient client;

    protected int toolTipWidth;
    @Shadow
    public int width;
    @Shadow
    public int height;
    private static final Identifier SHULKER_PREVIEW = new Identifier("vanillaishtweaks", "textures/gui/shulker_preview.png");

    public EnhancedShulkerGui() {
        toolTipWidth = 0;
    }

    @Inject(method = "renderOrderedTooltip", at = @At(value = "TAIL"))
    public void renderOrderedTooltip(MatrixStack matrices, List<? extends OrderedText> lines, int x, int y, CallbackInfo ci) {
        if (!lines.isEmpty()) {
            Iterator it = lines.iterator();
            toolTipWidth = 0;

            while (it.hasNext()) {
                OrderedText orderedText = (OrderedText) it.next();
                int rowWidth = this.textRenderer.getWidth(orderedText);
                if (rowWidth > toolTipWidth) {
                    toolTipWidth = rowWidth;
                }
            }
        }
    }

    @Inject(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V", at = @At(value = "TAIL"))
    protected void renderTooltip(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo ci) {
        Block block = Block.getBlockFromItem(stack.getItem());
        if (stack.isEmpty() || !(block instanceof ShulkerBoxBlock)) {
            return;
        }

        NbtCompound nbtCompound = stack.getSubNbt("BlockEntityTag");
        if (nbtCompound != null) {
            if (nbtCompound.contains("Items", 9)) {
                DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(27, ItemStack.EMPTY);
                Inventories.readNbt(nbtCompound, defaultedList);
                Iterator it = defaultedList.iterator();

                // Calculate if tooltip will render offscreen
                if ((toolTipWidth + x + 12) > width) {
                    x -= toolTipWidth + 28;
                }

                drawShulkerBackground(matrices, 0, x, y);

                int i = 0;
                int j = -1;

                while(it.hasNext()) {
                    ItemStack itemStack = (ItemStack)it.next();
                    ++j;

                    if ((j % 9) == 0) {
                        ++i;
                    }

                    if (!itemStack.isEmpty()) {
                        // Only show the count for stacks greater than 1 just like the normal game
                        String stackCount = (itemStack.getCount() > 1 ? String.valueOf(itemStack.getCount()) : "");

                        itemRenderer.zOffset = 700.0f;
                        itemRenderer.renderInGuiWithOverrides(itemStack, x + 6 + ((j % 9) * 18) + 7, y + (18 * (i - 1)) + 7 - 86);
                        itemRenderer.renderGuiItemOverlay(textRenderer, itemStack, x + 6 + ((j % 9) * 18) + 7, y + (18 * (i - 1)) + 7 - 86, stackCount);
                        itemRenderer.zOffset = 0.0f;
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    protected void drawShulkerBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, SHULKER_PREVIEW);
        int prevZOffset = getZOffset();
        setZOffset(700);
        drawTexture(matrices, mouseX+6, mouseY-86, 0.5f, 0.5f, 176, 68, 256, 256);
        setZOffset(prevZOffset);
    }
}
