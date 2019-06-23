package io.github.cottonmc.leylines.energy

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld

/**
 * Any block that wants to provide ley energy, should implement this interface.
 * */
interface LeyEnergyProvider {

    fun getInventory(blockState: BlockState, world: IWorld, blockPos: BlockPos): LeyEnergy

}