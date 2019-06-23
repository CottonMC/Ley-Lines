package io.github.cottonmc.leylines.upgrades

import io.github.cottonmc.leylines.Constants
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag

/**
 * Handles the wraith tool upgrades
 * */
object ToolUpgradeWraithManager{

    fun insertWraithInto(stack:ItemStack){
        val compoundTag = stack.orCreateTag
        if(!compoundTag.containsKey(Constants.WraithUpgrade.tagID)){
            val wraithTag = CompoundTag()
            wraithTag.putLong("exp",0)
            //multiply it by 100 to get the actual cap.
            wraithTag.putInt("capValue",30)

            compoundTag.put(Constants.WraithUpgrade.tagID,wraithTag)
        }
    }
}