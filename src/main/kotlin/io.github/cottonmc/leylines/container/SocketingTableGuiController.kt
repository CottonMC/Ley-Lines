package io.github.cottonmc.leylines.container

import io.github.cottonmc.leylines.Constants
import io.github.cottonmc.leylines.upgrades.attributeMap
import io.github.cottonmc.leylines.client.gui.DynamicTextComponent
import io.github.cottonmc.cotton.gui.CottonScreenController
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.logging.ModLogger
import io.github.cottonmc.leylines.Constants.Sockets.tagID
import io.github.cottonmc.leylines.upgrades.getAttributesForItem
import io.github.cottonmc.leylines.inventory.*
import net.minecraft.container.*
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.recipe.RecipeType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.tag.ItemTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.World


/**
 * The gui for the socketing table.
 *
 * Every item has a maximum number of sockets, items are consumed from the left to right, top row first.
 * The amount of sockets available is displayed bellow the input slot.
 * */
@Suppress("CAST_NEVER_SUCCEEDS")
class SocketingTableGuiController(syncId: Int, playerInventory: PlayerInventory, private val context: BlockContext) : CottonScreenController(RecipeType.SMELTING, syncId, playerInventory), WatchedInventory.InventoryListener {


    companion object {
        var logger = ModLogger(Constants.modid, "socketing table")
    }

    private val inv = InventoryBase(7)
    private val watchedInput = WatchedInventory(inv, this)
    private val output = InventoryBase(1)
    private val socketDisplay = DynamicTextComponent("")
    private val outputSlot: WItemSlot

    init {

        val rootPanel = getRootPanel() as WGridPanel

        rootPanel.add(WLabel(TranslatableComponent("block.${Constants.modid}.socketing_table"), WLabel.DEFAULT_TEXT_COLOR), 0, 0)

        val inputSlot = WItemSlot.of(watchedInput, 0)

        val socketCounterMonitor = WLabel(socketDisplay, 1)

        rootPanel.add(socketCounterMonitor, 2, 1)
        outputSlot = WItemSlot(output, 0,1,1,true,true)
        rootPanel.add(inputSlot, 1, 1)
        rootPanel.add(WItemSlot.of(watchedInput, 1), 3, 1)
        rootPanel.add(WItemSlot.of(watchedInput, 2), 4, 1)
        rootPanel.add(WItemSlot.of(watchedInput, 3), 5, 1)
        rootPanel.add(WItemSlot.of(watchedInput, 4), 3, 2)
        rootPanel.add(WItemSlot.of(watchedInput, 5), 4, 2)
        rootPanel.add(WItemSlot.of(watchedInput, 6), 5, 2)
        rootPanel.add(outputSlot, 7, 1)

        rootPanel.add(this.createPlayerInventoryPanel(), 0, 4)

        rootPanel.validate(this)
    }

    override fun canInsertIntoSlot(slot: Slot): Boolean {
        return slot.inventory != output
    }

    override fun onContentChanged(inventory_1: Inventory?) {

    }
    /**
     * When inserting into slot 0, we check if it is marked as a socketable item.
     * when inserting into slot 7, we prevent it.
     *
     * */
    override fun onSlotClick(slotNumber: Int, button: Int, action: SlotActionType, player: PlayerEntity): ItemStack {
        val cursorStack = player.inventory.cursorStack
        if (!cursorStack.isEmpty) {
            if (slotNumber == 7) {

                return cursorStack
            }
            if (slotNumber == 0) {
                val tags = ItemTags.getContainer().getTagsFor(cursorStack.item)
                Constants.Sockets.socketCounts.firstOrNull { tags.contains(it) } ?: return cursorStack
            } else if (slotNumber in 1..6) {
                val tags = ItemTags.getContainer().getTagsFor(cursorStack.item)

                attributeMap.keys.firstOrNull { tags.contains(it) } ?: return cursorStack
            }
        }
        if (slotNumber == 7) {
            if (action != SlotActionType.PICKUP && action != SlotActionType.PICKUP_ALL && action != SlotActionType.QUICK_CRAFT && action != SlotActionType.QUICK_MOVE && action != SlotActionType.THROW) {
                return cursorStack
            } else {
                val result = getResult(consume = true)
                socketDisplay.text = ""
                inv[0].amount--
                this.getSlot(7).stack = result
                //return result
                player.inventory.markDirty()
            }
        }
        return super.onSlotClick(slotNumber, button, action, player)
    }

    override fun close(playerEntity_1: PlayerEntity) {
        dropInventory(playerEntity_1, playerEntity_1.world, inv)
    }

    override fun getCraftingResultSlotIndex(): Int {
        return -1 //There's no real result slot
    }

    private fun getResult(consume: Boolean): ItemStack {
        val input = inv[0]

        if (input.isEmpty) {
            this.getSlot(7).stack = ItemStack.EMPTY
            socketDisplay.text = ""
            return ItemStack.EMPTY
        } else {
            val result = input.copy()
            result.amount = 1

            val resultItem = result.item
            val socketCount = getSocketCount(resultItem)

            val tag = result.orCreateTag
            if (tag.containsKey(tagID)) {
                val socketsUsed = tag.getList(tagID, 8).size
                socketDisplay.text = (socketCount - socketsUsed).toString()
            } else
                socketDisplay.text = socketCount.toString()

            //walk through all inventory slots, and add them to the sockets.
            inv.onRangeExcludeEmpty(1..6) { itemStack ->
                val identifier = Registry.ITEM.getId(itemStack.item)
                //adds the socketed item to the stack, and if we succeed, than proceeds with the craft.
                if ((result as Socketable).addtoSocket(identifier)) {
                    val equipmentSlot = when (resultItem) {
                        is ArmorItem -> resultItem.slotType
                        else -> EquipmentSlot.MAINHAND
                    }

                    //if the item has no custom attributes, than we add the default modifiers to the custom attributes, so we can't loose them.
                    if (!result.orCreateTag.containsKey("AttributeModifiers")) {
                        resultItem.getAttributeModifiers(equipmentSlot)
                                .asMap()
                                .map {
                                    Pair(it.key, it.value.first())
                                }.forEach { mod ->
                                    val amount = when (mod.first) {
                                        EntityAttributes.ATTACK_DAMAGE.id -> mod.second.amount + 1
                                        EntityAttributes.ATTACK_SPEED.id -> mod.second.amount + 4
                                        EntityAttributes.ARMOR.id-> mod.second.amount
                                        else -> return@forEach
                                    }
                                    result.addAttributeModifier(mod.first, EntityAttributeModifier(mod.first, amount, mod.second.operation), equipmentSlot)
                                }
                    }

                    //we add all of the expected modifiers to the item.
                    val attributesForItem = getAttributesForItem(itemStack.item)
                    attributesForItem.forEach { mod ->
                        result.addAttributeModifier(mod.name, mod, equipmentSlot)
                    }

                    if (consume)
                        itemStack.amount--
                } else {
                    return@onRangeExcludeEmpty
                }
            }
            return result
        }

    }

    override fun watch(inv: Inventory) {

        context.run { _: World?, _: BlockPos? ->
            this.getSlot(7).stack = getResult(false)
        }
    }
}

