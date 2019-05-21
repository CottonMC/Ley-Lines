package io.github.cottonmc.leylines.inventory

import com.google.common.collect.ImmutableList
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag


/**
 * This class is a full, reusable inventory, that can save itself.
 *
 * It won't crash, if the data does not match it's state, aka: the block now has fewer/more slots than it did before.
 *
 * It's also easy to traverse.
 * */
open class InventoryBase(val size: Int) : Inventory, Saveable, Iterable<ItemStack> {
    override fun iterator(): Iterator<ItemStack> {
        return items.requireNoNulls().iterator()
    }

    private var isDirty = false

    override fun isDirty(): Boolean {
        return isDirty
    }

    open var items = Array<ItemStack?>(size) { return@Array ItemStack.EMPTY }

    override fun getInvStack(p0: Int): ItemStack {
        return items[p0] ?: ItemStack.EMPTY
    }

    override fun markDirty() {
        isDirty = true
    }

    override fun clear() {
        items.fill(ItemStack.EMPTY)
    }

    override fun setInvStack(p0: Int, p1: ItemStack) {
        items[p0] = p1
    }

    override fun removeInvStack(p0: Int): ItemStack {
        val itemStack = items[p0]
        items[p0] = ItemStack.EMPTY
        return itemStack ?: ItemStack.EMPTY
    }

    override fun canPlayerUseInv(p0: PlayerEntity?): Boolean {
        return true
    }

    override fun getInvSize(): Int {
        return size
    }

    override fun takeInvStack(p0: Int, p1: Int): ItemStack {
        val itemStack = items[p0]!!

        return if (itemStack.amount > p1) {
            val result = itemStack.copy()
            result.amount = p1
            itemStack.amount = itemStack.amount - p1
            result
        } else {
            items[p0] = ItemStack.EMPTY
            itemStack
        }
    }

    override fun isInvEmpty(): Boolean {
        return items.count { it == ItemStack.EMPTY } == items.size
    }

    override fun toTag(compoundTag: CompoundTag): CompoundTag {
        val itemTags = ListTag()
        items.forEach {
            it?.apply {
                itemTags.add(this.toTag(CompoundTag()))
            }
        }
        compoundTag.put("items", itemTags)
        isDirty = false
        return compoundTag
    }

    override fun createFromTag(compoundTag: CompoundTag) {
        val list = compoundTag.getList("items", 10)

        list.forEachIndexed { index, tag ->
            items[index] = ItemStack.fromTag(tag as CompoundTag)
        }

    }

    fun toList(): ImmutableList<ItemStack> {
        return ImmutableList.copyOf(items)
    }
}