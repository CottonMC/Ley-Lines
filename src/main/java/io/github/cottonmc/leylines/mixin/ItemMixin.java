package io.github.cottonmc.leylines.mixin;

import io.github.cottonmc.leylines.attributes.TagAttributesKt;
import io.github.cottonmc.leylines.attributes.UpgradeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(Item.class)
@Implements(@Interface(iface = UpgradeContainer.class, prefix = "leylines_upgrades$", unique = true))
public abstract class ItemMixin //implements UpgradeContainer
{

    @Shadow
    public abstract Item asItem();

    @NotNull
    public List<EntityAttributeModifier> leylines_upgrades$getStoredAttributes() {
        return TagAttributesKt.getAttributesForItem(asItem());
    }

}
