/*
 * Copyright (c) 2023 Team Galacticraft
 *
 * Licensed under the MIT license.
 * See LICENSE file in the project root for details.
 */

package micdoodle8.mods.galacticraft.planets.venus.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.inventory.SlotSpecific;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityLaserTurret;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerLaserTurret extends Container
{

    private TileEntityLaserTurret tileEntity;

    public ContainerLaserTurret(InventoryPlayer par1InventoryPlayer, TileEntityLaserTurret laserTurret)
    {
        this.tileEntity = laserTurret;
        this.addSlotToContainer(new SlotSpecific(laserTurret, 0, 82, 103, IItemElectric.class));

        int var6;
        int var7;

        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 69 + 68 + var6 * 18));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 79 + 116));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.tileEntity.isUsableByPlayer(var1);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(par1);
        final int b = this.inventorySlots.size();

        if (slot != null && slot.getHasStack())
        {
            final ItemStack stack = slot.getStack();
            var2 = stack.copy();
            boolean movedToMachineSlot = false;

            if (par1 == 0)
            {
                if (!this.mergeItemStack(stack, b - 36, b, true))
                {
                    return ItemStack.EMPTY;
                }
            } else
            {
                if (EnergyUtil.isElectricItem(stack.getItem()))
                {
                    if (!this.mergeItemStack(stack, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                    movedToMachineSlot = true;
                } else
                {
                    if (par1 < b - 9)
                    {
                        if (!this.mergeItemStack(stack, b - 9, b, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.mergeItemStack(stack, b - 36, b - 9, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stack.isEmpty())
            {
                // Needed where tile has inventoryStackLimit of 1
                if (movedToMachineSlot && var2.getCount() > 1)
                {
                    ItemStack remainder = var2.copy();
                    remainder.shrink(1);
                    slot.putStack(remainder);
                } else
                {
                    slot.putStack(ItemStack.EMPTY);
                }
            } else
            {
                slot.onSlotChanged();
            }

            if (stack.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(entityPlayer, stack);
        }

        return var2;
    }
}
