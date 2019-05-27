package io.github.cottonmc.leylines.container

import io.github.cottonmc.cotton.gui.CottonScreenController
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.leylines.Constants
import io.github.cottonmc.leylines.inventory.InventoryBase
import io.github.cottonmc.leylines.inventory.WatchedInventory
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.recipe.RecipeType

class WraithInserterGui(syncId: Int, playerInventory: PlayerInventory, val context: BlockContext) : CottonScreenController(RecipeType.SMELTING, syncId, playerInventory), WatchedInventory.InventoryListener  {

    val inv = InventoryBase(7)
    val watchedInput = WatchedInventory(inv, this)
    val output = InventoryBase(1)
    val outputSlot: WItemSlot

    init {

        val rootPanel = getRootPanel() as WGridPanel

        rootPanel.add(WLabel(TranslatableComponent("block.${Constants.modid}.wraith_manipulator"), WLabel.DEFAULT_TEXT_COLOR), 0, 0)

        val inputSlot = WItemSlot.of(watchedInput, 0)


        outputSlot = WItemSlot.of(output, 0)
        rootPanel.add(inputSlot, 4, 1)
        rootPanel.add(WItemSlot.of(watchedInput, 1), 4, 1)
        rootPanel.add(outputSlot, 4, 3)

        rootPanel.add(this.createPlayerInventoryPanel(), 0, 4)

        rootPanel.validate(this)
    }

    override fun getCraftingResultSlotIndex(): Int {
        return -1
    }

    override fun watch(inv: Inventory) {
    }
}