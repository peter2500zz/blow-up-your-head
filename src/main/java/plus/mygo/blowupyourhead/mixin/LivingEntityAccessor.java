package plus.mygo.blowupyourhead.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
interface LivingEntityAccessor {
    @Invoker("shouldDropLoot")
    boolean invokeShouldDropLoot(ServerLevel level);
}
