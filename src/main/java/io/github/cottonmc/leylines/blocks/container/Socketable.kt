package io.github.cottonmc.leylines.blocks.container

import net.minecraft.inventory.Inventory

/**
 * Mixed into the itemstack, to make it accept upgrade items.
 * */
interface Socketable {
    /**
     * weather this stack has been already socketed.
     * */
    fun isSocketed():Boolean
    /**
     * returns the internal socket inventory.
     * */
    fun getSocketInventory():Inventory

    /**
     * returns the socket inventory, after setting it to a specific size
     * */
    fun getSocketInventory(size:Int):Inventory

}