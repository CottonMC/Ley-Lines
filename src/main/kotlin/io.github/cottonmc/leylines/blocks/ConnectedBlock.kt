package io.github.cottonmc.leylines.blocks

import io.github.cottonmc.leylines.Constants.modid
import net.minecraft.block.Block
import net.minecraft.block.BlockRenderLayer
import net.minecraft.block.BlockState
import net.minecraft.state.StateFactory
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties.POWER
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.World

class ConnectedBlock(settings: Settings) : Block(settings) {

    companion object States {
        val UP = BooleanProperty.create("up")
        val DOWN = BooleanProperty.create("down")
        val EAST = BooleanProperty.create("east")
        val WEST = BooleanProperty.create("west")
        val NORTH = BooleanProperty.create("north")
        val SOUTH = BooleanProperty.create("south")
    }

    init {
        this.defaultState = stateFactory.defaultState
                .with(UP, false)
                .with(DOWN, false)
                .with(EAST, false)
                .with(WEST, false)
                .with(NORTH, false)
                .with(SOUTH, false)
    }

    override fun appendProperties(`stateFactory$Builder_1`: StateFactory.Builder<Block, BlockState>) {
        `stateFactory$Builder_1`.add(UP, DOWN, EAST, WEST, NORTH, SOUTH)
    }


    override fun getRenderLayer(): BlockRenderLayer {
        return BlockRenderLayer.SOLID
    }

    override fun neighborUpdate(blockState_1: BlockState, world: World, ownPos: BlockPos, block: Block, blockPos_2: BlockPos?, boolean_1: Boolean) {
        super.neighborUpdate(blockState_1, world, ownPos, block, blockPos_2, boolean_1)
        val stateManager = StateManager(blockState_1)

        val newState = stateManager.calculateDirectionState(ownPos.up(), world, UP)
                .calculateDirectionState(ownPos.down(), world, DOWN)
                .calculateDirectionState(ownPos.east(), world, EAST)
                .calculateDirectionState(ownPos.west(), world, WEST)
                .calculateDirectionState(ownPos.north(), world, NORTH)
                .calculateDirectionState(ownPos.south(), world, SOUTH)
                .state

        if(stateManager.dirty){
            world.setBlockState(ownPos, newState)
            world.updateNeighbors(ownPos,this)
        }
    }


    class StateManager(var state: BlockState) {
        var dirty = false
        fun calculateDirectionState(pos: BlockPos, world: IWorld, state: BooleanProperty): StateManager {
            val target = world.getBlockState(pos).block
            val sideState = this.state.get(state)
            if (target == this.state.block) {
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
    }
}