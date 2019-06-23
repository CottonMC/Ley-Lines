package io.github.cottonmc.leylines.blocks

import io.github.cottonmc.leylines.convertToIntColor
import io.github.cottonmc.leylines.energy.LeyEnergy
import io.github.cottonmc.leylines.isFullOpaque
import net.minecraft.block.Block
import net.minecraft.block.BlockRenderLayer
import net.minecraft.block.BlockState
import net.minecraft.block.FacingBlock
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateFactory
import net.minecraft.state.property.IntegerProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.IWorld
import net.minecraft.world.ViewableWorld
import net.minecraft.world.World

/**
 * Transfers typed ley energy.
 *
 */
class ReLayBlock(settings: Settings,val type:Identifier = LeyEnergy.CREATIVE.getType()) : Block(settings) {

    companion object {
        /**
         * decreases by the distance the energy travels.
         * */
        val LEVEL = IntegerProperty.create("level", 0, 4)
        val FACING = Properties.FACING

        /**
         * Color overlays for the different power levels.
         * it has no recoloring on level 0, for aesthetic reasons.
         * */
        val levelColors = intArrayOf(
                convertToIntColor(255, 255, 255),
                convertToIntColor(56, 255, 75),
                convertToIntColor(37, 186, 52),
                convertToIntColor(28, 147, 40),
                convertToIntColor(19, 107, 28)
        )
    }

    init {
        defaultState = stateFactory.defaultState
                .with(FACING, Direction.DOWN)
    }

    override fun appendProperties(`stateFactory$Builder_1`: StateFactory.Builder<Block, BlockState>) {
        `stateFactory$Builder_1`.add(FACING, LEVEL)
    }

    override fun getRenderLayer(): BlockRenderLayer {
        return BlockRenderLayer.CUTOUT
    }

    /**
     * if the block that it's attached to is broken, than it breaks.
     * */
    override fun neighborUpdate(blockState_1: BlockState, world_1: World, blockPos_1: BlockPos, block_1: Block, blockPos_2: BlockPos, boolean_1: Boolean) {
        val direction = blockState_1[FACING]
        if (blockPos_2 == blockPos_1.offset(direction)) {
            world_1.breakBlock(blockPos_1, true)
        }
    }

    /**
     * if there are no opaque blocks around, than it can not be placed.
     * */
    override fun canPlaceAt(blockState_1: BlockState?, world: ViewableWorld, blockPos: BlockPos): Boolean {

        return when {
            world.isFullOpaque(blockPos.offset(Direction.DOWN)) -> true
            world.isFullOpaque(blockPos.offset(Direction.UP)) -> true
            world.isFullOpaque(blockPos.offset(Direction.EAST)) -> true
            world.isFullOpaque(blockPos.offset(Direction.WEST)) -> true
            world.isFullOpaque(blockPos.offset(Direction.NORTH)) -> true
            world.isFullOpaque(blockPos.offset(Direction.SOUTH)) -> true
            else -> false
        }
    }

    /**
     * Always faces the block that the player is facing, if it's air, than it will search for the first available block to connect to. There will always be one, as you can't place it on air.
     * */
    override fun getPlacementState(itemPlacementContext: ItemPlacementContext): BlockState? {

        var expectedFacing = itemPlacementContext.playerFacing
        val blockPos = itemPlacementContext.blockPos
        val world = itemPlacementContext.world

        if (!world.isFullOpaque(blockPos.offset(expectedFacing))) {
            expectedFacing = when {
                world.isFullOpaque(blockPos.offset(Direction.DOWN)) -> Direction.DOWN
                world.isFullOpaque(blockPos.offset(Direction.EAST)) -> Direction.EAST
                world.isFullOpaque(blockPos.offset(Direction.WEST)) -> Direction.WEST
                world.isFullOpaque(blockPos.offset(Direction.NORTH)) -> Direction.NORTH
                world.isFullOpaque(blockPos.offset(Direction.SOUTH)) -> Direction.SOUTH
                else -> Direction.UP
            }
        }

        return (this.defaultState.with(FacingBlock.FACING, expectedFacing) as BlockState)
    }
}