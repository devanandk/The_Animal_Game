package com.animalgame.environment;

import com.animalgame.uimanagement.Animation;
import com.animalgame.uimanagement.Sprite;

public class Grass extends Sprite {

	private int strength;
	private boolean isAlive;
	public Grass(Animation a) {
		super(a);
		
		setStrength(2000);
		this.setAlive(true);
		this.setVelocityX(0.0F);
		this.setVelocityY(0.0F);
		
	}
	
	/**
	 * @return the strength
	 */
	public int getStrength() {
		if(strength > 0){
			strength-=250;
		return 250;
		}
		else{
			this.setAlive(false);
			return 0;
		}
	}
	
	public int getRemainingStrength(){
		return this.strength;
	}
	
	/**
	 * @param strength the strength to set
	 */
	public void setStrength(int strength) {
		this.strength = strength;
	}

	public void setGrassPosition(int forestX, int forestY, int vanishingPoint){
		float dy = forestY - vanishingPoint;
		float x = (float)(Math.random() * (forestX - this.getWidth()));
		float y = (float)(Math.random() * (dy));
		
		y += vanishingPoint;
		
		if(x < 0) 							x = 10;
		if(x > forestX - this.getWidth()) 	x = forestX - this.getWidth();
		
		if(y < vanishingPoint) 				y = vanishingPoint;
		if(y > forestY - this.getHeight())	y = forestY - this.getHeight() - 25;
		
		this.setX(x);
		this.setY(y);
	}

	public boolean isAlive() {
		return isAlive;
	}

	private void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
}
