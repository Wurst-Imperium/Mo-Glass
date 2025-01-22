/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass;

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
}
