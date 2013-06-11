package com.animalgame.animals;

import com.animalgame.animals.base.AnimalSize;
import com.animalgame.animals.base.Carnivore;
import com.animalgame.uimanagement.Animation;

public class Dog extends Carnivore {

	public Dog(String name, String food, String sound, Animation dogAnimation) {
		super(name, food, sound, dogAnimation);
		this.setMax_Life(2000);
		this.setLifeStrength(Max_Life);	
		this.setSizeOfAnimal(AnimalSize.SMALL);
	}

}
