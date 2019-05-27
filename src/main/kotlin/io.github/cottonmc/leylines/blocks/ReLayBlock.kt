package io.github.cottonmc.leylines.blocks

import net.minecraft.block.Block
import net.minecraft.block.BlockRenderLayer
import net.minecraft.block.BlockState
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction

class ReLayBlock(settings:Settings):Block(settings) {

    companion object{
        val FACING = Properties.FACING
    }

    init{
        defaultState = stateFactory.defaultState
                .with(FACING,Direction.DOWN)
    }

    override fun appendProperties(`stateFactory$Builder_1`: StateFactory.Builder<Block, BlockState>) {
       `stateFactory$Builder_1`.add(FACING)
    }

    override fun getRenderLayer(): BlockRenderLayer {
        return BlockRenderLayer.CUTOUT
    }
}