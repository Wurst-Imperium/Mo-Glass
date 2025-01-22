/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.wurstclient.glass.mixin.BlockMixin;

public enum MoGlassTags
{
	;
	
	/**
	 * Blocks in this tag need to be considered opaque for lighting
	 * calculations, but non-opaque for culling. This is necessary for tinted
	 * glass slabs and stairs to properly block light without letting you see
	 * through the world.
	 *
	 * <p>
	 * See {@link BlockMixin} for how I modified culling to make this work.
	 */
	public static final TagKey<Block> NON_OPAQUE_FOR_CULLING =
		createTag("non_opaque_for_culling");
	
	private static TagKey<Block> createTag(String idPath)
	{
		return TagKey.create(Registries.BLOCK,
			ResourceLocation.fromNamespaceAndPath("mo_glass", idPath));
	}
}
