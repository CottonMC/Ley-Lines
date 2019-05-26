package io.github.cottonmc.leylines.attributes

import io.github.cottonmc.leylines.Constants.modid
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation.ADDITION
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation.MULTIPLY_TOTAL
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.Item
import net.minecraft.tag.ItemTags
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.jetbrains.annotations.NotNull

fun getAttributesForItem(item: @NotNull Item): MutableList<EntityAttributeModifier> {
    val tags = ItemTags.getContainer().getTagsFor(item)

    val attributes = ArrayList<EntityAttributeModifier>()
    for (tag in tags) {
        if (attributeMap.containsKey(tag))
            attributes.add(attributeMap[tag]?.invoke()!!)
    }

    return attributes
}

fun getAttributesForItem(item:@NotNull Identifier): MutableList<EntityAttributeModifier>{
    return getAttributesForItem(Registry.ITEM.get(item))
}

interface UpgradeContainer {
    fun getStoredAttributes(): List<EntityAttributeModifier>
}

val attributeMap = mapOf(
         Pair(Identifier(modid, "deterioriate_mod"),
                 {EntityAttributeModifier(
                        DETERIORIATE.id,
                        1.0,
                        ADDITION)}
        ),
        Pair(Identifier(modid, "attack_mod"),
                {EntityAttributeModifier(
                        EntityAttributes.ATTACK_DAMAGE.id
                        , 1.0
                        , ADDITION)}
        ),
        Pair(Identifier(modid, "attack_boost_mod"),
                {EntityAttributeModifier(
                        EntityAttributes.ATTACK_DAMAGE.id
                        , 10.0
                        , MULTIPLY_TOTAL)}
        ),
        Pair(Identifier(modid, "defense_mod"),
                {EntityAttributeModifier(
                        EntityAttributes.ARMOR.id,
                        1.0,
                        ADDITION
                )}),
        Pair(Identifier(modid, "defense_boost_mod"),
                {EntityAttributeModifier(
                        EntityAttributes.ARMOR.id,
                        10.0,
                        MULTIPLY_TOTAL
                )}),
        Pair(Identifier(modid, "knockback_mod"),
                {EntityAttributeModifier(
                        EntityAttributes.ATTACK_KNOCKBACK.id,
                        1.0,
                        ADDITION
                )}),
        Pair(Identifier(modid, "knockback_boost_mod"),
                {EntityAttributeModifier(
                        EntityAttributes.ATTACK_KNOCKBACK.id,
                        1.0,
                        MULTIPLY_TOTAL
                )}),
        Pair(Identifier(modid, "follow_range_mod"),
                {EntityAttributeModifier(
                        EntityAttributes.FOLLOW_RANGE.id,
                        3.0,
                        ADDITION
                )}),
        Pair(Identifier(modid, "follow_range_boost_mod"),
                {EntityAttributeModifier(
                        EntityAttributes.FOLLOW_RANGE.id,
                        20.0,
                        MULTIPLY_TOTAL
                )})
)