package io.github.cottonmc.leylines.container

import io.github.cottonmc.leylines.Constants
import net.minecraft.item.Item
import net.minecraft.tag.ItemTags
import net.minecraft.util.Identifier

/**
 * Mixed into the itemstack, to make it accept upgrade items.
 * */
interface Socketable {
    /**
     * weather this stack has been already socketed.
     * */
    fun isSocketed():Boolean
    /**
     * adds a new entry to the sockets
     * @return true if the entry was added successfully.
     * */
    fun addtoSocket(identifier:Identifier):Boolean

    /**
     * returns every socket entry
     * */
    fun getSocketItems():List<Identifier>


    /**
     * removes a socket entry by id.
     * @return true, if the removal was sucessfull
     * */
    fun removeFromSocket(identifier:Identifier):Boolean



}

fun getSocketCount(item: Item):Int {

    val tags = ItemTags.getContainer().getTagsFor(item)

    val socketCount = tags.map {
        Constants.Sockets.getSocketCountInt(it)
    }.sorted().last()

    return socketCount
}