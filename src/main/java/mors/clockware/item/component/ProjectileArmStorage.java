package mors.clockware.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mors.clockware.registry.Clockware_Components;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;

import java.util.ArrayList;
import java.util.List;

public record ProjectileArmStorage(List<ItemStack> items, int maxItems) implements TooltipComponent {

    public static ItemStack of(final ItemStack stack, int maxItems) {
        stack.set(Clockware_Components.PROJECTILE_ARM_STORAGE, new ProjectileArmStorage(List.of(), maxItems));

        return stack;
    }

    public static ProjectileArmStorage createStorage(int maxItems) {
        return new ProjectileArmStorage(List.of(), maxItems);
    }

    public static final Codec<ProjectileArmStorage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.listOf().fieldOf("items").forGetter(storage -> storage.items),
            Codec.INT.fieldOf("max_items").forGetter(storage -> storage.maxItems)
    ).apply(instance, ProjectileArmStorage::new));


    public static final StreamCodec<RegistryFriendlyByteBuf, ProjectileArmStorage> STREAM_CODEC = StreamCodec.of(
            ProjectileArmStorage::write,
            ProjectileArmStorage::read
    );


    private static void write(RegistryFriendlyByteBuf buffer, ProjectileArmStorage packet) {

        buffer.writeInt(packet.maxItems);

        buffer.writeInt(packet.items.size());

        for (int i = 0; i < packet.items.size(); i++) {
            ItemStack.STREAM_CODEC.encode(buffer, packet.items.get(i));
        }
    }

    public static ProjectileArmStorage read(RegistryFriendlyByteBuf buffer) {
        int maxItems = buffer.readInt();
        int listSize = buffer.readInt();

        List<ItemStack> stackList = new ArrayList<>();

        for (int i = 0; i < listSize; i++) {
            stackList.add(ItemStack.STREAM_CODEC.decode(buffer));
        }

        return new ProjectileArmStorage(stackList, maxItems);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return !(other instanceof ProjectileArmStorage projectileArmStorage)
                    ? false
                    : ItemStack.listMatches(this.items, projectileArmStorage.items);
        }
    }

    @Override
    public int hashCode() {
        return ItemStack.hashStackList(this.items);
    }

    public static class Mutable {
        private final List<ItemStack> items;
        private final int maxItems;

        public Mutable(ProjectileArmStorage contents) {
            this.items = new ArrayList<>(contents.items);
            this.maxItems = contents.maxItems;
        }

        public List<ItemStack> getItems() {
            return items;
        }

        private int getMaxAmountToAdd(ItemStack stack) {

            return Math.max(stack.getCount(), this.maxItems);
        }

        public static boolean canInsert(ItemStack stackToInsert){
            return stackToInsert.getItem() instanceof ProjectileItem;
        }

        public void updateStacks() {
            if (!items.isEmpty() && items.getFirst().isEmpty()) {

                if(items.size()==1){
                    this.items.removeFirst();
                } else {
                    //Rearranges the stacks
                    for (int i = 1; i < items.size(); i++) {
                        if (!items.get(i).isEmpty()) {
                            items.set(0, items.get(i));
                            items.remove(i);
                            break;
                        }
                    }
                }
            }
        }

        public int tryInsert(ItemStack stackToInsert) {
            int currentAmount=0;
            for (ItemStack stored : this.items){
                currentAmount+=stored.getCount();
            }

            if (stackToInsert.isEmpty() || !canInsert(stackToInsert) || currentAmount>=this.maxItems) {
                return 0;
            }

            int insertedAmount = 0;

            for (ItemStack stored : this.items) {
                if (ItemStack.isSameItemSameComponents(stored, stackToInsert)) {
                    int space = stored.getMaxStackSize() - stored.getCount();
                    if (space > 0) {

                        int amount = Math.min(space, stackToInsert.getCount());
                        if(currentAmount+amount>this.maxItems){
                            amount= this.maxItems-currentAmount;
                        }

                        if (amount <= 0) break;

                        stored.grow(amount);
                        stackToInsert.shrink(amount);
                        insertedAmount += amount;
                        if (stackToInsert.isEmpty()) break;
                    }
                }
            }

            while (!stackToInsert.isEmpty()) {
                int currentCount = this.items.stream().mapToInt(ItemStack::getCount).sum();
                if (currentCount >= this.maxItems) break;

                int spaceLeft = this.maxItems - currentCount;
                int amountToAdd = Math.min(stackToInsert.getCount(), Math.min(spaceLeft, stackToInsert.getMaxStackSize()));

                ItemStack newStack = stackToInsert.copyWithCount(amountToAdd);
                this.items.addFirst(newStack);

                stackToInsert.shrink(amountToAdd);
                insertedAmount += amountToAdd;
            }

            return insertedAmount;
        }

        public ItemStack removeOne() {
            if (this.items.isEmpty()) {
                return null;
            } else {
                return this.items.removeFirst().copy();
            }
        }

        public int tryTransfer(Slot slot, Player player) {
            ItemStack itemstack = slot.getItem();
            int i = this.getMaxAmountToAdd(itemstack);
            return this.tryInsert(slot.safeTake(itemstack.getCount(), i, player));
        }

        public ProjectileArmStorage toImmutable() {
            return new ProjectileArmStorage(List.copyOf(this.items), this.maxItems);
        }
    }
}
