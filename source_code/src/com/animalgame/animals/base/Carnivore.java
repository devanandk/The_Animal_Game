package com.animalgame.animals.base;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.animalgame.uimanagement.Animation;

public class Carnivore extends Animal {	
	
	protected Animal currentPrey;
	protected boolean isOnTheHunt;

	public Carnivore(String name, String food, String sound, Animation carnivoreAnimation) {
		super(name, food, sound, "carnivore", carnivoreAnimation);
		currentPrey = null;
		currentHunter = null;
		isOnTheHunt = false;
		this.roam();
	}
	
	public String getPreyInfo(){
		if(currentPrey != null){
		return currentPrey.animalName + " @ " + currentPrey.getX() + " : " + currentPrey.getY();
		}
		else{
			return null;
		}
	}
	
	protected boolean hunt(ArrayList<Animal> Animals){
		Animal prey = findClosestPrey(Animals);
		if(prey!= null){
			this.setCurrentAction(AnimalState.HUNTING);
			currentPrey = prey;
			this.setOnTheHunt(true);
			this.currentPrey.setCurrentHunter(this);
			this.currentPrey.setCurrentAction(AnimalState.EVADING);
			this.setAnimalActionMessage(this.getName() + " is hunting a " + currentPrey.getName());
			return true;
		}
		else{
		return false;
		}
		
	}
	
	protected void roam(){
		if(!this.getCurrentAction().equals(AnimalState.ROAMING)){
				this.setCurrentAction(AnimalState.ROAMING);
				this.setAnimalActionMessage(this.getName() + " is roaming");
				this.currentPrey = null;
				this.setOnTheHunt(false);
				this.setCurrentHunter(null);
				this.setAnimalSpeed(walkingSpeed);
				this.setVelocityX(this.getAnimalSpeed());
				this.setVelocityY(this.getAnimalSpeed());
		}
		
	}
	
	public void move(int forestX, int forestY, int vanishingPoint){

		if(this.getCurrentAction().equals(AnimalState.HUNTING)){

			if(this.caughtPrey()){
				this.setVelocityX(0);
				this.setVelocityY(0);
			}
			else{
			this.setAnimalSpeed(runningSpeed);
			this.plotPathToPrey();
			}
		}
		else if(this.getCurrentAction().equals(AnimalState.EVADING)){
			this.evade();
		}
		else{
			this.setAnimalSpeed(walkingSpeed);
		}
		
		super.move(forestX, forestY, vanishingPoint);

	}
	
	protected void plotPathToPrey() {

		float hunterX = this.getX();
		float hunterY = this.getY();
		
		float preyX = currentPrey.getX();
		float preyY = currentPrey.getY();
		
		calculateTrajectory(hunterX, hunterY, preyX, preyY, this.getAnimalSpeed());
	}

	public Animal getCurrentPrey(){
		return currentPrey;
	}

	public float getPreyPosX() {
		return currentPrey.getX();
	}

	public float getPreyPosY() {
		return currentPrey.getY();
	}

	protected void setCurrentAction(AnimalState currentAction) {
		if(!currentAction.equals(AnimalState.GRAZING)){
		this.currentAction = currentAction;
		}
	}

	public boolean isOnTheHunt() {
		return isOnTheHunt;
	}

	protected void setOnTheHunt(boolean isOnTheHunt) {
		this.isOnTheHunt = isOnTheHunt;
	}

	public void updateCarnivoreState(ArrayList<Animal> Animals){
		if(this.getCurrentAction().equals(AnimalState.DYING)){
			this.setAnimalActionMessage("I am dying..!!");
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
			evade();
		}
		else if(this.getLifeStrength() < (this.getMax_Life() / 2)){
			if(this.isOnTheHunt()){
				if(!Animals.contains(this.currentPrey)){
					boolean gotNewPrey = this.hunt(Animals);
					if(!gotNewPrey){
//						System.out.println("didn't get new prey");
						this.roam();
					}
					}
				else if(this.caughtPrey()){
					currentPrey.setCurrentAction(AnimalState.DYING);
					if(this.currentPrey.getLife() == 0){
						this.roam();
					}
					}
				}
			else{
				this.hunt(Animals);
				}
			}
		else if(this.getLifeStrength() >= (this.getMax_Life())){
			this.roam();
		}
	}
	
	/**
	 * To check if the carnivore sprite has caught the currently assigned prey.
	 * 
	 * @return true if the hunter and prey sprites have come into contact with each other, else returns false
	 */
	private boolean caughtPrey() {

		Rectangle hunter = new Rectangle();
		Rectangle prey = new Rectangle();
		
		hunter.x = (int) this.getX();
		hunter.y = (int) this.getY();
		hunter.width = this.getWidth();
		hunter.height = this.getHeight();
		
		prey.x = (int) this.currentPrey.getX();
		prey.y = (int) this.currentPrey.getY();
		prey.width = this.currentPrey.getWidth();
		prey.height = this.currentPrey.getHeight();
		
		if(this.checkCollision(hunter, prey)){
			return true;
		}
		else{
		return false;
		}
	}

	protected Animal findClosestPrey(ArrayList<Animal> Animals) {

		Animal prey = null;
		float closestPreyDistance = 0.00000F;
		boolean initialize = true;
		for(Animal thisAnimal: Animals){
			if(this != thisAnimal){
				if(thisAnimal instanceof Herbivore){
					if(thisAnimal.getSizeOfAnimal() <= this.getSizeOfAnimal()){
					float absDistance = calculateAbsoluteDistance(this.getX(), this.getY(),
							thisAnimal.getX(), thisAnimal.getY());
					if(initialize){
						closestPreyDistance = absDistance;
						prey = thisAnimal;
						initialize = false;
					}
//					System.out.println("Distance from " + this.getName() + " to " + thisAnimal.getName() + " is " + absDistance);
					if(absDistance < closestPreyDistance){
						closestPreyDistance = absDistance;
						prey = thisAnimal;
					}
				}
				}
			}
		}

		if(prey == null){
			initialize = true;
			for(Animal thisAnimal: Animals){
				if(this != thisAnimal){
					if(thisAnimal.getSizeOfAnimal() <= this.getSizeOfAnimal()){
						{
					float absDistance = calculateAbsoluteDistance(this.getX(), this.getY(),
							thisAnimal.getX(), thisAnimal.getY());

					if(initialize){
						closestPreyDistance = absDistance;
						prey = thisAnimal;
						initialize = false;
					}

//					System.out.println("Distance from " + this.getName() + " to " + thisAnimal.getName() + " is " + absDistance);
					if(absDistance < closestPreyDistance){
						closestPreyDistance = absDistance;
						prey = thisAnimal;
							}
						}
				}
			}
		}
		}

		return prey;
	}
	
	public void update(long timePassed){
		if(this.isOnTheHunt()){
			if(this.caughtPrey()){
			int health = currentPrey.getLife();
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