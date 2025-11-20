package com.min01.unleashed.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class CelestialJellyItem extends Item
{
	public CelestialJellyItem() 
	{
		super(new Item.Properties().rarity(Rarity.EPIC).food(new FoodProperties.Builder().nutrition(1).saturationMod(0.8F).alwaysEat().fast().build()));
	}

	@Override
	public void appendHoverText(ItemStack p_41421_, Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) 
	{
		p_41423_.add(Component.translatable("item.bossesunleashed.celestial_jelly.tooltip").withStyle(ChatFormatting.AQUA));
	}
}
