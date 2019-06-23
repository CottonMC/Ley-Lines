package io.github.cottonmc.leylines.mixin;

import com.google.common.base.Objects;
import io.github.cottonmc.leylines.BlocksKt;
import io.github.cottonmc.leylines.enchantment.EnchantmentsKt;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AbstractEntityAttributeContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static io.github.cottonmc.leylines.blocks.MeltingBlockBaseKt.coolLava;


/**
 * add 2 new attributes.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    private Random rand = new Random();

    @Shadow
    private BlockPos lastBlockPos;

    @Shadow
    private AbstractEntityAttributeContainer attributeContainer;

    public LivingEntityMixin(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Inject(
            method = "initAttributes",
            at = @At("TAIL")
    )
    private void initAttributes(CallbackInfo ci) {

    }

    @Inject(
            at = @At("HEAD"),
            method = "baseTick"
    )
    void baseTick(CallbackInfo ci) {
        if (lastBlockPos != null)
            if (!world.isClient) {
                BlockPos blockPos_1 = new BlockPos(this);
                if (!Objects.equal(this.lastBlockPos, blockPos_1)) {
                    int int_1 = EnchantmentHelper.getEquipmentLevel(EnchantmentsKt.getFLAMEWALKER(), (LivingEntity) (Object) this);
                    if (int_1 > 0) {
                        if (onGround) {
                            coolLava(world, lastBlockPos, 1, rand, getPos());
                        }
                    }
                }
            }
    }
}
