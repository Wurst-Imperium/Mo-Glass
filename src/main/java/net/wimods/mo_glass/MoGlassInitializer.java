/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.mo_glass;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;

public final class MoGlassInitializer
	implements ClientModInitializer, DedicatedServerModInitializer
{
	private static boolean initialized;
	
	@Override
	public void onInitializeClient()
	{
		if(initialized)
			throw new RuntimeException(
				"MoGlassInitializer.onInitialize() ran twice!");
		
		MoGlass.INSTANCE.initialize(true);
		initialized = true;
	}
	
	@Override
	public void onInitializeServer()
	{
		if(initialized)
			throw new RuntimeException(
				"MoGlassInitializer.onInitialize() ran twice!");
		
		MoGlass.INSTANCE.initialize(false);
		initialized = true;
	}
}
