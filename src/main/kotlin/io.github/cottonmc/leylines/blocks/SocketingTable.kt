package io.github.cottonmc.leylines.blocks

import io.github.cottonmc.leylines.Constants
import net.minecraft.block.Block
import net.minecraft.util.Identifier
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.Hand
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.block.BlockState




class SocketingTable(settings: Settings) : Block(settings) {

    companion object{
        val id= Identifier(Constants.modid,"soketing_table")
    }

    override fun activate(state: BlockState?, world: World, pos: BlockPos?, player: PlayerEntity?, hand: Hand?, hitResult: BlockHitResult?): Boolean {
        if (world.isClient) return true

            ContainerProviderRegistry.INSTANCE.openContainer(id, player) { buf -> buf.writeBlockPos(pos) }

        return true
    }

}