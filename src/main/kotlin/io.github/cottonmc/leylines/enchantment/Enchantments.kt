package io.github.cottonmc.leylines.enchantment

import io.github.cottonmc.leylines.Constants.modid
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.EquipmentSlot.*
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

//allows the player to walk on lava
class FlameWalkerEnchant : Enchantment(
        Weight.VERY_RARE,
        EnchantmentTarget.ARMOR_FEET,
        arrayOf(FEET)
) {
    override fun differs(enchantment_1: Enchantment): Boolean {
        return super.differs(enchantment_1) && enchantment_1 != Enchantments.FROST_WALKER && enchantment_1 != Enchantments.DEPTH_STRIDER
    }
}

// allows the player to walk on air, while not sneaking.
class ToesOfTheVoid : Enchantment(
        Weight.VERY_RARE,
        EnchantmentTarget.ARMOR_FEET,
        arrayOf(FEET)
)

//whenever the shield blocks, we take a stat from the attacker's equipment.
class Deteriorate : Enchantment(
        Weight.VERY_RARE,
        EnchantmentTarget.ARMOR,
        arrayOf()
), ShieldEnchantable {
    override val onlyShields = true

    override fun getMaximumLevel(): Int {
        return 4
    }
}

fun registerEnchantment(identifier: Identifier, enchantment: Enchantment): Enchantment {
    return Registry.register(Registry.ENCHANTMENT, identifier, enchantment) as Enchantment
}

lateinit var FLAMEWALKER: Enchantment
lateinit var TOES_OF_THE_VOID: Enchantment
lateinit var DETERIORATE: Enchantment
