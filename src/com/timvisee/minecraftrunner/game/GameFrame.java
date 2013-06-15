package com.timvisee.minecraftrunner.game;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.timvisee.minecraftrunner.MinecraftRunner;

class GameFrame extends JFrame {
    
	private static final long serialVersionUID = 548254692573904433L;

	private static final Logger logger = Logger.getLogger(GameFrame.class.getCanonicalName());
    
    //private static final long serialVersionUID = 5499648340202625650L;
    private JPanel wrapper;
    private Applet applet;

    public GameFrame(Dimension dim) {
        setTitle("Minecraft - " + MinecraftRunner.FRAME_TITLE);
        setBackground(Color.BLACK);
        
        // Set the icon of the frame
 		try {
             InputStream in = MinecraftRunner.class.getResourceAsStream("/res/icon.png");
             if(in != null)
                 setIconImage(ImageIO.read(in));
         } catch (IOException e) { }
        
        wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(dim != null ? dim : new Dimension(854, 480));
        wrapper.setLayout(new BorderLayout());
        add(wrapper, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                if (applet != null) {
                    applet.stop();
                    applet.destroy();
                }

                System.exit(0);
            }
        });
    }

    public void start(Applet applet) {
        logger.info("Starting " + applet.getClass().getCanonicalName());
        
        applet.init();
        wrapper.add(applet, BorderLayout.CENTER);
        validate();
        applet.start();
    }
}
