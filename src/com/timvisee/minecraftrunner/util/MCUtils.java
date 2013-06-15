package com.timvisee.minecraftrunner.util;

import java.io.File;

import com.timvisee.minecraftrunner.Platform;

public class MCUtils {
	
	/**
	 * Get the base directory
	 * @return Base directory
	 */
	public static File getBaseDirectory() {
		return FileUtils.getAppData();
	}
	
	/**
	 * Get the default Minecraft directory
	 * @return Default Minecraft directory
	 */
	public static File getMinecraftDirectory() {
		return getMinecraftDirectory(FileUtils.getAppData());
	}
	
	/**
	 * Get the default Minecraft directory inside a base directory
	 * @param base Base directory
	 * @return Minecraft directory
	 */
	public static File getMinecraftDirectory(File base) {
		switch (Platform.getPlatform()) {
        case LINUX:
        case SOLARIS:
            return new File(base, ".minecraft");
        case WINDOWS:
            return new File(base, ".minecraft");
        case MAC_OS_X:
            return new File(base, "Library/Application Support/minecraft");
        default:
            return new File(base, "minecraft");
		}
	}
	
	/**
	 * Get the default Minecraft jar file
	 * @return Minecraft jar file
	 */
	public static File getMinecraftJar() {
		return getMinecraftJar(getMinecraftDirectory());
	}

	/**
	 * Get the default Minecraft jar file
	 * @param mcDir Minecraft directory
	 * @return Minecraft jar file
	 */
	public static File getMinecraftJar(File mcDir) {
		return new File(mcDir, "bin/minecraft.jar");
	}
}
