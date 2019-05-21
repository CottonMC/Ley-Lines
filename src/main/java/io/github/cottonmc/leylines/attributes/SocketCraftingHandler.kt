package io.github.cottonmc.leylines.attributes

import io.github.cottonmc.leylines.Constants
import io.github.cottonmc.leylines.blocks.container.Socketable
import io.github.cottonmc.leylines.inventory.ResizeableInventory
import io.github.cottonmc.leylines.inventory.isInvNotEmpty
import io.github.cottonmc.leylines.inventory.set
import net.minecraft.item.ItemStack
import net.minecraft.tag.ItemTags

object SocketCraftingHandler {

    fun addToItem(target: ItemStack, sockets: Array<ItemStack>, test: Boolean): ItemStack {
        val resultItem = target.copy()
        val socketInventory = (resultItem as Socketable).getSocketInventory() as ResizeableInventory

        if (socketInventory.isInvNotEmpty())
            return ItemStack.EMPTY

        val tags = ItemTags.getContainer().getTagsFor(target.item)

        val socketCountTag = Constants.Sockets.socketCounts.first { tags.contains(it) }

        val socketCount = when (socketCountTag) {
            Constants.Sockets.ONE_SOCKET_TAG -> 1
            Constants.Sockets.TWO_SOCKET_TAG -> 2
            Constants.Sockets.THREE_SOCKET_TAG -> 3
            Constants.Sockets.FOUR_SOCKET_TAG -> 4
            Constants.Sockets.FIVE_SOCKET_TAG -> 5
            Constants.Sockets.SIX_SOCKET_TAG -> 6
            else -> 0
        }

        socketInventory.resize(socketCount)

        for (i in 0 until socketCount) {
            val socketedItem = sockets[i]
            val stackIntoSocket = socketedItem.copy()
            stackIntoSocket.amount = 1
            socketInventory[i] = stackIntoSocket
            if (!test){
                socketedItem.amount--
            }
        }

        return resultItem
    }

}

