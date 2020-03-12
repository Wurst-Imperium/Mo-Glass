/*
 * Copyright (C) 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public enum MoGlass
{
	INSTANCE;
	
	public static final String VERSION = "1.2";
	
	public static final Block GLASS_SLAB =
		new GlassSlabBlock(FabricBlockSettings.of(Material.GLASS).hardness(0.3F)
			.resistance(0.3F).sounds(BlockSoundGroup.GLASS).build());
	
	public static final Block GLASS_STAIRS = new GlassStairsBlock(
		FabricBlockSettings.of(Material.GLASS).hardness(0.3F).resistance(0.3F)
			.sounds(BlockSoundGroup.GLASS).build());
	
	public void initialize(boolean client)
	{
		System.out.println("Starting Mo Glass...");
		
		registerBlock(GLASS_SLAB, "glass_slab", ItemGroup.BUILDING_BLOCKS);
		registerBlock(GLASS_STAIRS, "glass_stairs", ItemGroup.BUILDING_BLOCKS);
	}
	
	private void registerBlock(Block block, String idPath, ItemGroup itemGroup)
	{
		Identifier identifier = new Identifier("mo_glass", idPath);
		Registry.register(Registry.BLOCK, identifier, block);
		
		Settings itemSettings = new Item.Settings().group(itemGroup);
		BlockItem blockItem = new BlockItem(block, itemSettings);
		Registry.register(Registry.ITEM, identifier, blockItem);
	}
}
