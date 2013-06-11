package com.animalgame.uimanagement;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Window;

public abstract class Core {

	private static DisplayMode modes[] = {
		new DisplayMode(1400, 900, 32, 0),
		new DisplayMode(1400, 900, 24, 0),
		new DisplayMode(1400, 900, 16, 0),
		new DisplayMode(1366, 768, 32, 0),
		new DisplayMode(1366, 768, 24, 0),
		new DisplayMode(1366, 768, 16, 0),
		new DisplayMode(1024, 768, 32, 0),
		new DisplayMode(1024, 768, 24, 0),
		new DisplayMode(1024, 768, 16, 0),
	};
	
	protected boolean running;
	protected ScreenManager screen;
	protected long startTime;
	
	
	public void stop(){
		running = false;
	}
	
	public void run(){
		try{
			init();
			gameLoop();
		}
		finally{
			screen.restoreScreen();
		}
	}
	
	public void init(){
		screen = new ScreenManager();
		DisplayMode dm = screen.findFirstCompatibleMode(modes);
		screen.setFullScreen(dm);
		
		Window w = screen.getFullScreenWindow();
		w.setFont(new Font("Arial", Font.PLAIN, 20));
		w.setBackground(Color.WHITE);
		w.setForeground(Color.BLACK);
		running = true;
	}
	
	public void gameLoop(){
		startTime = System.currentTimeMillis();
		long cumTime = startTime;
		
		while(running){
			long timePassed = System.currentTimeMillis() - cumTime;
			cumTime += timePassed;
			
			update(timePassed);
			
			Graphics2D g = screen.getGraphics();
			draw(g);
			g.dispose();
			screen.update();
			
			try{
				Thread.sleep(1);
			}
				catch(Exception ex){}
		}
	}
	
	public abstract void update(long timePassed);
	
	public abstract void draw(Graphics2D g);
}
