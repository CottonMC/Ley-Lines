package io.github.cottonmc.leylines.mixin;

import io.github.cottonmc.leylines.energy.LeyEnergy;
import io.github.cottonmc.leylines.energy.LeyEnergyProvider;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(World.class)
@Implements(@Interface(iface = LeyEnergyProvider.class, prefix = "leylines_energy$"))
public class WorldLeyEnergyProviderMixin //implements LeyEnergyProvider
{

    @NotNull
    public LeyEnergy leylines_energy$getInventory(@NotNull BlockState blockState, @NotNull IWorld world, @NotNull BlockPos blockPos) {
        return LeyEnergy.CREATIVE;
    }
}
