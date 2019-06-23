package io.github.cottonmc.leylines.container

import io.github.cottonmc.cotton.gui.CottonScreenController
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.leylines.Constants
import io.github.cottonmc.leylines.WRAITH_CAGE
import io.github.cottonmc.leylines.inventory.InventoryBase
import io.github.cottonmc.leylines.inventory.WatchedInventory
import io.github.cottonmc.leylines.inventory.get
import io.github.cottonmc.leylines.upgrades.ToolUpgradeWraithManager
import net.minecraft.container.BlockContext
import net.minecraft.container.SlotActionType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolItem
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.recipe.RecipeType

class WraithInserterGuiController(syncId: Int, playerInventory: PlayerInventory, val context: BlockContext) : CottonScreenController(RecipeType.SMELTING, syncId, playerInventory), WatchedInventory.InventoryListener {

    val inv = InventoryBase(7)
    val watchedInput = WatchedInventory(inv, this)
    val output = InventoryBase(1)
    val outputSlot: WItemSlot

    init {

        val rootPanel = getRootPanel() as WGridPanel

        rootPanel.add(WLabel(TranslatableComponent("block.${Constants.modid}.wraith_manipulator"), WLabel.DEFAULT_TEXT_COLOR), 0, 0)

        val inputSlot = WItemSlot.of(watchedInput, 0)


        outputSlot = WItemSlot(output, 0,1,1,true,true)
        rootPanel.add(inputSlot, 5, 3)
        rootPanel.add(WItemSlot.of(watchedInput, 1), 3, 3)
        rootPanel.add(outputSlot, 4, 1)

        rootPanel.add(this.createPlayerInventoryPanel(), 0, 5)

        rootPanel.validate(this)
    }

    override fun getCraftingResultSlotIndex(): Int {
        return -1
    }

    override fun onSlotClick(slotNumber: Int, button: Int, action: SlotActionType, player: PlayerEntity): ItemStack {
        val cursorStack = player.inventory.cursorStack
        if (!cursorStack.isEmpty) {
            if (slotNumber == 2 || slotNumber == 0 && cursorStack.item != WRAITH_CAGE) return cursorStack
        }
        if (slotNumber == 2) {
            if (action != SlotActionType.PICKUP && action != SlotActionType.PICKUP_ALL && action != SlotActionType.QUICK_CRAFT && action != SlotActionType.QUICK_MOVE && action != SlotActionType.THROW) {
                return cursorStack
            } else {
                val result = getResult(consume = true)
                inv[0].amount--
                this.getSlot(2).stack = result
                //return result
                player.inventory.markDirty();
            }
        }
        return super.onSlotClick(slotNumber, button, action, player)
    }

    override fun watch(inv: Inventory) {
        val slot = getSlot(2)

        slot.stack = getResult(consume = false)

    }

    /**
     * handles the crafting:
     * slot 1 is the thing that we insert the tool into
     * slot 0 currently holds the wraith
     * the wraith is consumed, and a new tag is added to the stack.
     * */
    fun getResult(consume: Boolean): ItemStack {
        val slot = getSlot(1)
        if (slot.stack.item is ArmorItem || slot.stack.item is ToolItem) {
            val result = slot.stack.copy()
            val componentSlot = getSlot(0)

            if(componentSlot.stack.item == WRAITH_CAGE) {
                if (consume) {
                    inv.forEach {
                        it.amount--
                    }
                }
                ToolUpgradeWraithManager.insertWraithInto(result)
            }
            return result
        }

        return ItemStack.EMPTY
    }
}