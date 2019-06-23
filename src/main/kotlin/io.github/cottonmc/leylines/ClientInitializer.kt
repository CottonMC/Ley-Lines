package io.github.cottonmc.leylines

import io.github.cottonmc.leylines.blocks.ReLayBlock
import io.github.cottonmc.leylines.client.gui.SocketingTableGui
import io.github.cottonmc.leylines.client.gui.WraithInserterGui
import io.github.cottonmc.leylines.container.SocketingTableGuiController
import io.github.cottonmc.leylines.container.WraithInserterGuiController
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry
import net.minecraft.block.BlockState
import net.minecraft.client.color.block.BlockColorProvider
import net.minecraft.container.BlockContext
import net.minecraft.util.math.BlockPos
import net.minecraft.world.ExtendedBlockView


object ClientInitializer : ClientModInitializer {

    override fun onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(Constants.socketingTableIdentifier) { syncId, _, player, buf ->
            SocketingTableGui(
                    SocketingTableGuiController(
                            syncId,
                            player.inventory,
                            BlockContext.create(player.world, buf.readBlockPos())
                    ),
                    player
            )
        }

        ScreenProviderRegistry.INSTANCE.registerFactory(Constants.wraithInserterIdentifier) { syncId, _, player, buf ->
            WraithInserterGui(
                    WraithInserterGuiController(
                            syncId,
                            player.inventory,
                            BlockContext.create(player.world, buf.readBlockPos())
                    ),
                    player
            )
        }

        ColorProviderRegistry.BLOCK.register(
                BlockColorProvider { blockState: BlockState, extendedBlockView: ExtendedBlockView?, blockPos: BlockPos?, i: Int ->
                    val level = blockState.get(ReLayBlock.LEVEL)
                    ReLayBlock.levelColors[level]
                },
                RELEY
        )
    }
}