package io.github.cottonmc.leylines.blocks

import io.github.cottonmc.leylines.Constants
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World


/**
 * Base for blocks that open a gui on right click.
 * */
open class CraftingGuiBlock(settings: Settings, private val containerIdentifier: Identifier) : Block(settings) {


    override fun activate(state: BlockState?, world: World, pos: BlockPos?, player: PlayerEntity?, hand: Hand?, hitResult: BlockHitResult?): Boolean {
        if (world.isClient) return true

        ContainerProviderRegistry.INSTANCE.openContainer(containerIdentifier, player) { buf -> buf.writeBlockPos(pos) }

        return true
    }
}