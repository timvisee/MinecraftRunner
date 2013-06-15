package com.timvisee.minecraftrunner.util;

import java.io.File;
import java.io.IOException;

import com.timvisee.minecraftrunner.Platform;

public class FileUtils {
	
	/**
	 * Check if a path is valid according to the OS rules without checking if the file or path exists
	 * @param path The path to check for
	 * @return True if valid
	 */
	public static boolean isValidPath(String path) {
		File f = new File(path);
		try {
			f.getCanonicalPath();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Get the system's appdata directory
	 * @return Appdata folder
	 */
	public static File getAppData() {
        String homeDir = System.getProperty("user.home", ".");
        File workingDir;

        switch (Platform.getPlatform()) {
        case WINDOWS:
            String applicationData = System.getenv("APPDATA");
            if (applicationData != null)
                workingDir = new File(applicationData);
            else
                workingDir = new File(homeDir);
            break;
        case LINUX:
        case SOLARIS:
            workingDir = new File(homeDir);
            break;
        case MAC_OS_X:
            workingDir = new File(homeDir);
            break;
        default:
            workingDir = new File(homeDir);
        }
        
        return workingDir;
    }
}
