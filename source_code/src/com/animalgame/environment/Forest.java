package com.animalgame.environment;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.imageio.ImageIO;

import com.animalgame.animals.Cat;
import com.animalgame.animals.Dog;
import com.animalgame.animals.Fox;
import com.animalgame.animals.Hippopotamus;
import com.animalgame.animals.Lion;
import com.animalgame.animals.Rabbit;
import com.animalgame.animals.Tiger;
import com.animalgame.animals.base.Animal;
import com.animalgame.animals.base.Carnivore;
import com.animalgame.animals.base.Herbivore;
import com.animalgame.uimanagement.Animation;

public class Forest {

	private ArrayList<Animal> Animals;
	private ArrayList<Grass> grass;
	private int forestSizeX;
	private int forestSizeY;
	private int vanishingPoint;
	private TimeOfDay timeOfDay;

	private ClassLoader classLoader;
	InputStream input;

	private Image forestBackground[];
	private int currentBackground;

	private Image forestForeground[];
	private int currentForeground;

	private Image grassImg;
	private Animation grassAnimation;
	public static final int MAX_GRASS_COUNT = 10;

	public Forest() {
		Animals = new ArrayList<Animal>();
		grass = new ArrayList<Grass>();
		classLoader = Thread.currentThread().getContextClassLoader();
		timeOfDay = TimeOfDay.NIGHT;

		forestBackground = new Image[2];
		currentBackground = 0;

		forestForeground = new Image[2];
		currentForeground = 0;

		try {
			input = classLoader.getResourceAsStream("resources/background_day.png");
			forestBackground[0] = ImageIO.read(input);

			input = classLoader.getResourceAsStream("resources/nightbackground.png");
			forestBackground[1] = ImageIO.read(input);

			input = classLoader.getResourceAsStream("resources/foreground_day.png");
			forestForeground[0] = ImageIO.read(input);

			input = classLoader.getResourceAsStream("resources/foreground_night.png");
			forestForeground[1] = ImageIO.read(input);

			input = classLoader.getResourceAsStream("resources/grass.png");
			grassImg = ImageIO.read(input);

			grassAnimation = new Animation();

			grassAnimation.addScene(grassImg, 1000);

		} catch (IOException e) {
			System.out.println("error in reading forest background.");
			e.printStackTrace();
		}

	}

	public void addAnimal(Animal newAnimal){
		try{
			Animals.add(newAnimal);
		}
		catch(Exception er){
			System.out.println("error in adding animal");
			er.printStackTrace();
		}
	}

	public void addGrass(){
		try{
			if(this.getGrassCount() <= Forest.MAX_GRASS_COUNT){
				Grass newGrass = new Grass(grassAnimation);
				newGrass.setGrassPosition(forestSizeX, forestSizeY, vanishingPoint);
				grass.add(newGrass);
			}
		}
		catch(Exception er){
			System.out.println("error in adding grass");
			er.printStackTrace();
		}
	}

	public int getGrassCount(){
		return grass.size();
	}

	public void changeTimeOfDay(){
		if(timeOfDay == TimeOfDay.DAY){
			timeOfDay = TimeOfDay.NIGHT;
			currentBackground = 1;
			currentForeground = 1;
			for(Animal currAnimal: Animals){
				currAnimal.sleep();
			}
		}
		else if(timeOfDay == TimeOfDay.NIGHT){
			timeOfDay = TimeOfDay.DAY;
			currentBackground = 0;
			currentForeground = 0;
		}
	}

	public synchronized void moveAnimals(long timePassed){
		try{
			if(timeOfDay == TimeOfDay.NIGHT){
				for(Animal currAnimal: Animals){
					currAnimal.changeSoundState(false);
				}
			}
			else{
				for(Animal currAnimal: Animals){
					decideAnimalAction(currAnimal);
					currAnimal.changeSoundState(Math.random());
					currAnimal.update(timePassed);
				}
			}
		}
		catch(ConcurrentModificationException e){

		}
	}

