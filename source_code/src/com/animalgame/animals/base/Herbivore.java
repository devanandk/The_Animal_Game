package com.animalgame.animals.base;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.animalgame.environment.Grass;
import com.animalgame.uimanagement.Animation;

public class Herbivore extends Animal {
	
	protected Grass currentFood;
	protected boolean isGrazing;

	public Herbivore(String name, String food, String sound, Animation herbivoreAnimation) {
		super(name, food, sound, "herbivore", herbivoreAnimation);
		currentFood = null;
		currentHunter = null;
		setGrazing(false);
		this.roam();
	}
	
protected void graze(Grass food){
	this.setFood(food);
}

public String getGrassInfo(){
	if(currentFood != null){
	return "Grass @ " + currentFood.getX() + " : " + currentFood.getY();
	}
	else{
		return null;
	}
}

protected boolean graze(ArrayList<Grass> grass){

	Grass food = this.findClosestGrass(grass);
	
	if(food != null){
		this.setCurrentAction(AnimalState.GRAZING);
//		System.out.println(this.getName() + " is grazing grass @ " + food.getX() + " : " + food.getY());
		this.setAnimalActionMessage("I am hungry..");
		this.graze(food);
		this.setGrazing(true);
		return true;
	}
	return false;
}

protected void roam(){
	if(!this.getCurrentAction().equals(AnimalState.ROAMING)){
	this.setCurrentAction(AnimalState.ROAMING);
	this.setAnimalActionMessage("I am roaming");
	this.graze((Grass)null);
	this.setGrazing(false);
	this.setAnimalSpeed(walkingSpeed);
	this.setRandomVelocity();
	}
}

public void move(int forestX, int forestY, int vanishingPoint){
		
		if(this.getCurrentAction().equals(AnimalState.GRAZING)){
			if(this.foundGrass()){
				this.setAnimalActionMessage("eating grass..");
				this.setVelocityX(0);
				this.setVelocityY(0);
			}
			else{
			this.setAnimalSpeed(runningSpeed);
			plotPathToGrass();
			}
			
		}	
		if(this.getCurrentAction().equals(AnimalState.EVADING)){
			this.setAnimalSpeed(runningSpeed);
		}
		else{
			this.setAnimalSpeed(walkingSpeed);
			
		}
		
		super.move(forestX, forestY, vanishingPoint);	
	}

protected void plotPathToGrass() {

	float animalX = this.getX();
	float animalY = this.getY();
	
	float grassX = this.currentFood.getX();
	float grassY = this.currentFood.getY();
	
	calculateTrajectory(animalX, animalY, grassX, grassY, this.getAnimalSpeed());
}

protected void setCurrentAction(AnimalState newState){
	if(!newState.equals(AnimalState.HUNTING)){
	this.currentAction = newState;
	}
}

protected void setFood(Grass food) {
	this.currentFood = food;
}

public void setHunter(Animal hunter) {
	this.currentHunter = hunter;
}

public boolean isGrazing() {
	return isGrazing;
}

protected void setGrazing(boolean isGrazing) {
	this.isGrazing = isGrazing;
}

protected Grass findClosestGrass(ArrayList<Grass> grass) {
	
	Grass closestGrass = null;
	float closestGrassDistance = 0.0000F;
	
	boolean initialize = true;
	
	for(Grass thisGrass: grass){

		float absDistance = calculateAbsoluteDistance(this.getX(), this.getY(), thisGrass.getX(), thisGrass.getY());
		
		if(initialize){
			closestGrassDistance = absDistance;
			closestGrass = thisGrass;
			initialize = false;
		}
		if(absDistance < closestGrassDistance){
			closestGrassDistance = absDistance;
			closestGrass = thisGrass;
		}
	}
	return closestGrass;
}

public void updateHerbivoreState(ArrayList<Grass> grass, ArrayList<Animal> Animals){
	if(this.getCurrentAction().equals(AnimalState.DYING)){
		this.setAnimalActionMessage("I am dying...");
		this.setVelocityX(0);
		this.setVelocityY(0);
	}
	else if(this.getCurrentAction().equals(AnimalState.EVADING)){
		if(this.alreadyEvading){
			if(!Animals.contains(this.currentHunter)){
				this.roam();
			}
		}
		this.setAnimalSpeed(runningSpeed * (2/3));
		this.evade();
	}
	else if(this.getLifeStrength() < (this.getMax_Life() / 2)){
		if(this.isGrazing()){
			if(!grass.contains(this.currentFood)){
				this.graze(grass);
			}
		}
		else{
			this.graze(grass);
		}
		
	}
	else if(this.getLifeStrength() >= (this.getMax_Life())){
		this.roam();
	}
}

protected boolean foundGrass() {
	
	Rectangle thisHerbivore = new Rectangle();
	Rectangle currentGrass = new Rectangle();
	
	thisHerbivore.x = (int) this.getX();
	thisHerbivore.y = (int) this.getY();
	thisHerbivore.width = this.getWidth();
	thisHerbivore.height = this.getHeight();
	
	currentGrass.x = (int) this.currentFood.getX();
	currentGrass.y = (int) this.currentFood.getY();
	currentGrass.width = this.currentFood.getWidth();
	currentGrass.height = this.currentFood.getHeight();
	
	if(this.checkCollision(thisHerbivore, currentGrass)){
		return true;
	}
	else{
	return false;
	}
}

public void update(long timePassed){
	if(this.isGrazing()){
		if(this.foundGrass()){
		int health = currentFood.getStrength();
		if(health > 0){
		this.addLife(health);
		}
		else{
			this.roam();
		}
	}
	}
		super.update(timePassed);
}
}
