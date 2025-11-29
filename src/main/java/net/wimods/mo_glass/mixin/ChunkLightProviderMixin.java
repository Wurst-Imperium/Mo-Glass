/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.mo_glass.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import net.wimods.mo_glass.MoGlassTags;

@Mixin(ChunkLightProvider.class)
public class ChunkLightProviderMixin
{
	/**
	 * See {@link MoGlassTags#OPAQUE_FOR_LIGHTING} for why this exists.
	 */
	@Redirect(
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/block/BlockState;isOpaque()Z",
			ordinal = 0),
		method = "isTrivialForLighting(Lnet/minecraft/block/BlockState;)Z")
	private static boolean isOpaqueForLightingShape(BlockState blockState)
	{
		return blockState.isOpaque()
			|| blockState.isIn(MoGlassTags.OPAQUE_FOR_LIGHTING);
	}
}
