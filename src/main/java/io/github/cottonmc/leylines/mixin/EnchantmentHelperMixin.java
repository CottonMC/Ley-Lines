package io.github.cottonmc.leylines.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.InfoEnchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @Inject(
            at = @At("HEAD"),
            cancellable = true,
            method = "getEnchantments(Ljava/util/Random;Lnet/minecraft/item/ItemStack;IZ)Ljava/util/List;"
    )
    private static void getEnchantments(Random random_1, ItemStack itemStack_1, int int_1, boolean boolean_1, CallbackInfoReturnable<List<InfoEnchantment>> cir){

    }
}
