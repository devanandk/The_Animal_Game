package com.animalgame.animals;

import com.animalgame.animals.base.AnimalSize;
import com.animalgame.animals.base.Herbivore;
import com.animalgame.uimanagement.Animation;

public class Rabbit extends Herbivore {

	public Rabbit(String name, String food, String sound, Animation rabbitAnimation) {
		super(name, food, sound, rabbitAnimation);
		this.setMax_Life(1000);
		this.setLifeStrength(Max_Life);	
		this.setSizeOfAnimal(AnimalSize.TINY);
	}

}
