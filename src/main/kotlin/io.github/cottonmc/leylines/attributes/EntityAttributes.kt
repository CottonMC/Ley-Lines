package io.github.cottonmc.leylines.attributes

import io.github.cottonmc.leylines.Constants
import net.minecraft.entity.attribute.ClampedEntityAttribute
import net.minecraft.entity.attribute.EntityAttribute

val DETERIORIATE: EntityAttribute = ClampedEntityAttribute(
        null as EntityAttribute?,
        "${Constants.modid}.deteriorate",
        1.0,
        0.0,
        5.0)
        .setName("Deteriorate")
val HERCULIAN_STRENGTH = ClampedEntityAttribute(
        null as EntityAttribute?,
        "${Constants.modid}.herculean_strength",
        1.0,
        0.0,
        5.0)
        .setName("Herculean Strength")

