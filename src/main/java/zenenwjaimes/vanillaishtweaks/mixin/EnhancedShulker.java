package zenenwjaimes.vanillaishtweaks.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Mixin(ShulkerBoxBlock.class)
public class EnhancedShulker {
    @Inject(at = @At("TAIL"), method = "appendTooltip")
    private void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options, CallbackInfo info) {
        // Not a shulker, return. We only want to mess with shulker box tooltips.
        Block block = Block.getBlockFromItem(stack.getItem());
        if (stack.isEmpty() || !(block instanceof ShulkerBoxBlock)) {
            return;
        }

        // Items to pop from the text list
        int popCount = 0;

        // Only remove items on non-empty containers
        CompoundTag compoundTag = stack.getSubTag("BlockEntityTag");
        if (compoundTag != null) {
            if (compoundTag.contains("Items", 9)) {
                DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(27, ItemStack.EMPTY);
                Inventories.fromTag(compoundTag, defaultedList);
                int i = 0;
                int j = 0;
                Iterator it = defaultedList.iterator();

                while(it.hasNext()) {
                    ItemStack itemStack = (ItemStack)it.next();
                    if (!itemStack.isEmpty()) {
                        ++j;
                        if (i <= 4) {
                            ++i;
                            popCount += 1;
                        }
                    }
                }

                if (j - i > 0) {
                    popCount += 1;
                }

                int listSize = (tooltip.size() - 1);
                for (int idx = 0; idx < popCount; idx++) {
                    tooltip.remove(listSize - idx);
                }
            }
        }
    }
}
