package io.github.cottonmc.leylines.blocks

import io.github.cottonmc.leylines.WRAITH_CAGE
import io.github.cottonmc.leylines.WRAITH_CAGE_EMPTY
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.InventoryProvider
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.state.StateFactory
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.IntegerProperty
import net.minecraft.util.Hand
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.IWorld
import net.minecraft.world.World
import java.util.*

class WraithFabricatorBlock(settings: Settings) : Block(settings), InventoryProvider {
    val random = Random()

    enum class CageState : StringIdentifiable {
        NO_CAGE {
            override fun asString(): String {
                return "no_cage"
            }
        },
        EMPTY {
            override fun asString(): String {
                return "empty"
            }
        },
        FULL {
            override fun asString(): String {
                return "full"
            }
        }
    }

    companion object States {
        val LEVEL: IntegerProperty = IntegerProperty.create("level", 0, 16)
        val HAS_CAGE: EnumProperty<CageState> = EnumProperty.create("cage_state", CageState::class.java)
    }

    init {
        this.defaultState = ((this.stateFactory.defaultState as BlockState)
                .with(LEVEL, 0) as BlockState)
                .with(HAS_CAGE, CageState.NO_CAGE)
    }

    override fun appendProperties(`stateFactory$Builder_1`: StateFactory.Builder<Block, BlockState>) {
        `stateFactory$Builder_1`.add(LEVEL, HAS_CAGE)
    }

    //the comparator output is the state of the cage.
    override fun hasComparatorOutput(blockState: BlockState): Boolean {
        return blockState.get(HAS_CAGE) != CageState.NO_CAGE
    }

    /**
     * comparator output is determined by the state of the soul cage, combined with weather or not it can operate.
     * */
    override fun getComparatorOutput(blockState: BlockState, world_1: World, pos: BlockPos): Int {
        return when (blockState[HAS_CAGE]) {
            CageState.EMPTY -> {
                return if (world_1.getBlockState(pos.down()).block == Blocks.FIRE) {
                    8
                } else {
                    4
                }
            }
            CageState.FULL -> 15
            else -> 0
        }
    }

    override fun activate(blockState_1: BlockState, world_1: World, blockPos_1: BlockPos, playerEntity_1: PlayerEntity, hand_1: Hand, blockHitResult_1: BlockHitResult?): Boolean {
        val stackInHand = playerEntity_1.getStackInHand(hand_1)
        if (stackInHand.item == WRAITH_CAGE_EMPTY && blockState_1.get(HAS_CAGE) == CageState.NO_CAGE) {
            stackInHand.amount--
            world_1.setBlockState(blockPos_1, blockState_1.with(HAS_CAGE, CageState.EMPTY))
            return true
        }
        if (stackInHand.item == Items.SOUL_SAND) {
            stackInHand.amount--
            insertSand(world_1,blockState_1,blockPos_1)
        }

        return false
    }

    override fun neighborUpdate(blockState_1: BlockState?, world: World, blockPos: BlockPos, block_1: Block?, updatedPos: BlockPos, boolean_1: Boolean) {
        super.neighborUpdate(blockState_1, world, blockPos, block_1, updatedPos, boolean_1)
        if (updatedPos == blockPos.down())
            world.updateNeighbors(blockPos, this)

    }

    override fun getInventory(p0: BlockState, p1: IWorld, p2: BlockPos): SidedInventory {
        return FabricatorInventory(p1, p2,this)
    }

    fun insertSand(world: IWorld, blockState: BlockState, pos: BlockPos) {
        if (random.nextInt(10) > 6) {
            val level = blockState[LEVEL]
            if (level < 16) {
                world.setBlockState(pos, blockState.with(LEVEL, level + 1), 3)
                world.updateNeighbors(pos, blockState.block)
            } else {
                world.setBlockState(pos, blockState.with(LEVEL, 0).with(HAS_CAGE, CageState.FULL), 3)
                world.updateNeighbors(pos, blockState.block)
            }
        }
    }

