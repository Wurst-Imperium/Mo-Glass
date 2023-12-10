/*
 * Copyright (c) 2019-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;
import net.wurstclient.glass.MoGlassTags;

@Mixin(LightEngine.class)
public class LightEngineMixin
{
	/**
	 * See {@link MoGlassTags#OPAQUE_FOR_LIGHTING} for why this exists.
	 */
	@Redirect(at = @At(value = "INVOKE",
		target = "Lnet/minecraft/world/level/block/state/BlockState;canOcclude()Z",
		ordinal = 0),
		method = "isEmptyShape(Lnet/minecraft/world/level/block/state/BlockState;)Z")
	private static boolean isOpaqueForLightingShape(BlockState blockState)
	{
		return blockState.canOcclude()
			|| blockState.is(MoGlassTags.OPAQUE_FOR_LIGHTING);
	}
}
