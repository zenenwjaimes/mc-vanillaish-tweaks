package zenenwjaimes.vanillaishtweaks.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(targets = "net.minecraft.village.TradeOffers$EnchantBookFactory")
public class EnhancedLibrarian {
    int level;

    int maxLevel;

    int randomness;

    Enchantment enchantment;

    @Redirect(method = "create", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
    private Object getEnchantment(List instance, int i) {
        enchantment = (Enchantment) instance.get(i);
        return enchantment;
    }

    @Redirect(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;nextInt(Lnet/minecraft/util/math/random/Random;II)I"))
    private int enchantmentLevel(Random random, int min, int max) {
        level = MathHelper.nextInt(random, min, max);
        maxLevel = max;

        return level;
    }

    @Redirect(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I", ordinal = 1))
    private int random(Random instance, int i) {
        randomness = instance.nextInt(i);
        return randomness;
    }

    @Inject(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;isTreasure()Z", shift = At.Shift.AFTER))
    private void afterTreasureCheck(Entity entity, Random random, CallbackInfoReturnable<TradeOffer> cir) {
        int price = 2 + randomness + 3 * level;
        if (enchantment.isTreasure()) {
            price *= 2;
        }

        if (price > 64) {
            price = 64;
        }

        MinecraftClient.getInstance().player.sendMessage(Text.of("Enchanted Book %s for %d emeralds".formatted(enchantment.getName(level).getString(), price)));

        if(randomness == 0 && level == maxLevel) {
            MinecraftClient.getInstance().player.sendMessage(Text.of("---> Max enchanted book! %s".formatted(enchantment.getName(level).getString())));
        }
    }
}