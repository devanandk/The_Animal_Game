package com.animalgame.animals.base;

import java.awt.Rectangle;

import com.animalgame.uimanagement.Animation;
import com.animalgame.uimanagement.Sprite;


public abstract class Animal extends Sprite{

	protected String animalName;
	protected String animalSound;
	protected String animalType;
	protected String animalActionMessage;
	
	protected String food;
	protected int lifeStrength;
	protected int Max_Life;
	
	protected boolean sleeping;
	protected boolean alive;
	protected boolean soundState;
	
	protected AnimalState currentAction;
	protected AnimalSize sizeOfAnimal;
	
	protected float walkingSpeed;
	protected float runningSpeed = 0.1F;
	protected float currentSpeed;
	protected float scalingFactor;
	
	protected Animal currentHunter;
	protected boolean alreadyEvading = false;
	
	public Animal(String name, String food, String sound, String type, Animation animalScene){
		super(animalScene);
		this.animalName = name;
		this.food = food;
		this.animalSound = sound;
		this.animalType = type;
		
		lifeStrength = 1000;
		
		walkingSpeed = 0.01F;
		currentSpeed = walkingSpeed;
		this.setX(0.0F);
		this.setY(0.0F);
		this.setRandomVelocity();
		
		sleeping = false;
		alive = true;
		soundState = true;
		scalingFactor = 1.0F;
		this.setCurrentAction(AnimalState.UNDEFINED);
		setSizeOfAnimal(AnimalSize.UNDEFINED);
		this.setAnimalActionMessage("");		
	}
	
	//----------------------------------------------------------
	//			GETTERS & SETTERS
	//----------------------------------------------------------
 	public String getName() {
		return animalName;
	}
	
	public void setName(String animalName) {
		this.animalName = animalName;
	}
	
	public String getFood() {
		return food;
	}
	
	public void setFood(String food) {
		this.food = food;
	}
	
	public int getLifeStrength() {
		return lifeStrength;
	}
	
	public int getLife(){
		int subtractValue = 500;
		int life = lifeStrength - subtractValue;
		
		if(life <= 0 && life > -subtractValue){
			life += subtractValue;
		}
		else if(life <= -subtractValue){
			life = 0;
		}
		lifeStrength = lifeStrength - subtractValue;
		
		return life;
	}
	
	public void setLifeStrength(int strength) {
		this.lifeStrength = strength;
	}
	
	public void addLife(int life){
		this.lifeStrength += life;
	}
	
