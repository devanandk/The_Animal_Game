package com.animalgame.uimanagement;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

public class Button {

	private Image buttonImage;
	private String buttonName;
	private int xPos;
	private int yPos;
	private Rectangle buttonBounds;

	public Button(Image buttonImage, String buttonName, int x, int y){
		this.buttonImage = buttonImage;
		this.buttonName = buttonName;
		this.xPos = x;
		this.yPos = y;
		this.setButtonBounds();
	}
	
	public Image getButtonImage() {
		return buttonImage;
	}

	public void setButtonImage(Image buttonImage) {
		this.buttonImage = buttonImage;
	}

	public String getButtonName() {
		return buttonName;
	}
	
	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	
	public Rectangle getButtonBounds() {
		return buttonBounds;
	}
	
	private void setButtonBounds() {
		buttonBounds = new Rectangle();
		buttonBounds.x = this.xPos;
		buttonBounds.y = this.yPos;
		buttonBounds.height = this.buttonImage.getHeight(null);
		buttonBounds.width = this.buttonImage.getWidth(null);
	}
	
	public int getWidth(){
		return buttonImage.getWidth(null);
	}
	
	public int getHeight(){
		return buttonImage.getHeight(null);
	}
	
	public boolean isClicked(int clickX, int clickY){
		Rectangle clickPosition = new Rectangle();
		clickPosition.x = clickX;
		clickPosition.y = clickY;
		clickPosition.height = 1;
		clickPosition.width = 1;
		
		if(clickPosition.intersects(this.getButtonBounds())){
			return true;
		}
		return false;
	}
	
	public boolean isClicked(Rectangle clickPosition){
		
		if(clickPosition.intersects(this.getButtonBounds())){
			return true;
		}
		return false;
	}
	
	public boolean isClicked(Point clickPoint){
		Rectangle clickPosition = new Rectangle();
		clickPosition.x = clickPoint.x;
		clickPosition.y = clickPoint.y;
		clickPosition.height = 1;
		clickPosition.width = 1;
		
		if(clickPosition.intersects(this.getButtonBounds())){
			return true;
		}
		return false;
	}

	public void drawButton(Graphics2D g){
		g.drawImage(this.getButtonImage(), this.getxPos(), this.getyPos(), null);
	}
}
