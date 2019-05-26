package io.github.cottonmc.leylines.client.gui

import io.github.cottonmc.leylines.container.SocketingTableGuiController
import net.minecraft.entity.player.PlayerEntity
import io.github.cottonmc.cotton.gui.client.CottonScreen



class SocketingTableGui (controller: SocketingTableGuiController, player: PlayerEntity) : CottonScreen<SocketingTableGuiController>(controller,player){}