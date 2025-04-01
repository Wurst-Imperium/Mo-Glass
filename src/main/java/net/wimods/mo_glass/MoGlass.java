/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.mo_glass;

import java.util.Optional;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.CustomValue.CvType;

public enum MoGlass
{
	INSTANCE;
	
	private boolean client;
	private Optional<Platform> platform;
	
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
	
	public Optional<Platform> getPlatform()
	{
		if(platform == null)
			platform = FabricLoader.getInstance().getModContainer("mo_glass")
				.map(ModContainer::getMetadata)
				.map(metadata -> metadata.getCustomValue("mo_glass"))
				.filter(cv -> cv != null && cv.getType() == CvType.OBJECT)
				.map(CustomValue::getAsObject)
				.map(object -> object.get("platform"))
				.filter(cv -> cv != null && cv.getType() == CvType.STRING)
				.map(CustomValue::getAsString).map(Platform::of);
		
		return platform;
	}
	
	public static enum Platform
	{
		MODRINTH("Modrinth"),
		CURSEFORGE("CurseForge"),
		WI_MODS("WI-Mods");
		
		private final String name;
		
		private Platform(String name)
		{
			this.name = name;
		}
		
		public String getName()
		{
			return name;
		}
		
		public static Platform of(String name)
		{
			for(Platform platform : values())
				if(platform.getName().equals(name))
					return platform;
				
			return null;
		}
	}
}
