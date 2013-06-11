package com.animalgame.environment;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;
import java.util.ConcurrentModificationException;

import javax.imageio.ImageIO;

import com.animalgame.uimanagement.Core;
import com.animalgame.uimanagement.Menu;


public class AnimalGameLauncher extends Core implements KeyListener, MouseListener {

	Forest amazon;
	long dayDuration;
	boolean showMenu;
	
	Menu gameMenu;
	
	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	InputStream input;
	
	String message = "";
	Long dayChangeIntervalCounter;
	
	AnimalGameLauncher(){
		startTime = System.currentTimeMillis();
		running = true;
		dayDuration = 5000000;
		showMenu = true;
		dayChangeIntervalCounter = 0L;
	}
	
	public static void main(String[] args) {

		new AnimalGameLauncher().run();
	}
	
	public void init(){
	
		super.init();
		try{
		Window w = screen.getFullScreenWindow();
		
		w.addMouseListener(this);
		
		w.addKeyListener(this);
		
		w.setFocusable(true);
		
		w.requestFocusInWindow();
		
		amazon = new Forest();
		amazon.setVanishingPoint(370);
		amazon.setForestSizeX(screen.getWidth());
		amazon.setForestSizeY(screen.getHeight());
		//---------------------------------------------------------------
		//		Creating main game menu
		//---------------------------------------------------------------
		input = classLoader.getResourceAsStream("resources/menubackground.png");
		Image menuBackground = ImageIO.read(input);
		
		gameMenu = new Menu(menuBackground, 20, 100);
		
		gameMenu.setxOffset(-10);
		gameMenu.setyOffset(30);
		
		Image buttonImage;
		
		input = classLoader.getResourceAsStream("resources/lionbutton.png");
		buttonImage = ImageIO.read(input);
		gameMenu.addButton(buttonImage, "Lion");
		
		input = classLoader.getResourceAsStream("resources/tigerbutton.png");
		buttonImage = ImageIO.read(input);
		gameMenu.addButton(buttonImage, "Tiger");
		
		input = classLoader.getResourceAsStream("resources/hippobutton.png");
		buttonImage = ImageIO.read(input);
		gameMenu.addButton(buttonImage, "Hippo");
		
		input = classLoader.getResourceAsStream("resources/foxbutton.png");
		buttonImage = ImageIO.read(input);
		gameMenu.addButton(buttonImage, "Fox");
		
		input = classLoader.getResourceAsStream("resources/dogbutton.png");
		buttonImage = ImageIO.read(input);
		gameMenu.addButton(buttonImage, "Dog");
		
		input = classLoader.getResourceAsStream("resources/catbutton.png");
		buttonImage = ImageIO.read(input);
		gameMenu.addButton(buttonImage, "Cat");
		
		input = classLoader.getResourceAsStream("resources/rabbitbutton.png");
		buttonImage = ImageIO.read(input);
		gameMenu.addButton(buttonImage, "Rabbit");
		
		}
		catch(Exception e){
			System.out.println("Exception in init()");
		}
	}
	
	private void toggleMenuState(){
		if(showMenu){
			showMenu = false;
		}
		else
		{
			showMenu = true;
		}
	}

	@Override
	public void update(long timePassed) {
		
		long timeElapsed = System.currentTimeMillis() - dayChangeIntervalCounter;
		
		if(timeElapsed >= dayDuration){
			dayChangeIntervalCounter = System.currentTimeMillis();
			amazon.changeTimeOfDay();
			message = "It's "+ amazon.getTimeOfDay() + " now...";
		}
//		if((timeElapsed % 2) == 0){
		amazon.updateAnimalStates();
		amazon.moveAnimals(timePassed);
//		}		
	}

	@Override
	public void draw(Graphics2D g) {
try{
	g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		amazon.drawForest(g);
		
		g.drawString("Press ESC to exit.", screen.getWidth() - 180, 30);
		g.drawString(message, 10, 720);
		
		String animalCount = "";
		int count = amazon.getAnimalCount();
		
		if(count == 1){
			animalCount = count + " Animal";
		}
		else{
			animalCount = count + " Animals";
		}
		
		if(showMenu){
			gameMenu.drawMenu(g);
			g.drawString(animalCount, screen.getWidth() - 180, 60);
		}
		else{
			g.drawString("Press M for Menu.", screen.getWidth() - 180, 60);
			g.drawString(animalCount, screen.getWidth() - 180, 90);
		}
		
}
catch(ConcurrentModificationException er){
	System.out.println("error in animal game launcher draw method.");
	er.printStackTrace();
}
catch(Exception e){
	System.out.println("Unexpected error in game launcher draw method");
	e.printStackTrace();
}
		
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {

		int keyCode = e.getKeyCode();
		
		if(keyCode == KeyEvent.VK_ESCAPE){
			stop();
		}
		else if(keyCode == KeyEvent.VK_M)
		{
			toggleMenuState();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		int x = e.getX();
		int y = e.getY();
		try{
			if(showMenu){
		if(gameMenu.buttonClicked(x, y)){
			
			amazon.addNewAnimal(gameMenu.getLastSelectionName().toUpperCase());
		}
		else{
			if(!gameMenu.clickedInsideMenuBounds(x, y)){
				this.toggleMenuState();
			}
			//message = "";
		}
		}
			else{
				
			//	message = "";
			}
		}
		catch(Exception ex){
			System.out.println("Exception in mouse pressed.");
			ex.printStackTrace();
		}
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
