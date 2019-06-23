package io.github.cottonmc.leylines.energy

import net.minecraft.client.util.math.Vector3f
import net.minecraft.world.World

interface LeyEnergyMedium {

    val energy:LeyEnergy
    val position:Vector3f

    /**
     * Carries the effect. Spawn an entity, affect entities in the area, do what needs to be done.
     * */
    fun cast(world:World)
}