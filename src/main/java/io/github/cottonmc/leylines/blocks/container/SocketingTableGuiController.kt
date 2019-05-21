package io.github.cottonmc.leylines.blocks.container

import io.github.cottonmc.leylines.Constants
import io.github.cottonmc.leylines.attributes.SocketCraftingHandler
import io.github.cottonmc.leylines.attributes.attributeMap
import io.github.cottonmc.leylines.client.gui.DynamicTextComponent
import io.github.cottonmc.leylines.inventory.InventoryBase
import io.github.cottonmc.leylines.inventory.set
import io.github.cottonmc.cotton.gui.CottonScreenController
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.logging.ModLogger
import net.minecraft.container.Container
import net.minecraft.container.ContainerListener
import net.minecraft.container.Slot
import net.minecraft.container.SlotActionType
import net.minecraft.recipe.RecipeType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.tag.ItemTags
import net.minecraft.util.DefaultedList


/**
 * The gui for the socketing table.
 *
 * Every item has a maximum number of sockets, items are consumed from the left to right, top row first.
 * The amount of sockets available is displayed bellow the input slot.
 * */
class SocketingTableGuiController(syncId: Int, playerInventory: PlayerInventory) : CottonScreenController(RecipeType.SMELTING, syncId, playerInventory) {

    companion object {
        var logger = ModLogger(Constants.modid, "socketing table")
    }

    val inv = InventoryBase(7)
    val output = InventoryBase(1)
    val socketDisplay = DynamicTextComponent("")

    init {

        val rootPanel = getRootPanel() as WGridPanel

        rootPanel.add(WLabel(TranslatableComponent("block.${Constants.modid}.socketing_table"), WLabel.DEFAULT_TEXT_COLOR), 0, 0)

        val inputSlot = WItemSlot.of(inv, 0)

        val socketCounterMonitor = WLabel(socketDisplay, 1)

        rootPanel.add(socketCounterMonitor, 2, 1)

        rootPanel.add(inputSlot, 1, 1)
        rootPanel.add(WItemSlot.of(inv, 1), 3, 1)
        rootPanel.add(WItemSlot.of(inv, 2), 4, 1)
        rootPanel.add(WItemSlot.of(inv, 3), 5, 1)
        rootPanel.add(WItemSlot.of(inv, 4), 3, 2)
        rootPanel.add(WItemSlot.of(inv, 5), 4, 2)
        rootPanel.add(WItemSlot.of(inv, 6), 5, 2)
        rootPanel.add(WItemSlot.of(output, 0), 7, 1)

        rootPanel.add(this.createPlayerInventoryPanel(), 0, 4)

        rootPanel.validate(this)

        addListener(SocketListener(this))
    }

    override fun canInsertIntoSlot(slot: Slot): Boolean {
        return slot.inventory != output
    }


    /**
     * When inserting into slot 0, we check if it is marked as a socketable item.
     * when inserting into slot 7, we prevent it.
     *
     * */
    override fun onSlotClick(slotNumber: Int, button: Int, action: SlotActionType?, player: PlayerEntity): ItemStack {
        val cursorStack = player.inventory.cursorStack
        if (slotNumber == 7) {
            return cursorStack
        } else
            if (slotNumber == 0) {
                if (!cursorStack.isEmpty) {
                    val tags = ItemTags.getContainer().getTagsFor(cursorStack.item)
                    Constants.Sockets.socketCounts.firstOrNull { tags.contains(it) } ?: return cursorStack
                }
            } else if (slotNumber in 1..6) {
                val tags = ItemTags.getContainer().getTagsFor(cursorStack.item)

                attributeMap.keys.firstOrNull { tags.contains(it) } ?: return cursorStack
            }
        return super.onSlotClick(slotNumber, button, action, player)
    }

    override fun close(playerEntity_1: PlayerEntity) {
        dropInventory(playerEntity_1, playerEntity_1.world, inv)
    }

    override fun getCraftingResultSlotIndex(): Int {
        return -1 //There's no real result slot
    }

    class SocketListener(val socketingTableGuiController: SocketingTableGuiController) : ContainerListener {
        override fun onContainerRegistered(p0: Container, p1: DefaultedList<ItemStack>) {
        }

        override fun onContainerPropertyUpdate(p0: Container, p1: Int, p2: Int) {
        }

        override fun onContainerSlotUpdate(p0: Container, p1: Int, p2: ItemStack) {
            if (p1 != 7) {
                logger.debug("crafting")
                val stacks = p0.stacks
                val socketItems = stacks.subList(1, 6)
                val socketedItem = stacks[0]
                val resultStack = SocketCraftingHandler.addToItem(socketedItem, socketItems.toTypedArray(), true)
                socketingTableGuiController.output[0] = resultStack

            }
            if (p1 == 7) {
                logger.debug("calculating crafting result")

                val stacks = p0.stacks
                val socketItems = stacks.subList(1, 6)
                val socketedItem = stacks[0]
                val resultStack = SocketCraftingHandler.addToItem(socketedItem, socketItems.toTypedArray(), true)
                socketedItem.amount--
            }
        }
    }
}

