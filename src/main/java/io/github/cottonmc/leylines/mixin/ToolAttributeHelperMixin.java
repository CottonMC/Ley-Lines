package io.github.cottonmc.leylines.mixin;

import com.google.common.collect.Multimap;
import io.github.cottonmc.leylines.upgrades.UpgradeContainer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Item.class)
public abstract class ToolAttributeHelperMixin {

    @Shadow public abstract Item asItem();

    @Inject(
            at = @At("RETURN"),
            method = "getAttributeModifiers"
    )
    private void getAttributes(EquipmentSlot equipmentSlot_1, CallbackInfoReturnable<Multimap<String, EntityAttributeModifier>> cir) {
        if(asItem() instanceof ToolItem){
            List<EntityAttributeModifier> storedAttributes = ((UpgradeContainer) asItem()).getStoredAttributes();

            Multimap<String, EntityAttributeModifier> returnValue = cir.getReturnValue();

            for (EntityAttributeModifier storedAttribute : storedAttributes) {
                returnValue.put(storedAttribute.getName(),storedAttribute);
            }
        }
    }

}
