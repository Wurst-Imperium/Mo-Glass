/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.mo_glass.test;

import net.wimods.mo_glass.MoGlass;
import net.wimods.mo_glass.MoGlass.Platform;

public enum PlatformTest
{
	;
	
	public static void testPlatformDetection()
	{
		System.out.println("Testing platform detection...");
		
		Platform platform = MoGlass.INSTANCE.getPlatform().orElse(null);
		System.out.println("Platform is " + platform);
		
		if(platform != Platform.MODRINTH)
			throw new RuntimeException(
				"Expected platform MODRINTH, but got " + platform);
	}
}
