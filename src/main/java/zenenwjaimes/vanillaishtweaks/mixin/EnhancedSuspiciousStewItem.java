package zenenwjaimes.vanillaishtweaks.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Mixin(Item.class)
public class EnhancedSuspiciousStewItem {
    @Inject(at = @At("HEAD"), method = "appendTooltip")
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo info) {
        Item rawItem = stack.getItem();
        if (rawItem == Items.SUSPICIOUS_STEW && stack.getNbt() != null && stack.getNbt().contains("Effects")) {
            NbtList listTag = (NbtList) stack.getNbt().get("Effects");

            listTag.forEach((item) -> {
                StatusEffect statusEffect = StatusEffect.byRawId(((NbtCompound)item).getByte("EffectId"));
                if (statusEffect != null) {
                    MutableText effectsInfo = statusEffect.getName().copyContentOnly();
                    tooltip.add(effectsInfo.formatted(Formatting.ITALIC).formatted(Formatting.AQUA));
                }
            });
        }
    }
}
