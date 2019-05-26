package io.github.cottonmc.leylines.blocks

import io.github.cottonmc.leylines.Constants.modid
import net.minecraft.block.Block
import net.minecraft.block.BlockRenderLayer
import net.minecraft.block.BlockState
import net.minecraft.state.StateFactory
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties.POWER
import net.minecraft.tag.BlockTags
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.IWorld
import net.minecraft.world.World

class RedstoneConduitBlock(settings: Settings) : Block(settings) {

    companion object States {
        val UP = BooleanProperty.create("up")
        val DOWN = BooleanProperty.create("down")
        val EAST = BooleanProperty.create("east")
        val WEST = BooleanProperty.create("west")
        val NORTH = BooleanProperty.create("north")
        val SOUTH = BooleanProperty.create("south")

        val connectionTag = Identifier(modid, "redstone_conduit_connection")
    }

    init {
        this.defaultState = stateFactory.defaultState
                .with(UP, false)
                .with(DOWN, false)
                .with(EAST, false)
                .with(WEST, false)
                .with(NORTH, false)
                .with(SOUTH, false)
                .with(POWER, 0)
    }

    override fun appendProperties(`stateFactory$Builder_1`: StateFactory.Builder<Block, BlockState>) {
        `stateFactory$Builder_1`.add(UP, DOWN, EAST, WEST, NORTH, SOUTH, POWER)
    }


    override fun getRenderLayer(): BlockRenderLayer {
        return BlockRenderLayer.CUTOUT
    }

    override fun neighborUpdate(blockState_1: BlockState, world: World, ownPos: BlockPos, block: Block, blockPos_2: BlockPos?, boolean_1: Boolean) {
        super.neighborUpdate(blockState_1, world, ownPos, block, blockPos_2, boolean_1)
        val powerManager = PowerManager(blockState_1)

        val newState = powerManager.calculateDirectionState(ownPos.up(), world, UP)
                .calculateDirectionState(ownPos.down(), world, DOWN)
                .calculateDirectionState(ownPos.east(), world, EAST)
                .calculateDirectionState(ownPos.west(), world, WEST)
                .calculateDirectionState(ownPos.north(), world, NORTH)
                .calculateDirectionState(ownPos.south(), world, SOUTH)
                .calculatePowerState(ownPos.up(), world, UP)
                .state

        if(powerManager.dirty){
            world.setBlockState(ownPos, newState)
            world.updateNeighbors(ownPos,this)
        }
    }

    override fun getStrongRedstonePower(blockState_1: BlockState, blockView_1: BlockView?, blockPos_1: BlockPos?, direction_1: Direction?): Int {
        return 0
    }

    override fun getWeakRedstonePower(blockState_1: BlockState, blockView_1: BlockView?, blockPos_1: BlockPos?, direction_1: Direction?): Int {
        return blockState_1.get(POWER)
    }

    override fun emitsRedstonePower(blockState_1: BlockState?): Boolean {
        return true
    }


    class PowerManager(var state: BlockState) {
        var dirty = false
        fun calculateDirectionState(pos: BlockPos, world: IWorld, state: BooleanProperty): PowerManager {
            val tags = BlockTags.getContainer().getTagsFor(world.getBlockState(pos).block)
            val sideState = this.state.get(state)
            if (tags.contains(connectionTag)) {
                if (!sideState && !dirty)
                    dirty = true

                this.state = this.state.with(state, true)
            } else {
                if (sideState && !dirty)
                    dirty = true
                this.state = this.state.with(state, false)
            }
            return this
        }

        fun calculatePowerState(pos: BlockPos, world: World, state: BooleanProperty): PowerManager {
            if (!this.state.get(state)) return this

            val recievedPower = world.getReceivedRedstonePower(pos)
            val power = this.state.get(POWER)
            if (recievedPower > power) {
                if (power != recievedPower - 1)
                    dirty = true;

                this.state = this.state.with(POWER, recievedPower - 1)
            } else {
                if (recievedPower == 0)
                    if (power == 0)
                        dirty = true;
                    this.state = this.state.with(POWER, 0)

            }
            return this
        }

    }
}