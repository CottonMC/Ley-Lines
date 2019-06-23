package io.github.cottonmc.leylines.blocks

import io.github.cottonmc.leylines.COOLED_LAVA
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.*
import net.minecraft.block.FrostedIceBlock.AGE
import net.minecraft.entity.EntityContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Property
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.BlockView
import net.minecraft.world.World
import org.jetbrains.annotations.NotNull
import java.util.*

open class MeltingBlockBase(settings: Settings, val replaces:Block) : Block(settings) {

    init {
        this.defaultState = (this.stateFactory.defaultState as BlockState).with(AGE, 0) as BlockState
    }

    override fun onScheduledTick(blockState_1: BlockState, world_1: World, blockPos_1: BlockPos, random_1: Random) {
        if ((random_1.nextInt(3) == 0 || this.canMelt(world_1, blockPos_1, 4)) && world_1.getLightLevel(blockPos_1) > 11 - blockState_1.get(AGE) as Int - blockState_1.getLightSubtracted(world_1, blockPos_1) && this.increaseAge(blockState_1, world_1, blockPos_1)) {
            val `blockPos$PooledMutable_1` = BlockPos.PooledMutable.get()
            var var6: Throwable? = null

            try {
                val var7 = Direction.values()
                val var8 = var7.size

                for (var9 in 0 until var8) {
                    val direction_1 = var7[var9]
                    `blockPos$PooledMutable_1`!!.method_10114(blockPos_1).method_10118(direction_1)
                    val blockState_2 = world_1.getBlockState(`blockPos$PooledMutable_1`)
                    if (blockState_2.block === this && !this.increaseAge(blockState_2, world_1, `blockPos$PooledMutable_1`)) {
                        world_1.blockTickScheduler.schedule(`blockPos$PooledMutable_1`, this, MathHelper.nextInt(random_1, 20, 40))
                    }
                }
            } catch (var19: Throwable) {
                var6 = var19
                throw var19
            } finally {
                if (`blockPos$PooledMutable_1` != null) {
                    if (var6 != null) {
                        try {
                            `blockPos$PooledMutable_1`.close()
                        } catch (var18: Throwable) {
                            var6.addSuppressed(var18)
                        }

                    } else {
                        `blockPos$PooledMutable_1`.close()
                    }
                }

            }

        } else {
            world_1.blockTickScheduler.schedule(blockPos_1, this, MathHelper.nextInt(random_1, 20, 40))
        }
    }

    private fun increaseAge(blockState_1: BlockState, world_1: World, blockPos_1: BlockPos): Boolean {
        val int_1 = blockState_1.get(AGE) as Int
        if (int_1 < 3) {
            world_1.setBlockState(blockPos_1, blockState_1.with(AGE, int_1 + 1) as BlockState, 2)
            return false
        } else {
            this.melt(blockState_1, world_1, blockPos_1)
            return true
        }
    }

    override fun neighborUpdate(blockState_1: BlockState, world_1: World?, blockPos_1: BlockPos, block_1: Block?, blockPos_2: BlockPos?, boolean_1: Boolean) {
        if (block_1 === this && this.canMelt(world_1, blockPos_1, 2)) {
            this.melt(blockState_1, world_1!!, blockPos_1)
        }

        super.neighborUpdate(blockState_1, world_1, blockPos_1, block_1, blockPos_2, boolean_1)
    }

    private fun canMelt(blockView_1: BlockView?, blockPos_1: BlockPos?, int_1: Int): Boolean {
        var int_2 = 0
        val `blockPos$PooledMutable_1` = BlockPos.PooledMutable.get()
        var var6: Throwable? = null

        try {
            val var7 = Direction.values()
            val var8 = var7.size

            for (var9 in 0 until var8) {
                val direction_1 = var7[var9]
                `blockPos$PooledMutable_1`!!.method_10114(blockPos_1!!).method_10118(direction_1)
                if (blockView_1!!.getBlockState(`blockPos$PooledMutable_1`).block === this) {
                    ++int_2
                    if (int_2 >= int_1) {
                        return false
                    }
                }
            }
        } catch (var21: Throwable) {
            var6 = var21
            throw var21
        } finally {
            if (`blockPos$PooledMutable_1` != null) {
                if (var6 != null) {
                    try {
                        `blockPos$PooledMutable_1`.close()
                    } catch (var20: Throwable) {
                        var6.addSuppressed(var20)
                    }

                } else {
                    `blockPos$PooledMutable_1`.close()
                }
            }

        }

        return true
    }

    override fun appendProperties(`stateFactory$Builder_1`: StateFactory.Builder<Block, BlockState>) {
        `stateFactory$Builder_1`.add(*arrayOf<Property<*>>(AGE))
    }

    @Environment(EnvType.CLIENT)
    override fun getPickStack(blockView_1: BlockView?, blockPos_1: BlockPos?, blockState_1: BlockState?): ItemStack {
        return ItemStack.EMPTY
    }

    protected fun melt(blockState_1: BlockState, world_1: World, blockPos_1: BlockPos) {

        world_1.setBlockState(blockPos_1, replaces.defaultState)
        world_1.updateNeighbor(blockPos_1, replaces, blockPos_1)

    }


}

fun coolLava(world_1: @NotNull World, blockPos_1: @NotNull BlockPos, int_1: Int, rand: @NotNull Random, pos: Vec3d) {
        val blockState_1 = COOLED_LAVA.defaultState
        val float_1 = Math.min(16, 2 + int_1).toFloat()
        val `blockPos$Mutable_1` = BlockPos.Mutable()

        for (blockPos_2 in BlockPos.iterate(blockPos_1.add((-float_1).toDouble(), -1.0, (-float_1).toDouble()), blockPos_1.add(float_1.toDouble(), -1.0, float_1.toDouble()))) {
            if (blockPos_2.isWithinDistance(pos, float_1.toDouble())) {
                `blockPos$Mutable_1`.set(blockPos_2.x, blockPos_2.y + 1, blockPos_2.z)
                val blockState_2 = world_1.getBlockState(`blockPos$Mutable_1`)
                if (blockState_2.isAir) {
                    val blockState_3 = world_1.getBlockState(blockPos_2)
                    if (blockState_3.material == Material.LAVA && blockState_3.get(FluidBlock.LEVEL) == 0 && blockState_1.canPlaceAt(world_1, blockPos_2) && world_1.canPlace(blockState_1, blockPos_2, EntityContext.absent())) {
                        world_1.setBlockState(blockPos_2, blockState_1)
                        world_1.blockTickScheduler.schedule(blockPos_2, COOLED_LAVA, MathHelper.nextInt(rand, 60, 120))
                    }
                }
            }
        }
}