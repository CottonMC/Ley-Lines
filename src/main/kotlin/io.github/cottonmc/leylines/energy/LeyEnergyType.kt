package io.github.cottonmc.leylines.energy

import net.minecraft.client.util.math.Vector3f
import net.minecraft.entity.Entity
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.EntityView
import net.minecraft.world.World

/**
 * Describes the info that is related to each energy types
 * */
interface LeyEnergyType {

    val id: Identifier
    val color:Int

    /**
     * The method that is called when the energy interacts with the world
     * */
    fun blockMethod(pos: BlockPos,world :BlockView)

    /**
     * The method that is called when the energy interacts with an entity
     * */
    fun entityMethod(pos:Vector3f,world:EntityView)

    /**
     * The way the effect is applied to the world.
     * */
    fun getMedium(world: World):LeyEnergyMedium

}