	public boolean isSleeping(){
		return sleeping;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public String getSound(){
		if(soundState){
		return animalSound;
		}
		else
		{
			return "";
		}
	}
	
	public float getScalingFactor() {
		return scalingFactor;
	}

	public void setScalingFactor(float scalingFactor) {
		this.scalingFactor = scalingFactor;
	}
	
	public int getWidth(){
		return (int)(super.getWidth() * scalingFactor);
	}
	
	public int getHeight(){
		return (int)(super.getHeight() * scalingFactor);
	}
	
	public AnimalState getCurrentAction() {
		return currentAction;
	}

	protected void setCurrentAction(AnimalState currentAction) {
		this.currentAction = currentAction;
	}
	
	public int getMax_Life() {
		return Max_Life;
	}

	protected void setMax_Life(int max_Life) {
		Max_Life = max_Life;
	}

	public float getAnimalSpeed() {
		return currentSpeed;
	}

	public void setAnimalSpeed(float animalSpeed) {
		this.currentSpeed = animalSpeed;
	}
	
	//---------------------------------------------------------
	//			ANIMAL BEHAVIOUR
	//---------------------------------------------------------
	
	public synchronized void changeSoundState(double d){
		int random = (int) d * 10000;
		if((random % 500) < 100)
		{
			soundState = true;
		}
		else{
			soundState = false;
		}
	}
	
	public void changeSoundState(boolean condition){
		if(condition){
			soundState = true;
		}
		else
		{
			soundState = false;
		}
	
	}
		
	public void move(int forestX, int forestY, int vanishingPoint){
		
		if((this.getX() + this.getWidth()) >= forestX){
			this.setVelocityX(-Math.abs(this.getVelocityX()));
		}
		else if((this.getY() + this.getHeight()) >= forestY){
			this.setVelocityY(-Math.abs(this.getVelocityY()));
		}
		else if(this.getX() <= 0){
			this.setVelocityX(Math.abs(this.getVelocityX()));
		}
		else if(this.getY() <= vanishingPoint){
			this.setVelocityY(Math.abs(this.getVelocityY()));
		}
		
	}
	
	public void sleep(){
		this.sleeping = true;
	}

	public void wakeUp(){
		sleeping = false;
	}

	public void kill(){
		this.alive = false;
	}
	
	public boolean isActive(){
		if(!this.isSleeping() && isAlive()){
			return true;
	}
	return false;
	}
	
	public void setRandomVelocity(){
		int random1 = ((int) (Math.random() * 100) % 2);
		int random2 = ((int) (Math.random() * 100) % 2);
		float speed = this.getAnimalSpeed();
		if(random1 == 0){
			if(random2 == 0){
				this.setVelocityX(speed);
				this.setVelocityY(speed);
			}
			else{
				this.setVelocityX(speed);
				this.setVelocityY(- speed);
			}
		}
		else{
			if(random2 == 0){
				this.setVelocityX(- speed);
				this.setVelocityY(speed);
			}
			else{
				this.setVelocityX(- speed);
				this.setVelocityY(- speed);
			}
		}
		
	}

	public void update(long timePassed){
//		if(!this.getCurrentAction().equals(AnimalState.DYING)){
		lifeStrength--;
		if(lifeStrength <= 0){
			this.kill();
		}
//		}
		super.update(timePassed);
	}
	
	protected float calculateAbsoluteDistance(float x1, float y1, float x2, float y2){
		
		float dx = x1 - x2;
		float dy = y1 - y2;
		
		float distanceSqr = (dx * dx) + (dy * dy);
		
		float absDistance = (float) Math.abs(Math.sqrt((double)distanceSqr));
		
		return absDistance;
	}

	protected void calculateTrajectory(float ax, float ay, float bx, float by, float velocity){
		
		float absDistance = this.calculateAbsoluteDistance(ax, ay, bx, by);
		float time = absDistance / velocity;
		
		float dx = ax - bx;
		float dy = ay - by;
		
		float vx = Math.abs(dx / time);
		float vy = Math.abs(dy / time);
				
		if(ax > bx){
			this.setVelocityX(-vx);
		}
		else{
			this.setVelocityX(vx);
		}
		
		if(ay > by){
			this.setVelocityY(-vy);
		}
		else{
			this.setVelocityY(vy);
		}
		if(!this.alreadyEvading && this instanceof Herbivore){
		System.out.println("Absolute Distance : " + absDistance);
		System.out.println("dx : " + dx + "\ndy : " + dy);
		System.out.println("time : " + time);
		System.out.println("vx : " + vx + "\nvy : " + vy);
		}
	}
	
	protected boolean checkCollision(Rectangle element1, Rectangle element2){
		
		if(element1.intersects(element2)){
			return true;
		}
		
		return false;
	}

	public Animal getCurrentHunter() {
		return currentHunter;
	}

	public void setCurrentHunter(Animal currentHunter) {
		this.currentHunter = currentHunter;
	}
	
	protected void evade() {
		if(!alreadyEvading){
		this.setAnimalActionMessage(this.getName() +" is trying to escape from a " + this.currentHunter.getName());
		float animalX = this.getX();
		float animalY = this.getY();
		
		float hunterX = this.currentHunter.getX();
		float hunterY = this.currentHunter.getY();
		
		float evadeSpeed = (this.runningSpeed * (2.0f/3.0f));
		System.out.println("Evade Speed : " + evadeSpeed);
		this.setAnimalSpeed(evadeSpeed);
		
		System.out.println("Animal Speed : " + this.getAnimalSpeed() + " [" + this.getVelocityX() + " : " + this.getVelocityY() + "]");
		System.out.println("Hunter Speed : " + this.currentHunter.getVelocityX() + " : " + this.currentHunter.getVelocityY());
		
		this.calculateTrajectory(animalX, animalY, hunterX, hunterY, this.getAnimalSpeed());
		System.out.println(this.getVelocityX() + " : " + this.getVelocityY() + " \n\n");
		this.setVelocityX(-1 * this.getVelocityX());
		this.setVelocityY(-1 * this.getVelocityY());
		
		alreadyEvading = true;
		}
	}

	public int getSizeOfAnimal() {
		return sizeOfAnimal.getSize();
	}

	protected void setSizeOfAnimal(AnimalSize sizeOfAnimal) {
		this.sizeOfAnimal = sizeOfAnimal;
	}

	public String getAnimalActionMessage() {
		return animalActionMessage;
	}

	protected void setAnimalActionMessage(String animalActionMessage) {
		this.animalActionMessage = animalActionMessage;
	}
}
