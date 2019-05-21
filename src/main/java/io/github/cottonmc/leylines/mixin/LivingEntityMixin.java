package io.github.cottonmc.leylines.mixin;

import io.github.cottonmc.leylines.attributes.EntityAttributesKt;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AbstractEntityAttributeContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    private AbstractEntityAttributeContainer attributeContainer;

    @Inject(
            method = "initAttributes",
            at = @At("TAIL")
    )
    private void initAttributes(CallbackInfo ci){
        attributeContainer.register(EntityAttributesKt.getDETERIORIATE());
        attributeContainer.register(EntityAttributesKt.getHERCULIAN_STRENGTH());
    }
}
