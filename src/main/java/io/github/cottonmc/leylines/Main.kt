package io.github.cottonmc.leylines

import io.github.cottonmc.leylines.blocks.RedstoneConduit
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.RedstoneWireBlock
import net.minecraft.client.color.block.BlockColorProvider
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.state.property.Properties.POWER
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.ExtendedBlockView
import io.github.cottonmc.leylines.blocks.SocketingTable
import io.github.cottonmc.leylines.blocks.container.SocketingTableGuiController
import net.fabricmc.fabric.api.container.ContainerProviderRegistry



object Main : ModInitializer {
    override fun onInitialize() {
        REDSTONE_CONDUIT =
                Registry.register(Registry.BLOCK, Identifier(Constants.modid, "redstone_conduit"), RedstoneConduit(
                        FabricBlockSettings
                                .copy(Blocks.REDSTONE_BLOCK)
                                .breakByHand(true)
                                .build()))

        Registry.register(Registry.ITEM, Identifier(Constants.modid, "redstone_conduit"),
                BlockItem(REDSTONE_CONDUIT, Item.Settings().itemGroup(ItemGroup.REDSTONE)))

        ColorProviderRegistry.BLOCK.register(BlockColorProvider {
            blockState: BlockState, extendedBlockView: ExtendedBlockView?, blockPos: BlockPos?, i: Int ->
            val power = blockState[POWER]
            return@BlockColorProvider RedstoneWireBlock.getWireColor(power)

        }, REDSTONE_CONDUIT)

        ContainerProviderRegistry.INSTANCE.registerFactory(SocketingTable.id) { syncId, id, player, buf -> SocketingTableGuiController(syncId, player.inventory) }

        SOCKETING_TABLE =
                Registry.register(Registry.BLOCK, SocketingTable.id, SocketingTable(
                        FabricBlockSettings
                                .copy(Blocks.CRAFTING_TABLE)
                                .breakByHand(true)
                                .build()))

        Registry.register(Registry.ITEM, SocketingTable.id,BlockItem(SOCKETING_TABLE,Item.Settings().itemGroup(ItemGroup.DECORATIONS)))

    }
}