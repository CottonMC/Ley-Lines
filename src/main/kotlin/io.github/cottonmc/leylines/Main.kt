package io.github.cottonmc.leylines

import io.github.cottonmc.leylines.Constants.modid
import io.github.cottonmc.leylines.Constants.socketingTableIdentifier
import io.github.cottonmc.leylines.Constants.wraithInserterIdentifier
import io.github.cottonmc.leylines.blocks.*
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
import io.github.cottonmc.leylines.container.SocketingTableGuiController
import io.github.cottonmc.leylines.container.WraithInserterGuiController
import io.github.cottonmc.leylines.enchantment.*
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.Block
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
                                .copy(Blocks.CAULDRON)
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

        ContainerProviderRegistry.INSTANCE.registerFactory(socketingTableIdentifier) { syncId, _, player, buf ->
            SocketingTableGuiController(
                    syncId,
                    player.inventory,
                    BlockContext.create(player.world, buf.readBlockPos())
            )
        }

        SOCKETING_TABLE =
                Registry.register(Registry.BLOCK, socketingTableIdentifier, CraftingGuiBlock(
                        FabricBlockSettings
                                .copy(Blocks.CRAFTING_TABLE)
                                .breakByHand(true)
                                .build(),
                        socketingTableIdentifier
                ))

        RELEY = Registry.register(Registry.BLOCK, Identifier(modid, "reley"), ReLayBlock(
                FabricBlockSettings
                        .copy(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE)
                        .breakByHand(true)
                        .lightLevel(15)
                        .build()
        ))

        COOLED_LAVA = Registry.register(Registry.BLOCK, Identifier(modid, "cooled_lava"), CooledLavaBlock())

        WRAITH_INSERTER = Registry.register(Registry.BLOCK, wraithInserterIdentifier, CraftingGuiBlock(
                FabricBlockSettings
                        .copy(Blocks.CRAFTING_TABLE)
                        .breakByHand(true)
                        .build(),
                wraithInserterIdentifier
        ))
        Registry.register(Registry.ITEM, wraithInserterIdentifier, BlockItem(WRAITH_INSERTER, Item.Settings().itemGroup(ItemGroup.DECORATIONS)))

        ContainerProviderRegistry.INSTANCE.registerFactory(wraithInserterIdentifier) { syncId, _, player, buf ->
            WraithInserterGuiController(
                    syncId,
                    player.inventory,
                    BlockContext.create(player.world, buf.readBlockPos())
            )
        }

        Registry.register(Registry.ITEM, Identifier(modid, "reley"), BlockItem(RELEY, Item.Settings().itemGroup(ItemGroup.DECORATIONS)))

        WRAITH_CAGE = Item(Item.Settings().itemGroup(ItemGroup.MISC))
        WRAITH_CAGE_EMPTY = Item(Item.Settings().itemGroup(ItemGroup.MISC))

        Registry.register(Registry.ITEM, socketingTableIdentifier, BlockItem(SOCKETING_TABLE, Item.Settings().itemGroup(ItemGroup.DECORATIONS)))

        Registry.register(Registry.ITEM, Identifier(modid, "wraith_cage_empty"), WRAITH_CAGE_EMPTY)
        Registry.register(Registry.ITEM, Identifier(modid, "wraith_cage"), WRAITH_CAGE)

        FLAMEWALKER = registerEnchantment(Identifier(modid, "flamewalker"), FlameWalkerEnchant())
        TOES_OF_THE_VOID = registerEnchantment(Identifier(modid, "toesofthevoid"), ToesOfTheVoid())
        DETERIORATE = registerEnchantment(Identifier(modid, "deteriorate"), Deteriorate())

        CONNECTED_BLOCK_TEST = Registry.register(Registry.BLOCK, Identifier(modid,"connected_block_test"), ConnectedBlock(
        FabricBlockSettings
                .copy(Blocks.CRAFTING_TABLE)
                .build()
        ))
        Registry.register(Registry.ITEM, Identifier(modid,"connected_block_test"), BlockItem(CONNECTED_BLOCK_TEST, Item.Settings().itemGroup(ItemGroup.DECORATIONS)))

    }

}