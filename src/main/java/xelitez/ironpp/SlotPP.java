package xelitez.ironpp;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class SlotPP extends Slot
{
    public SlotPP(IInventory par1iInventory, int par2, int par3, int par4)
    {
        super(par1iInventory, par2, par3, par4);
        // TODO Auto-generated constructor stub
    }

    public boolean isItemValid(ItemStack par1ItemStack)
    {
        if (par1ItemStack.getItem() instanceof ItemBlock)
        {
            return true;
        }

        return false;
    }
}
