package io.github.cottonmc.leylines

import io.github.cottonmc.cotton.logging.ModLogger
import net.minecraft.util.Identifier


object Constants {

    const val modid = "leylines"

    @JvmField
    var logger = ModLogger(modid)

    @JvmField
    val socketingTableIdentifier= Identifier(modid,"soketing_table")


    @JvmField
    val wraithInserterIdentifier = Identifier(modid,"wraith_inserter")

    /**
     * Stores the tags that mark specific socket counts.
     * */
    object Sockets {

        @JvmField
        val ID = Identifier(modid, "sockets")
        @JvmField
        var tagID = Constants.Sockets.ID.toString()

        @JvmField
        val ONE_SOCKET_TAG = Identifier(modid, "socket_count_1")
        @JvmField
        val TWO_SOCKET_TAG = Identifier(modid, "socket_count_2")
        @JvmField
        val THREE_SOCKET_TAG = Identifier(modid, "socket_count_3")
        @JvmField
        val FOUR_SOCKET_TAG = Identifier(modid, "socket_count_4")
        @JvmField
        val FIVE_SOCKET_TAG = Identifier(modid, "socket_count_5")
        @JvmField
        val SIX_SOCKET_TAG = Identifier(modid, "socket_count_6")

        @JvmField
        val socketCounts = arrayOf(
                ONE_SOCKET_TAG,
                TWO_SOCKET_TAG,
                THREE_SOCKET_TAG,
                FOUR_SOCKET_TAG,
                FIVE_SOCKET_TAG,
                SIX_SOCKET_TAG
        )

        /**
         * Returns the socket count from a tag as an integer.
         * */
        fun getSocketCountInt(tag: Identifier): Int {
            if (socketCounts.contains(tag))
                return socketCounts.indexOf(tag)+1
            return 0
        }
    }

    object WraithUpgrade{
        @JvmField
        val ID = Identifier(modid, "wraith")
        @JvmField
        var tagID = Constants.WraithUpgrade.ID.toString()
    }
}