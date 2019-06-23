package io.github.cottonmc.leylines.mixin;

import io.github.cottonmc.leylines.container.Socketable;
import io.github.cottonmc.leylines.Constants;
import io.github.cottonmc.leylines.container.SocketableKt;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

import static io.github.cottonmc.leylines.Constants.Sockets.tagID;

/**
 * added methods to manage a few data on stacks.
 * <p>
 * The irreplacable part is the added lore.
 */
@Mixin(ItemStack.class)
@Implements(@Interface(iface = Socketable.class, prefix = "leylines_sockets$", unique = true))
public abstract class ItemStackMixin //implements Socketable
{

    @Shadow
    private CompoundTag tag;

    @Shadow
    public abstract CompoundTag getOrCreateTag();

    @Shadow
    public abstract Item getItem();

    @Inject(
            at = @At("RETURN"),
            method = "getTooltipText"
    )
    private void tooltip(PlayerEntity playerEntity_1, TooltipContext tooltipContext, CallbackInfoReturnable<List<Component>> cir) {
        List<Component> componentList = cir.getReturnValue();

        //add the socket info to the lore
        CompoundTag tag = getOrCreateTag();
        if (tag.containsKey(Constants.Sockets.tagID)) {
            ListTag tagList = tag.getList(tagID, 8);
            componentList.add(new TranslatableComponent(Constants.modid + ".tooltip.sockets"));
            for (Tag socketItem : tagList) {
                String[] rawId = socketItem.asString().split(":");
                componentList.add(new TranslatableComponent("item." + rawId[0] + "." + rawId[1]));
            }
        }
        //add wraith info to the lore
        if (tag.containsKey(Constants.WraithUpgrade.tagID)) {
            CompoundTag wraith = tag.getCompound(Constants.WraithUpgrade.tagID);
            componentList.add(new TranslatableComponent(Constants.modid + ".tooltip.wraith"));

            long exp = wraith.getLong("exp");
            int capValue = wraith.getInt("capValue") * 100;

            componentList.add(new TextComponent("lv:" + exp + "/" + capValue));
        }

    }

    public boolean leylines_sockets$isSocketed() {
        return !tag.getList(Constants.Sockets.ID.toString(), 8).isEmpty();
    }

    public boolean leylines_sockets$addtoSocket(@NotNull Identifier identifier) {
        CompoundTag tag = getOrCreateTag();
        if (tag.containsKey(tagID)) {
            int socketCount = SocketableKt.getSocketCount(getItem());

            ListTag socketList = tag.getList(tagID, 8);
            if (socketList.size() < socketCount) {
                socketList.add(new StringTag(identifier.toString()));
                return true;
            }
        } else {
            ListTag socketList = new ListTag();
            socketList.add(new StringTag(identifier.toString()));
            tag.put(tagID, socketList);

            return true;
        }

        return false;
    }

    @NotNull
    public List<Identifier> leylines_sockets$getSocketItems() {
        if (!this.tag.containsKey(tagID)) {
            return Collections.emptyList();
        }
        ListTag list = tag.getList(tagID, 8);

        ArrayList<Identifier> result = new ArrayList<>();

        list.forEach(tag -> {
            String[] id = tag.asString().split(":");
            result.add(new Identifier(id[0], id[1]));
        });

        return result;
    }

    public boolean leylines_sockets$removeFromSocket(@NotNull Identifier identifier) {

        CompoundTag tag = getOrCreateTag();
        if (tag.containsKey(tagID)) {

            ListTag socketList = tag.getList(tagID, 8);
            for (int i = 0; i < socketList.size(); i++) {
                String string = socketList.getString(i);
                if (string.equals(identifier.toString())) {
                    socketList.remove(i);

                    return true;
                }
            }
        }
        return false;
    }
}


