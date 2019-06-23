package io.github.cottonmc.leylines

import net.minecraft.util.math.BlockPos
import net.minecraft.world.ViewableWorld

fun ViewableWorld.isFullOpaque(blockPos:BlockPos): Boolean {
    return this.getBlockState(blockPos).isFullOpaque(this,blockPos)
}