	private void decideAnimalAction(Animal currAnimal) {

		if(currAnimal instanceof Lion){
			((Lion)currAnimal).move(forestSizeX, forestSizeY, vanishingPoint);
		}
		else if(currAnimal instanceof Tiger){
			((Tiger)currAnimal).move(forestSizeX, forestSizeY, vanishingPoint);
		}
		else if(currAnimal instanceof Fox){
			((Fox)currAnimal).move(forestSizeX, forestSizeY, vanishingPoint);
		}
		else if(currAnimal instanceof Hippopotamus){
			((Hippopotamus)currAnimal).move(forestSizeX, forestSizeY, vanishingPoint);
		}
		else if(currAnimal instanceof Dog){
			((Dog)currAnimal).move(forestSizeX, forestSizeY, vanishingPoint);;
		}
		else if(currAnimal instanceof Cat){
			((Cat)currAnimal).move(forestSizeX, forestSizeY, vanishingPoint);
		}
		else if(currAnimal instanceof Rabbit){
			((Rabbit)currAnimal).move(forestSizeX, forestSizeY, vanishingPoint);
		}
	}

	public Image getBackground(){
		return forestBackground[currentBackground];
	}

	public Image getForeground(){
		return forestForeground[currentForeground];
	}

	public int getAnimalCount(){
		return Animals.size();
	}

	public void drawForest(Graphics2D g) {

		try{	
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

			//--- drawing forest background
			g.drawImage(this.getBackground(), 0, 0, null);

			//--- 	drawing grass on forest
			//		the health remaining in the grass will be displayed below it
			this.addGrass();
			g.setFont(new Font("Arial", Font.PLAIN, 12));
			for(Grass thisGrass: grass){
				g.drawImage(thisGrass.getImage(),(int) thisGrass.getX(),(int) thisGrass.getY(), null);
				g.drawString(""+thisGrass.getRemainingStrength(), thisGrass.getX(), thisGrass.getY() + 50);
			}
			g.setFont(new Font("Arial", Font.PLAIN, 20));

			checkOverlaps();

			removeDeadAnimals();

			//---	drawing the animals on the forest floor.
			//		the animals will be scaled to match their distance from the observer to the
			//		vanishing point.
			for(Animal thisAnimal: Animals){

				setScalingFactors(thisAnimal);

				g.drawImage(thisAnimal.getImage(),(int) thisAnimal.getX(),(int) thisAnimal.getY(),
						thisAnimal.getWidth(),
						thisAnimal.getHeight(), null);

				int fontSize = (int) (20 * thisAnimal.getScalingFactor());
				if(fontSize < 15) fontSize = 15;

				g.setFont(new Font("Arial", Font.PLAIN, fontSize));
				
				if(this.timeOfDay.equals(TimeOfDay.DAY)){
				g.drawString(thisAnimal.getAnimalActionMessage(), 
						(int) thisAnimal.getX(), 
						(int) thisAnimal.getY() + thisAnimal.getHeight() + 20);
				}
				else{
					g.drawString("zzz...", (int) thisAnimal.getX(), (int) thisAnimal.getY() + thisAnimal.getHeight() + 20);
				}
			}
			g.setFont(new Font("Arial", Font.BOLD, 20));
//			g.drawImage(getForeground(), 0, 0, null);
		}
		catch(ConcurrentModificationException er){
			System.out.println("error in drawing forest : \n\n");		
			er.printStackTrace();
		}
	}

	private void removeDeadAnimals() {
		try{
			for(Animal thisAnimal: Animals){
				if(!thisAnimal.isAlive()){
					Animals.remove(thisAnimal);
				}
			}
			for(Grass thisGrass: grass){
				if(!thisGrass.isAlive()){
					grass.remove(thisGrass);
				}
			}
		}
		catch(Exception er){

		}
	}

	private void checkOverlaps() {
		try{

			for(int i = 0; i < Animals.size(); i++){
				Animal currentAnimal = Animals.get(i);
				for(int j = i + 1; j < Animals.size(); j++){
					Animal otherAnimal = Animals.get(j);
					if((otherAnimal.getY() + otherAnimal.getHeight()) < (currentAnimal.getY() + currentAnimal.getHeight())){
						Animals.remove(j);
						Animals.add(i, otherAnimal);
					}
				}
			}
		}
		catch(Exception er){
			System.out.println("error in checking overlaps");
			er.printStackTrace();
		}
	}

