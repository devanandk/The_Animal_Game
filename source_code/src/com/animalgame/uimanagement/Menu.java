package com.animalgame.uimanagement;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Menu {
	
	private Image background;
	private ArrayList<Button> buttons;
	Rectangle menuBoundary;
	
	private int xOffset;
	private int yOffset;
	
	private int xPos;
	private int yPos;
	
	private int lastButtonSelected;
	
	public Menu(Image background, int xPos, int yPos){
		this.background = background;
		xOffset = 0;
		yOffset = 0;
		this.xPos = xPos;
		this.yPos = yPos;
		setLastButtonSelected(-1);
		buttons = new ArrayList<Button>();
		
		menuBoundary = new Rectangle();
		menuBoundary.x = this.xPos;
		menuBoundary.y = this.yPos;
		menuBoundary.height = this.background.getHeight(null);
		menuBoundary.width = this.background.getWidth(null);
	}
	
	public Menu(Image background, int xPos, int yPos, int xOffset, int yOffset){
		this.background = background;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.xPos = xPos;
		this.yPos = yPos;
		setLastButtonSelected(-1);
		buttons = new ArrayList<Button>();
		
		menuBoundary = new Rectangle();
		menuBoundary.x = this.xPos;
		menuBoundary.y = this.yPos;
		menuBoundary.height = this.background.getHeight(null);
		menuBoundary.width = this.background.getWidth(null);
	}
	
	public void addButton(Image buttonImage, String buttonName, int xPos, int yPos){
		
		buttons.add(new Button(buttonImage, buttonName, xPos, yPos));
	}
	
	public void addButton(Image buttonImage, String buttonName){
		
		int buttonx = this.xPos + (int)(background.getWidth(null)/2) - (buttonImage.getWidth(null) / 2) + this.xOffset;
		int buttony = this.yPos + yOffset;
		
		for(Button button: buttons){
			buttony += button.getHeight();
			buttony += yOffset;
		}
		
		buttons.add(new Button(buttonImage, buttonName, buttonx, buttony));
		
		
	}

	public boolean buttonClicked(int x, int y){
		
		Rectangle clickPosition = new Rectangle();
		clickPosition.x = x;
		clickPosition.y = y;
		clickPosition.height = 1;
		clickPosition.width = 1;
		int count = 0;
		
		for(Button button: buttons){
			if(button.isClicked(clickPosition)){
				this.setLastButtonSelected(count);
				return true;
			}
			count++;
		}
		
		return false;
	}
	
	public boolean clickedInsideMenuBounds(int x, int y){
		
		Rectangle clickPosition = new Rectangle();
		clickPosition.x = x;
		clickPosition.y = y;
		clickPosition.height = 1;
		clickPosition.width = 1;
		
		if(clickPosition.intersects(menuBoundary)){
			return true;
		}
		
		return false;
	}
	
	public int getLastButtonSelected() {
		return lastButtonSelected;
	}

	public String getLastSelectionName(){
		return buttons.get(lastButtonSelected).getButtonName();
	}
	
	private void setLastButtonSelected(int lastButtonSelected) {
		this.lastButtonSelected = lastButtonSelected;
	}
	
	public void drawMenu(Graphics2D g){
		g.drawImage(this.background, this.xPos, this.yPos, null);
		if(buttons.size() > 0){
		for(Button button: buttons){
		button.drawButton(g);
		}
		}
	}

	
	public int getxOffset() {
		return xOffset;
	}

	
	public void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	
	public int getyOffset() {
		return yOffset;
	}

	
	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}
	
}
