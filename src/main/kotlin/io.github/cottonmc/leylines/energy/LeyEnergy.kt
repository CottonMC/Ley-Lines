package io.github.cottonmc.leylines.energy

import io.github.cottonmc.leylines.Constants
import net.minecraft.util.Identifier

/**
 * this object returns the state of ley energy in a given area.
 * */
interface LeyEnergy {
    companion object {
        @JvmField
        val CREATIVE: LeyEnergy = DefaultLeyEnergy()
    }

    private class DefaultLeyEnergy : LeyEnergy {

        override fun getType(): Identifier {
            return Identifier(Constants.modid, "normal")
        }

        override fun getStrength(): Int {
            return 100
        }
    }

    /**
     * Identifies the kind of energy.
     * */
    fun getType(): Identifier
    /**
     * A strength value, that will determine the speed of the target machine.
     * */
    fun getStrength(): Int
}