	private void setScalingFactors(Animal currentAnimal){
		try{
			float totalForestLength = forestSizeY - (vanishingPoint);
			float relativeAnimalPosition = (currentAnimal.getY()) - (vanishingPoint - 150);
			float scalingFactor;

			scalingFactor = relativeAnimalPosition / totalForestLength;
			currentAnimal.setScalingFactor(scalingFactor);
		}
		catch(Exception er){
			System.out.println("error in scaling image");
			er.printStackTrace();
		}
	}

	public int getForestSizeX() {
		return forestSizeX;
	}

	public void setForestSizeX(int forestSizeX) {
		this.forestSizeX = forestSizeX;
	}

	public int getForestSizeY() {
		return forestSizeY;
	}

	public void setForestSizeY(int forestSizeY) {
		this.forestSizeY = forestSizeY;
	}

	public String getTimeOfDay(){
		return this.timeOfDay.name();
	}

	private void initializeAnimalPosition(Animal thisAnimal){
		thisAnimal.setX((float)(Math.random() * (forestSizeX - thisAnimal.getWidth())));
		thisAnimal.setY((float)(Math.random() * (forestSizeY - thisAnimal.getHeight())));
		if(thisAnimal.getX() < 0) thisAnimal.setX(0);
		if(thisAnimal.getY() < vanishingPoint) thisAnimal.setY(vanishingPoint);
	}

	private Animation getAnimalAnimation(String imageAddress){
		Image animalImage;
		Animation animalAnimation = new Animation();
		try{
			input = classLoader.getResourceAsStream("resources/"+imageAddress);
			animalImage = ImageIO.read(input);
			animalAnimation.addScene(animalImage, 1000);
		}
		catch(Exception e){
			System.out.println("Error in getting animal animation.");
		}
		return animalAnimation;
	}

	public void addNewAnimal(String animalToAdd){

		switch(animalToAdd)
		{
		case "LION" :
			Carnivore lion = new Lion("Lion", "Meat", "Roar...",this.getAnimalAnimation("lion1.png"));
			initializeAnimalPosition(lion);

			this.addAnimal(lion);

			break;

		case "TIGER" :
			Carnivore tiger = new Tiger("Tiger", "Meat", "Growl...", this.getAnimalAnimation("tiger1.png"));
			initializeAnimalPosition(tiger);

			this.addAnimal(tiger);

			break;

		case "HIPPO" :
			Herbivore hippo = new Hippopotamus("Hippo", "Grass", "Grrrr...", this.getAnimalAnimation("hippo1.png"));
			initializeAnimalPosition(hippo);

			this.addAnimal(hippo);
			break;

		case "FOX" :
			Carnivore fox = new Fox("Fox", "Meat", "howwwww....", this.getAnimalAnimation("wolf1.png"));
			initializeAnimalPosition(fox);

			this.addAnimal(fox);
			break;

		case "DOG" :
			Carnivore dog = new Dog("Dog", "Meat", "Wuff...", this.getAnimalAnimation("dog1.png"));
			initializeAnimalPosition(dog);

			this.addAnimal(dog);
			break;

		case "CAT"	:
			Carnivore cat = new Cat("Cat", "Meat", "meow...", this.getAnimalAnimation("cat1.png"));
			initializeAnimalPosition(cat);

			this.addAnimal(cat);
			break;

		case "RABBIT" :
			Herbivore rabbit = new Rabbit("Rabbit", "Grass", "Squeak...", this.getAnimalAnimation("rabbit1.png"));
			initializeAnimalPosition(rabbit);

			this.addAnimal(rabbit);
			break;

		default:
			break;
		}	
	}

	public int getVanishingPoint() {
		return vanishingPoint;
	}

	public void setVanishingPoint(int vanishingPoint) {
		this.vanishingPoint = vanishingPoint;
	}

	public void updateAnimalStates() {
		try{
			for(Animal thisAnimal: Animals){

				if(thisAnimal instanceof Carnivore){
					((Carnivore)thisAnimal).updateCarnivoreState(Animals);
				}
				else{
					((Herbivore) thisAnimal).updateHerbivoreState(grass, Animals);	
				}
			}
		}
		catch(Exception e){
			System.out.println("error in updating animal states");
			e.printStackTrace();
		}
	}

	
}
