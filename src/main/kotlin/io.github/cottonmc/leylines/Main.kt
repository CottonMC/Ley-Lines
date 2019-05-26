package io.github.cottonmc.leylines

import WRAITH_CAGE
import WRAITH_CAGE_EMPTY
import io.github.cottonmc.leylines.Constants.modid
import io.github.cottonmc.leylines.blocks.RedstoneConduitBlock
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
import io.github.cottonmc.leylines.blocks.SocketingTableBlock
import io.github.cottonmc.leylines.blocks.WraithFabricatorBlock
import io.github.cottonmc.leylines.container.SocketingTableGuiController
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.container.BlockContext


object Main : ModInitializer {
    override fun onInitialize() {
        REDSTONE_CONDUIT =
                Registry.register(Registry.BLOCK, Identifier(modid, "redstone_conduit"), RedstoneConduitBlock(
                        FabricBlockSettings
                                .copy(Blocks.REDSTONE_BLOCK)
                                .breakByHand(true)
                                .build()))

        WRAITH_FABRICATOR =
                Registry.register(Registry.BLOCK, Identifier(modid, "wraith_fabricator"), WraithFabricatorBlock(
                        FabricBlockSettings
                                .copy(Blocks.COMPOSTER)
                                .breakByHand(true)
                                .build()))

        Registry.register(Registry.ITEM, Identifier(modid, "redstone_conduit"),
                BlockItem(REDSTONE_CONDUIT, Item.Settings().itemGroup(ItemGroup.REDSTONE)))

        Registry.register(Registry.ITEM, Identifier(modid, "wraith_fabricator"),
                BlockItem(WRAITH_FABRICATOR, Item.Settings().itemGroup(ItemGroup.MISC)))

        ColorProviderRegistry.BLOCK.register(BlockColorProvider { blockState: BlockState, _: ExtendedBlockView?, _: BlockPos?, _: Int ->
            val power = blockState[POWER]
            return@BlockColorProvider RedstoneWireBlock.getWireColor(power)
        }, REDSTONE_CONDUIT)

        ContainerProviderRegistry.INSTANCE.registerFactory(SocketingTableBlock.id) { syncId, _, player, buf ->
            SocketingTableGuiController(
                    syncId,
                    player.inventory,
                    BlockContext.create(player.world, buf.readBlockPos())
            )
        }

        SOCKETING_TABLE =
                Registry.register(Registry.BLOCK, SocketingTableBlock.id, SocketingTableBlock(
                        FabricBlockSettings
                                .copy(Blocks.CRAFTING_TABLE)
                                .breakByHand(true)
                                .build()))

        WRAITH_CAGE = Item(Item.Settings().itemGroup(ItemGroup.MISC))
        WRAITH_CAGE_EMPTY = Item(Item.Settings().itemGroup(ItemGroup.MISC))

        Registry.register(Registry.ITEM, SocketingTableBlock.id, BlockItem(SOCKETING_TABLE, Item.Settings().itemGroup(ItemGroup.DECORATIONS)))

        Registry.register(Registry.ITEM, Identifier(modid, "wraith_cage_empty"), WRAITH_CAGE_EMPTY)
        Registry.register(Registry.ITEM, Identifier(modid, "wraith_cage"), WRAITH_CAGE)

    }
}