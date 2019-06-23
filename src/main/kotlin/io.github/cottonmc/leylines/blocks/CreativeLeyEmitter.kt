package io.github.cottonmc.leylines.blocks

import io.github.cottonmc.leylines.leyenergy.LeyEnergy
import io.github.cottonmc.leylines.leyenergy.LeyEnergyProvider
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld

class CreativeLeyEmitter : Block(Settings.copy(Blocks.BEDROCK)), LeyEnergyProvider {

    override fun getLeyEnergy(blockState: BlockState, iWorld: IWorld, pos: BlockPos): LeyEnergy {
        return CreativeLeyEnergy()
    }


    class CreativeLeyEnergy : LeyEnergy


}