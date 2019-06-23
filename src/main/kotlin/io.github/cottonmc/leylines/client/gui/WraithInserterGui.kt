package io.github.cottonmc.leylines.client.gui

import io.github.cottonmc.leylines.container.SocketingTableGuiController
import net.minecraft.entity.player.PlayerEntity
import io.github.cottonmc.cotton.gui.client.CottonScreen
import io.github.cottonmc.leylines.container.WraithInserterGuiController


class WraithInserterGui (controller: WraithInserterGuiController, player: PlayerEntity) : CottonScreen<WraithInserterGuiController>(controller,player){}