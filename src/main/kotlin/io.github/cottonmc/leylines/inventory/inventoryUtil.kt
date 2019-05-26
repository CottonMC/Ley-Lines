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

/**
 * runs a function on the slot range of the inventory.
 * */
fun Inventory.onRange(range: IntRange, action: (ItemStack) -> Unit) {
    for (i in range) {
        action(this[i])
    }
}

/**
 * runs a function on the slot range of the inventory. Skips over empty stacks
 * */
fun Inventory.onRangeExcludeEmpty(range: IntRange, action: (ItemStack) -> Unit) {
    var skipped = 0;
    if(this.isInvEmpty)
        return
    for (i in range) {
        if(!this[i].isEmpty)
        {
            while(this[i+skipped].isEmpty){
                if(i+skipped> this.invSize-1){
                    return
                }
                skipped++
            }
            action(this[i+skipped])
        }
    }
}

fun Inventory.forEach(action: (ItemStack) -> Unit){
    for (i in 0..invSize) {
        action(this[i])
    }
}


/**
 * Inserts a stack into the next empty slot.
 * returns false if the stack was not inserted.
 * */
fun Inventory.insertIntoNextEmptySlot(stack: ItemStack): Boolean {
    if(stack.isEmpty)
        return true

    for (i in 0 until this.invSize) {
        if(this[i].isEmpty)
        {
            this[i] = stack
            this.markDirty()
            return true
        }
    }

    return false
}