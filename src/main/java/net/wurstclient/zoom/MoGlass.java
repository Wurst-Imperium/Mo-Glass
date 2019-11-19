/*
 * Copyright (C) 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.zoom;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public enum MoGlass
{
	INSTANCE;
	
	public static final String VERSION = "1.0";
	
	public static final Block GLASS_SLAB =
		new GlassSlabBlock(FabricBlockSettings.of(Material.GLASS).hardness(0.3F)
			.resistance(0.3F).sounds(BlockSoundGroup.GLASS).build());
	
	public void initialize()
	{
		System.out.println("Starting Mo Glass...");
		
		Identifier glassSlabId = new Identifier("mo_glass", "glass_slab");
		Registry.register(Registry.BLOCK, glassSlabId, GLASS_SLAB);
		Registry.register(Registry.ITEM, glassSlabId, new BlockItem(GLASS_SLAB,
			new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
	}
}
