package com.timvisee.minecraftrunner;

public enum Platform {
	WINDOWS,
	MAC_OS_X,
	SOLARIS,
	LINUX,
	UNKNOWN;
	
	public static Platform getPlatform() {
        String osName = System.getProperty("os.name").toLowerCase();
        
        if (osName.contains("win"))
            return Platform.WINDOWS;
        if (osName.contains("mac"))
            return Platform.MAC_OS_X;
        if (osName.contains("solaris") || osName.contains("sunos"))
            return Platform.SOLARIS;
        if (osName.contains("linux") || osName.contains("unix"))
            return Platform.LINUX;
        
        return Platform.UNKNOWN;
    }
}
