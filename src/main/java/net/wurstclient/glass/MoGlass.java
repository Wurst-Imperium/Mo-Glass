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
import net.minecraft.block.Blocks;
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
			.method_26235(MoGlass::method_26114_)
			.method_26236(MoGlass::method_26122)
			.method_26243(MoGlass::method_26122));
	
	public static final Block GLASS_STAIRS =
		new GlassStairsBlock(AbstractBlock.Settings.of(Material.GLASS)
			.strength(0.3F).sounds(BlockSoundGroup.GLASS).nonOpaque()
			.method_26235(MoGlass::method_26114_)
			.method_26236(MoGlass::method_26122)
			.method_26243(MoGlass::method_26122));
	
	// These methods replace:
	//
	// public boolean canSuffocate(BlockState blockState_1, BlockView
	// blockView_1, BlockPos blockPos_1)
	//
	// public boolean isSimpleFullBlock(BlockState blockState_1, BlockView
	// blockView_1, BlockPos blockPos_1)
	//
	// public boolean allowsSpawning(BlockState blockState_1, BlockView
	// blockView_1, BlockPos blockPos_1, EntityType<?> entityType_1)
	//
	// I'm not sure which is which and why two of them point to the same method.
	// Hopefully the mappings will clear this up in the future.
	
	/**
	 * Copy of {@link Blocks#method_26114}
	 */
	private static Boolean method_26114_(BlockState blockState,
		BlockView blockView, BlockPos blockPos, EntityType<?> entityType)
	{
		return false;
	}
	
	/**
	 * Copy of {@link Blocks#method_26122}
	 */
	private static boolean method_26122(BlockState blockState,
		BlockView blockView, BlockPos blockPos)
	{
		return false;
	}
	
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
}
