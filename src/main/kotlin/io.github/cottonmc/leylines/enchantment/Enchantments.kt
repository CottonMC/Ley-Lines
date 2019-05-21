package io.github.cottonmc.leylines.enchantment

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityGroup
import net.minecraft.entity.EquipmentSlot.*
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.util.registry.Registry

//allows the player to walk on lava
class FlameWalkerEnchant : Enchantment(
        Weight.VERY_RARE,
        EnchantmentTarget.ARMOR_FEET,
        arrayOf(FEET)
)

//increases movement speed
class LighFeetEnchant : Enchantment(
        Weight.UNCOMMON,
        EnchantmentTarget.ARMOR_FEET,
        arrayOf(LEGS)
)

//decreases movement speed
class HeavyFeet : Enchantment(
        Weight.COMMON,
        EnchantmentTarget.ARMOR_FEET,
        arrayOf(LEGS)
) {
    override fun isCursed(): Boolean {
        return true
    }
}

//increases damage taken
class WeakBody : Enchantment(
        Weight.UNCOMMON,
        EnchantmentTarget.WEARABLE,
        arrayOf(LEGS, FEET, CHEST, HEAD)
) {
    override fun isCursed(): Boolean {
        return true
    }

    override fun getMaximumLevel(): Int {
        return 5
    }

    override fun getProtectionAmount(int_1: Int, damageSource_1: DamageSource?): Int {
        return int_1 * -2
    }
}

//increases damage dealt
class HerculienStrength : Enchantment(
        Weight.RARE,
        EnchantmentTarget.WEARABLE,
        arrayOf(LEGS, FEET, CHEST, HEAD)
) {
    override fun getAttackDamage(int_1: Int, entityGroup_1: EntityGroup?): Float {
        return int_1 * 2.toFloat()
    }

    override fun getMaximumLevel(): Int {
        return 5
    }
}

//teleports the player when damaged.
class EnderReflex : Enchantment(
        Weight.RARE,
        EnchantmentTarget.WEARABLE,
        arrayOf(CHEST)
) {
    override fun onUserDamaged(user: LivingEntity, entity_1: Entity?, int_1: Int) {
        user.getEquippedStack(CHEST).applyDamage(15, user,  {  })
        //TODO teleportation code
    }
}

// allows the player to walk on air, while not sneaking.
class ToesOfTheVoid : Enchantment(
        Weight.VERY_RARE,
        EnchantmentTarget.ARMOR_FEET,
        arrayOf(FEET)
)

//whenever the shield blocks, we take a stat from the attacker's equipment.
class Deteriorate:Enchantment(
        Weight.VERY_RARE,
        EnchantmentTarget.ARMOR,
        arrayOf()
), ShieldEnchantable {
    override val onlyShields= true

    override fun getMaximumLevel(): Int {
        return 4
    }
}

private fun register(string_1: String, enchantment_1: Enchantment): Enchantment {
    return Registry.register(Registry.ENCHANTMENT, string_1, enchantment_1) as Enchantment
}


val FLAMEWALKER = register("flamewalker", FlameWalkerEnchant())
val LIGHTFEET = register("lightfeel", LighFeetEnchant())
val HEAVYFEET = register("heavyfeet", HeavyFeet())
val WEAKBODY = register("weakbody", WeakBody())

val TOES_OF_THE_VOID = register("toesofthevoid", ToesOfTheVoid())
val ENDER_REFLEX = register("enderreflex", EnderReflex())
val DETERIORATE = register("deteriorate", Deteriorate())
