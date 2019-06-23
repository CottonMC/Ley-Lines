package io.github.cottonmc.leylines.leyenergy

import net.minecraft.block.BlockState
import net.minecraft.inventory.SidedInventory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld

interface LeyEnergyProvider {

    fun getLeyEnergy(blockState: BlockState, iWorld: IWorld, pos: BlockPos): LeyEnergy
}