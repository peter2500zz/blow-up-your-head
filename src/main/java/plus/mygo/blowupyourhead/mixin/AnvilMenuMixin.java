package plus.mygo.blowupyourhead.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {
    @Shadow
    private @Nullable String itemName;

    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/AnvilMenu;broadcastChanges()V"
            ),
            method = "createResult"
    )
    private void renamePlayerHead(CallbackInfo ci) {
        // 获取铁砧的产物
        ResultContainer resultSlots = ((ItemCombinerMenuAccessor) this).getResultSlots();
        ItemStack resultItem = resultSlots.getItem(0);

        // 检查是不是玩家头物品
        if (!ItemStack.isSameItem(resultItem, new ItemStack(Items.PLAYER_HEAD))) {
            return;
        }

        // 检查输入框中的字符是否符合玩家命名规则
        if (this.itemName == null || !this.itemName.matches("^[a-zA-Z0-9_]{3,16}$")) {
            resultItem.remove(DataComponents.PROFILE);
            return;
        }

        // 到这里就是改名为有效玩家 ID 的玩家的头

        // 清除物品自定义名称
        resultItem.remove(DataComponents.CUSTOM_NAME);
        // 将目标玩家设定为输入的玩家 ID
        resultItem.set(DataComponents.PROFILE, ResolvableProfile.createUnresolved(this.itemName));
    }
}
