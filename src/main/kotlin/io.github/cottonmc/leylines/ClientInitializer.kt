package io.github.cottonmc.leylines

import io.github.cottonmc.leylines.blocks.SocketingTable
import io.github.cottonmc.leylines.client.gui.SocketingTableGui
import io.github.cottonmc.leylines.blocks.container.SocketingTableGuiController
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry


object ClientInitializer:ClientModInitializer {
    override fun onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(SocketingTable.id) { syncId, identifier, player, buf -> SocketingTableGui(SocketingTableGuiController(syncId, player.inventory), player) }
    }
}