    class FabricatorInventory(
            private val world: IWorld,
            private val pos: BlockPos,
            private val block: WraithFabricatorBlock
    ) : SidedInventory {
        override fun getInvStack(p0: Int): ItemStack {
            return if (p0 == 0) {
                val blockState = world.getBlockState(pos)
                when (blockState[HAS_CAGE]) {
                    CageState.EMPTY -> ItemStack(WRAITH_CAGE_EMPTY)
                    CageState.FULL -> ItemStack(WRAITH_CAGE)
                    else -> ItemStack.EMPTY
                }
            } else
                ItemStack.EMPTY
        }

        override fun markDirty() {

        }

        override fun clear() {
            val blockState = world.getBlockState(pos)
            world.setBlockState(pos, blockState.with(LEVEL, 0), 3)
        }

        override fun setInvStack(index: Int, stack: ItemStack) {
            val blockState = world.getBlockState(pos)
            if (stack.item == WRAITH_CAGE_EMPTY) {
                world.setBlockState(pos, blockState.with(HAS_CAGE, CageState.EMPTY), 3)
                world.updateNeighbors(pos, blockState.block)
            }
            if (stack.item == Items.SOUL_SAND && blockState[HAS_CAGE] == CageState.EMPTY) {
               block.insertSand(world,blockState,pos)

            }

        }

        override fun removeInvStack(p0: Int): ItemStack {

            val blockState = world.getBlockState(pos)
            if (p0 == 1)
                return ItemStack.EMPTY

            return when (blockState[HAS_CAGE]) {
                CageState.EMPTY -> {
                    world.setBlockState(pos, blockState.with(HAS_CAGE, CageState.NO_CAGE), 3)

                    ItemStack(WRAITH_CAGE_EMPTY)
                }
                CageState.FULL -> {
                    world.setBlockState(pos, blockState.with(HAS_CAGE, CageState.NO_CAGE), 3)

                    ItemStack(WRAITH_CAGE)
                }
                else -> {
                    ItemStack.EMPTY
                }
            }

        }

        override fun canPlayerUseInv(p0: PlayerEntity?): Boolean {
            return false
        }

        override fun getInvAvailableSlots(p0: Direction): IntArray {
            return when (p0) {
                Direction.UP -> intArrayOf(0, 1)
                Direction.DOWN -> intArrayOf(0)
                else -> intArrayOf()
            }
        }

        override fun getInvSize(): Int {
            return 1
        }

        override fun canExtractInvStack(index: Int, stack: ItemStack, direction: Direction): Boolean {

            if (direction == Direction.DOWN) {
                return stack.item == WRAITH_CAGE_EMPTY || stack.item == WRAITH_CAGE
            }


            return false
        }

        override fun takeInvStack(p0: Int, p1: Int): ItemStack {

            if (p0 == 1)
                return ItemStack.EMPTY

            val blockState = world.getBlockState(pos)

            return when (blockState[HAS_CAGE]) {
                CageState.EMPTY -> {
                    world.setBlockState(pos, blockState.with(HAS_CAGE, CageState.NO_CAGE), 3)

                    ItemStack(WRAITH_CAGE_EMPTY)
                }
                CageState.FULL -> {
                    world.setBlockState(pos, blockState.with(HAS_CAGE, CageState.NO_CAGE), 3)

                    ItemStack(WRAITH_CAGE)
                }
                else -> {
                    ItemStack.EMPTY
                }
            }

        }

        override fun getInvMaxStackAmount(): Int {
            return 1
        }

        override fun isInvEmpty(): Boolean {
            val blockState = world.getBlockState(pos)

            return blockState[LEVEL] == 0 && blockState[HAS_CAGE] != CageState.NO_CAGE
        }

        override fun canInsertInvStack(index: Int, stack: ItemStack, direction: Direction?): Boolean {

            val blockState = world.getBlockState(pos)

            if (direction == Direction.UP) {
                return when {
                    //soul sand goes into slot 1.
                    stack.item == Items.SOUL_SAND -> blockState[HAS_CAGE] == CageState.EMPTY && world.getBlockState(pos.down()).block == Blocks.FIRE
                    //the cage is in slot 0, we can only insert if there is no cage.
                    stack.item == WRAITH_CAGE_EMPTY -> blockState[HAS_CAGE] == CageState.NO_CAGE
                    else -> false
                }
            }
            return false
        }
    }
}