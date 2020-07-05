/*
 * Copyright (C) 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

public enum MoGlass
{
	INSTANCE;
	
	public static final String VERSION = "1.2";
	
	private boolean client;
	
	public static final Block GLASS_SLAB =
		new GlassSlabBlock(AbstractBlock.Settings.of(Material.GLASS)
			.strength(0.3F).sounds(BlockSoundGroup.GLASS).nonOpaque()
			.allowsSpawning(MoGlass::never).solidBlock(MoGlass::never)
			.suffocates(MoGlass::never));
	
	public static final Block GLASS_STAIRS =
		new GlassStairsBlock(AbstractBlock.Settings.of(Material.GLASS)
			.strength(0.3F).sounds(BlockSoundGroup.GLASS).nonOpaque()
			.allowsSpawning(MoGlass::never).solidBlock(MoGlass::never)
			.suffocates(MoGlass::never));
	
	public void initialize(boolean client)
	{
		this.client = client;
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
		
		if(client)
			BlockRenderLayerMap.INSTANCE.putBlock(block,
				RenderLayer.getTranslucent());
	}
	
	// Copies of the Blocks.never() methods because the originals are not
	// accessible from here.
	
	private static Boolean never(BlockState blockState, BlockView blockView,
		BlockPos blockPos, EntityType<?> entityType)
	{
		return false;
	}
	
	private static boolean never(BlockState blockState, BlockView blockView,
		BlockPos blockPos)
	{
		return false;
	}
}
