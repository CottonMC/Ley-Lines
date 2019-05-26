package io.github.cottonmc.leylines.mixin;

import com.google.common.collect.Multimap;
import io.github.cottonmc.leylines.attributes.TagAttributesKt;
import io.github.cottonmc.leylines.container.Socketable;
import io.github.cottonmc.leylines.Constants;
import io.github.cottonmc.leylines.container.SocketableKt;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static io.github.cottonmc.leylines.Constants.Sockets.socketId;

@Mixin(ItemStack.class)
@Implements(@Interface(iface = Socketable.class, prefix = "leylines_sockets$", unique = true))
public abstract class ItemStackMixin //implements Socketable
{
    private List<EntityAttributeModifier> socketAttributeCache = new LinkedList<>();

    @Shadow
    private CompoundTag tag;

    @Shadow
    public abstract CompoundTag getOrCreateTag();

    @Shadow
    public abstract Item getItem();

    @Inject(
            at = @At("RETURN"),
            method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V"
    )
    private void init(CompoundTag compoundTag_1, CallbackInfo ci) {

    }


    @Inject(
            at = @At("RETURN"),
            method = "getAttributeModifiers"
    )
    private void attributes(EquipmentSlot equipmentSlot_1, CallbackInfoReturnable<Multimap<String, EntityAttributeModifier>> cir) {
        Multimap<String, EntityAttributeModifier> returnValue = cir.getReturnValue();

        //if the cahce needs to be rebuild, we rebuild it.
        if (socketAttributeCache.isEmpty()) {
            CompoundTag tag = getOrCreateTag();
            if (tag.containsKey(Constants.Sockets.ID.toString())) {
                ListTag tagList = tag.getList(socketId, 8);
                for (Tag socketItem : tagList) {
                    String[] rawId = socketItem.asString().split(":");
                    try {
                        socketAttributeCache.addAll(TagAttributesKt.getAttributesForItem(new Identifier(rawId[0], rawId[1])));
                    } catch (NullPointerException ignored) {

                    }
                }
            }

            for (EntityAttributeModifier entityAttributeModifier : socketAttributeCache) {

            }
        }

        for (EntityAttributeModifier entityAttributeModifier : socketAttributeCache) {
            returnValue.put(entityAttributeModifier.getName(), entityAttributeModifier);
        }

    }
    @Inject(
            at = @At("RETURN"),
            method = "getTooltipText"
    )
    private void tooltip(PlayerEntity playerEntity_1, TooltipContext tooltipContext, CallbackInfoReturnable<List<Component>> cir) {
        List<Component> componentList = cir.getReturnValue();

        CompoundTag tag = getOrCreateTag();
        if (tag.containsKey(Constants.Sockets.ID.toString())) {
            ListTag tagList = tag.getList(socketId, 8);
            componentList.add(new TranslatableComponent(Constants.modid + ".tooltip.sockets"));
            for (Tag socketItem : tagList) {
                String[] rawId = socketItem.asString().split(":");
                componentList.add(new TranslatableComponent("item." + rawId[0] + "." + rawId[1]));
            }
        }

    }

    public boolean leylines_sockets$isSocketed() {
        return !tag.getList(Constants.Sockets.ID.toString(), 8).isEmpty();
    }


    public boolean leylines_sockets$addtoSocket(@NotNull Identifier identifier) {
        CompoundTag tag = getOrCreateTag();
        if (tag.containsKey(socketId)) {
            int socketCount = SocketableKt.getSocketCount(getItem());

            ListTag socketList = tag.getList(socketId, 8);
            if (socketList.size() < socketCount) {
                socketList.add(new StringTag(identifier.toString()));
                socketAttributeCache.clear();
                return true;
            }
        } else {
            ListTag socketList = new ListTag();
            socketList.add(new StringTag(identifier.toString()));
            tag.put(socketId, socketList);
            socketAttributeCache.clear();

            return true;
        }

        return false;
    }

    @NotNull
    public List<Identifier> leylines_sockets$getSocketItems() {
        if (!this.tag.containsKey(socketId)) {
            return Collections.emptyList();
        }
        ListTag list = tag.getList(socketId, 8);

        ArrayList<Identifier> result = new ArrayList<>();

        list.forEach(tag -> {
            String[] id = tag.asString().split(":");
            result.add(new Identifier(id[0], id[1]));
        });

        return result;
    }

    public boolean leylines_sockets$removeFromSocket(@NotNull Identifier identifier) {

        CompoundTag tag = getOrCreateTag();
        if (tag.containsKey(socketId)) {

            ListTag socketList = tag.getList(socketId, 8);
            for (int i = 0; i < socketList.size(); i++) {
                String string = socketList.getString(i);
                if (string.equals(identifier.toString())) {
                    socketList.remove(i);
                    socketAttributeCache.clear();

                    return true;
                }
            }
        }
        return false;
    }
}


