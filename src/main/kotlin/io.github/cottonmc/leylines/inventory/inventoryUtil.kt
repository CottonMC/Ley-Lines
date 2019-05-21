package io.github.cottonmc.leylines.inventory

import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

operator fun Inventory.set(index: Int, itemStack: ItemStack?) {
    this.setInvStack(index, itemStack)
}

operator fun Inventory.get(index: Int): ItemStack {
    return this.getInvStack(index)
}

fun Inventory.isInvNotEmpty(): Boolean {
    return !isInvEmpty
}