package io.github.cottonmc.leylines.inventory

import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag

class ResizeableInventory : InventoryBase(1) {

    fun resize(size: Int) {
        val newInventory = Array<ItemStack?>(size) { ItemStack.EMPTY }
        items.forEachIndexed { index, stack ->
            newInventory[index] = stack
        }
        items = newInventory
    }

    /**
     * when we load the inventory, we make sure that we have the correct size.
     * */
    override fun createFromTag(compoundTag: CompoundTag) {
        val list = compoundTag.getList("items", 10)
        resize(list.size)

        super.createFromTag(compoundTag)
    }

}