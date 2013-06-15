/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010, 2011 Albert Pham <http://www.sk89q.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.timvisee.minecraftrunner;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Manages a login session.
 * 
 * @author sk89q
 */
public class LoginSession {

	// Constants
    private static final String MINECRAFT_LOGIN_URL = "https://login.minecraft.net/";
    private static final String LAUNCHER_VERSION = "13";
    
    private String user;
    private URL loginURL;
    private boolean isValid;
    
    private String latestVer;
    private String downloadTicket;
    private String sessId;
    
    /**
     * Construct the session.
     * 
     * @param username username
     */
    public LoginSession(String username) {
        // Store the username
    	this.user = username;
        
    	// Define the login URL
        try {
            this.loginURL = new URL(MINECRAFT_LOGIN_URL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Attempt to login.
     * 
     * @param password plain text password
     * @return true if successful
     * @throws IOException throw on an IO error
     * @throws OutdatedLauncherException thrown on an outdated launcher exception
     * @throws LoginException thrown on an unknown login exception
     */
    public boolean login(String password) throws IOException, OutdatedLauncherException, LoginException {
        // Define the connection variable
    	HttpsURLConnection conn = null;
        
        // HTTP request URL params
        String params = String.format("user=%s&password=%s&version=%s", URLEncoder.encode(user, "UTF-8"), 
        		URLEncoder.encode(password, "UTF-8"), URLEncoder.encode(LAUNCHER_VERSION, "UTF-8"));
        
        try {
        	// Open the connection
            conn = (HttpsURLConnection) loginURL.openConnection();
            
            // Set the connection stuff
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", Integer.toString(params.getBytes().length));
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setReadTimeout(1000 * 60 * 10);
            
            // Connect
            conn.connect();
            
            // Get the output data
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(params);
            out.flush();
            out.close();
            
            if (conn.getResponseCode() != 200)
                throw new IOException("Did not get expected 200 code");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            StringBuilder s = new StringBuilder();
            char[] buf = new char[1024];
            int len = 0;
            while ((len = reader.read(buf)) != -1) {
                s.append(buf, 0, len);
            }
            String result = s.toString();
            
            if (result.contains(":")) {
                String[] values = result.split(":");
                
                try {
                    latestVer = values[0].trim();
                    downloadTicket = values[1].trim();
                    user = values[2].trim();
                    sessId = values[3].trim();
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new LoginException("Returned login payload had an incorrect number of arguments");
                }
                
                isValid = true;
                return true;
            } else {
                if (result.trim().equals("Bad login")) {
                    return false;
                } else if (result.trim().equals("Old version")) {
                    throw new OutdatedLauncherException();
                } else {
                    throw new LoginException(result.trim());
                }
            }
        } finally {
            if (conn != null) conn.disconnect();
            conn = null;
        }
    }
    
    /**
     * Returns whether the session has been authenticated.
     * 
     * @return true is authenticated
     */
    public boolean isValid() {
        return isValid;
    }
    
    /**
     * Get the username. If a login was successful, this will be the correct
     * form of the username.
     * 
     * @return username
     */
    public String getUsername() {
        return user;
    }

    /**
     * Get the latest version of Minecraft, available once logged in.
     * 
     * @return version string
     */
    public String getLatestVersion() {
        return latestVer;
    }

    /**
     * Get the download ticket, available once logged in.
     * 
     * @return download ticket
     */
    public String getDownloadTicket() {
        return downloadTicket;
    }

    /**
     * Get the session ID, available once logged in.
     * 
     * @return session ID
     */
    public String getSessionId() {
        return sessId;
    }
    
    /**
     * Get the login URL being used.
     * 
     * @return url
     */
    public URL getLoginURL() {
        return loginURL;
    }

    public static class LoginException extends Exception {
        private static final long serialVersionUID = 3704469434921739106L;
        
        public LoginException(String message) {
            super(message);
        }
        
        public LoginException(String message, Throwable t) {
            super(message, t);
        }
    }
    
    public static class OutdatedLauncherException extends Exception {
        private static final long serialVersionUID = -7109390633647649010L;
    }
    
}
