package io.github.cottonmc.leylines

import io.github.cottonmc.leylines.blocks.SocketingTableBlock
import io.github.cottonmc.leylines.client.gui.SocketingTableGui
import io.github.cottonmc.leylines.container.SocketingTableGuiController
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry
import net.minecraft.container.BlockContext


object ClientInitializer : ClientModInitializer {
    override fun onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(SocketingTableBlock.id) { syncId, identifier, player, buf ->
            SocketingTableGui(
                    SocketingTableGuiController(
                            syncId,
                            player.inventory,
                            BlockContext.create(player.world, buf.readBlockPos())
                    ),
                    player
            )
        }
    }
}