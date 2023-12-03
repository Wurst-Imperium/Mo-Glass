/*
 * Copyright (c) 2019-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass;

import net.minecraft.block.Block;

public enum MoGlass
{
	INSTANCE;
	
	private boolean client;
	
	public void initialize(boolean client)
	{
		this.client = client;
		System.out.println("Starting Mo Glass...");
		MoGlassBlocks.initialize();
	}
	
	/**
	 * @return true if running on the client-side
	 */
	public boolean isClient()
	{
		return client;
	}
	
	// Everything below is deprecated.
	
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final Block GLASS_SLAB = MoGlassBlocks.GLASS_SLAB;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final Block GLASS_STAIRS = MoGlassBlocks.GLASS_STAIRS;
	
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassSlabBlock WHITE_STAINED_GLASS_SLAB =
		MoGlassBlocks.WHITE_STAINED_GLASS_SLAB;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassSlabBlock ORANGE_STAINED_GLASS_SLAB =
		MoGlassBlocks.ORANGE_STAINED_GLASS_SLAB;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassSlabBlock MAGENTA_STAINED_GLASS_SLAB =
		MoGlassBlocks.MAGENTA_STAINED_GLASS_SLAB;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassSlabBlock LIGHT_BLUE_STAINED_GLASS_SLAB =
		MoGlassBlocks.LIGHT_BLUE_STAINED_GLASS_SLAB;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassSlabBlock YELLOW_STAINED_GLASS_SLAB =
		MoGlassBlocks.YELLOW_STAINED_GLASS_SLAB;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassSlabBlock LIME_STAINED_GLASS_SLAB =
		MoGlassBlocks.LIME_STAINED_GLASS_SLAB;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassSlabBlock PINK_STAINED_GLASS_SLAB =
		MoGlassBlocks.PINK_STAINED_GLASS_SLAB;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassSlabBlock GRAY_STAINED_GLASS_SLAB =
		MoGlassBlocks.GRAY_STAINED_GLASS_SLAB;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassSlabBlock LIGHT_GRAY_STAINED_GLASS_SLAB =
		MoGlassBlocks.LIGHT_GRAY_STAINED_GLASS_SLAB;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassSlabBlock CYAN_STAINED_GLASS_SLAB =
		MoGlassBlocks.CYAN_STAINED_GLASS_SLAB;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassSlabBlock PURPLE_STAINED_GLASS_SLAB =
		MoGlassBlocks.PURPLE_STAINED_GLASS_SLAB;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassSlabBlock BLUE_STAINED_GLASS_SLAB =
		MoGlassBlocks.BLUE_STAINED_GLASS_SLAB;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassSlabBlock BROWN_STAINED_GLASS_SLAB =
		MoGlassBlocks.BROWN_STAINED_GLASS_SLAB;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassSlabBlock GREEN_STAINED_GLASS_SLAB =
		MoGlassBlocks.GREEN_STAINED_GLASS_SLAB;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassSlabBlock RED_STAINED_GLASS_SLAB =
		MoGlassBlocks.RED_STAINED_GLASS_SLAB;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassSlabBlock BLACK_STAINED_GLASS_SLAB =
		MoGlassBlocks.BLACK_STAINED_GLASS_SLAB;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassStairsBlock WHITE_STAINED_GLASS_STAIRS =
		MoGlassBlocks.WHITE_STAINED_GLASS_STAIRS;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassStairsBlock ORANGE_STAINED_GLASS_STAIRS =
		MoGlassBlocks.ORANGE_STAINED_GLASS_STAIRS;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassStairsBlock MAGENTA_STAINED_GLASS_STAIRS =
		MoGlassBlocks.MAGENTA_STAINED_GLASS_STAIRS;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassStairsBlock LIGHT_BLUE_STAINED_GLASS_STAIRS =
		MoGlassBlocks.LIGHT_BLUE_STAINED_GLASS_STAIRS;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassStairsBlock YELLOW_STAINED_GLASS_STAIRS =
		MoGlassBlocks.YELLOW_STAINED_GLASS_STAIRS;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassStairsBlock LIME_STAINED_GLASS_STAIRS =
		MoGlassBlocks.LIME_STAINED_GLASS_STAIRS;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassStairsBlock PINK_STAINED_GLASS_STAIRS =
		MoGlassBlocks.PINK_STAINED_GLASS_STAIRS;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassStairsBlock GRAY_STAINED_GLASS_STAIRS =
		MoGlassBlocks.GRAY_STAINED_GLASS_STAIRS;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassStairsBlock LIGHT_GRAY_STAINED_GLASS_STAIRS =
		MoGlassBlocks.LIGHT_GRAY_STAINED_GLASS_STAIRS;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassStairsBlock CYAN_STAINED_GLASS_STAIRS =
		MoGlassBlocks.CYAN_STAINED_GLASS_STAIRS;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassStairsBlock PURPLE_STAINED_GLASS_STAIRS =
		MoGlassBlocks.PURPLE_STAINED_GLASS_STAIRS;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassStairsBlock BLUE_STAINED_GLASS_STAIRS =
		MoGlassBlocks.BLUE_STAINED_GLASS_STAIRS;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassStairsBlock BROWN_STAINED_GLASS_STAIRS =
		MoGlassBlocks.BROWN_STAINED_GLASS_STAIRS;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassStairsBlock GREEN_STAINED_GLASS_STAIRS =
		MoGlassBlocks.GREEN_STAINED_GLASS_STAIRS;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassStairsBlock RED_STAINED_GLASS_STAIRS =
		MoGlassBlocks.RED_STAINED_GLASS_STAIRS;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassStairsBlock BLACK_STAINED_GLASS_STAIRS =
		MoGlassBlocks.BLACK_STAINED_GLASS_STAIRS;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassSlabBlock[] STAINED_GLASS_SLABS =
		MoGlassBlocks.STAINED_GLASS_SLABS;
	/**
	 * @deprecated Use {@link MoGlassBlocks} instead.
	 */
	@Deprecated(since = "1.6", forRemoval = true)
	public static final StainedGlassStairsBlock[] STAINED_GLASS_STAIRS =
		MoGlassBlocks.STAINED_GLASS_STAIRS;
}
