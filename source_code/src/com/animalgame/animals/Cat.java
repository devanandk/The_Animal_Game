package com.animalgame.animals;

import com.animalgame.animals.base.AnimalSize;
import com.animalgame.animals.base.Carnivore;
import com.animalgame.uimanagement.Animation;

public class Cat extends Carnivore {

	public Cat(String name, String food, String sound, Animation catAnimation) {
		super(name, food, sound, catAnimation);
		this.setMax_Life(1500);
		this.setLifeStrength(Max_Life);	
		this.setSizeOfAnimal(AnimalSize.TINY);
	}
		
}
