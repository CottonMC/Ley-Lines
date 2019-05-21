package io.github.cottonmc.leylines.mixin;

import com.google.common.collect.Multimap;
import io.github.cottonmc.leylines.attributes.UpgradeContainer;
import io.github.cottonmc.leylines.blocks.container.Socketable;
import io.github.cottonmc.leylines.inventory.ResizeableInventory;
import io.github.cottonmc.leylines.Constants;
import io.github.cottonmc.leylines.attributes.UpgradeContainer;
import io.github.cottonmc.leylines.blocks.container.Socketable;
import io.github.cottonmc.leylines.inventory.ResizeableInventory;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static io.github.cottonmc.leylines.Constants.Sockets.ID;

@Mixin(ItemStack.class)
@Implements(@Interface(iface= Socketable.class,prefix = "leylines_sockets$",unique = true))
public abstract class ItemStackMixin //implements Socketable
{

    private ResizeableInventory socketInventory = new ResizeableInventory();

    @Inject(
            at = @At("RETURN"),
            method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V"
    )
    private void init(CompoundTag compoundTag_1, CallbackInfo ci) {
        CompoundTag sockets = compoundTag_1.getCompound(Constants.Sockets.ID.toString());
        socketInventory.createFromTag(sockets);
    }

    @Inject(
            at = @At("RETURN"),
            method = "toTag"
    )
    private void save(CompoundTag compoundTag, CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag sockets = socketInventory.toTag(new CompoundTag());
        compoundTag.put(Constants.Sockets.ID.toString(), sockets);
    }

    @Inject(
            at = @At("RETURN"),
            method = "getAttributeModifiers"
    )
    private void attributes(EquipmentSlot equipmentSlot_1, CallbackInfoReturnable<Multimap<String, EntityAttributeModifier>> cir) {
        Multimap<String, EntityAttributeModifier> returnValue = cir.getReturnValue();

        for (ItemStack itemStack : socketInventory) {

            List<EntityAttributeModifier> storedAttributes = ((UpgradeContainer) itemStack.getItem()).getStoredAttributes();

            for (EntityAttributeModifier storedAttribute : storedAttributes) {

                returnValue.put(storedAttribute.getName(), storedAttribute);
            }
        }
    }

    public boolean leylines_sockets$isSocketed() {
        return !socketInventory.isInvEmpty();
    }

    @NotNull
    public Inventory leylines_sockets$getSocketInventory(int size) {
        socketInventory.resize(size);
        return socketInventory;
    }


    @NotNull
    public Inventory leylines_sockets$getSocketInventory() {
        return socketInventory;
    }
}


