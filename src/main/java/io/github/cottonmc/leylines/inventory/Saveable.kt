package io.github.cottonmc.leylines.inventory

import net.minecraft.nbt.CompoundTag

interface Saveable {
    fun isDirty():Boolean
    fun toTag(compoundTag: CompoundTag): CompoundTag
    fun createFromTag(compoundTag: CompoundTag)
}