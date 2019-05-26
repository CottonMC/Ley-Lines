package io.github.cottonmc.leylines.inventory

import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

class WatchedInventory(private val rootInv: Inventory, private val listener: InventoryListener) : Inventory by rootInv {

    override fun takeInvStack(var1: Int, var2: Int): ItemStack {
        val result = rootInv.takeInvStack(var1, var2)
        if (result != ItemStack.EMPTY) {
            listener.watch(rootInv)
        }
        return result
    }

    override fun removeInvStack(var1: Int): ItemStack{
        val result = rootInv.removeInvStack(var1)
        if (result != ItemStack.EMPTY) {
            listener.watch(rootInv)
        }
        return result
    }

    override fun setInvStack(var1: Int, var2: ItemStack){
        val itemStack = rootInv[var1]
        rootInv.setInvStack(var1,var2)
        if(!itemStack.equals(var2)){
            listener.watch(rootInv)
        }
    }


    interface InventoryListener {
        fun watch(inv: Inventory)
    }
}