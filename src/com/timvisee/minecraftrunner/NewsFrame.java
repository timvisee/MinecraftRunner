package com.timvisee.minecraftrunner;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;

public class NewsFrame extends JFrame implements HyperlinkListener {

	private static final long serialVersionUID = -4911040003703939979L;
	
	public static final String NEWS_URL = "http://mcupdate.tumblr.com/";
	
    private JEditorPane browser;
	
	/**
	 * Constructor
	 */
	public NewsFrame(JFrame owner) {
		// Construct super class
		super(MinecraftRunner.FRAME_TITLE + " - Minecraft News");
		
		// Build the frame's UI
		buildUI();
		
		// Set the frame's position
		setLocationRelativeTo(owner);
		
		// Load the page
		loadNewsPage();
	}
	
	/**
	 * Build the UI of the frame
	 */
	public void buildUI() {
		// Set the layout
		setLayout(new BorderLayout(0, 0));

		// Set some frame stuff
		setSize(854, 480);
		setResizable(true);
		
		// Set the icon of the frame
		try {
            InputStream in = MinecraftRunner.class.getResourceAsStream("/res/icon.png");
            if(in != null)
                setIconImage(ImageIO.read(in));
        } catch (IOException e) { }
		
		// Set up the browser panel
        browser = new JEditorPane();
        browser.setContentType("text/html");
        browser.setEditable(false);
        browser.setPreferredSize(new Dimension(getWidth(), getHeight()));
        browser.addHyperlinkListener(this);
        browser.setBorder(null);
        JScrollPane pane = new JScrollPane(browser);
        add(pane);
        
		
		// Add some menu bar options on systems with a menu bar available
		/*if(Platform.getPlatform().equals(Platform.MAC_OS_X)) {
			MenuBar menuBar = new MenuBar();
			Menu fileMenu = new Menu("File");
			MenuItem prefsItem = new MenuItem("Preferences");
			fileMenu.add(prefsItem);
			Menu helpMenu = new Menu("Help");
			MenuItem aboutItem = new MenuItem("About");
			helpMenu.add(aboutItem);
			menuBar.add(fileMenu);
			menuBar.add(helpMenu);
			setMenuBar(menuBar);
		}*/
	}
	
	public void loadNewsPage() {
		try {
			loadPage(new URL(NEWS_URL));
		} catch (MalformedURLException e) {
            // Show error messsage
        	JOptionPane.showMessageDialog(this, "Unable to load news page!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void loadPage(URL pageUrl) {
        // Show hour glass cursor while crawling is under way.
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        try {
            // Load and display specified page.
            browser.setPage(pageUrl);
        } catch (Exception e) {
            // Show error messsage
        	JOptionPane.showMessageDialog(this, "Unable to load news page!", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Return to default cursor.
            setCursor(Cursor.getDefaultCursor());
        }
    }

	private void openWebpage(URL url) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(url.toURI());
	        } catch (Exception e) {
	            // Show error messsage
	        	JOptionPane.showMessageDialog(this, "!", "Error while opening URL!", JOptionPane.ERROR_MESSAGE);
	        }
	    } else {
        	JOptionPane.showMessageDialog(this, "!", "Error while opening URL in the default browser!", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		URL url = e.getURL();
		
		// Open the page in the user's webbrowser
		if(e.getEventType().equals(EventType.ACTIVATED))
			openWebpage(url);
	}
}
