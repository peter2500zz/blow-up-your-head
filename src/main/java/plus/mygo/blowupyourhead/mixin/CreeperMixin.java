package plus.mygo.blowupyourhead.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Creeper.class)
public abstract class CreeperMixin {
    @Shadow
    private boolean droppedSkulls;

    @Shadow
    public abstract boolean isPowered();

    /**
     * @see Creeper#killedEntity(ServerLevel, LivingEntity, DamageSource)  参考父类实现
     */
    @Inject(at = @At("HEAD"), method = "killedEntity")
    private void killedPlayer(ServerLevel level, LivingEntity entity, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        // 原始判定，检查 是否允许掉落掉落物 是否是闪电苦力怕 是否已经掉落过头
        if (((LivingEntityAccessor) this).invokeShouldDropLoot(level) && this.isPowered() && !this.droppedSkulls) {
            // 判定击杀的生物是否是玩家
            if (entity instanceof Player player) {
                // 创造一个玩家头
                ItemStack itemStack = new ItemStack(Items.PLAYER_HEAD);
                // 将玩家头的玩家档案设定为这个倒霉蛋
                itemStack.set(DataComponents.PROFILE, ResolvableProfile.createResolved(player.getGameProfile()));

                // 生成玩家头
                entity.spawnAtLocation(level, itemStack);
                // 标记已经掉落过头
                this.droppedSkulls = true;
            }
        }
    }
}