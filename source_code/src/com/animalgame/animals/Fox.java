package com.animalgame.animals;

import com.animalgame.animals.base.AnimalSize;
import com.animalgame.animals.base.Carnivore;
import com.animalgame.uimanagement.Animation;

public class Fox extends Carnivore {

	public Fox(String name, String food, String sound, Animation foxAnimation) {
		super(name, food, sound, foxAnimation);
		this.setMax_Life(2500);
		this.setLifeStrength(Max_Life);	
		this.setSizeOfAnimal(AnimalSize.SMALL);
	}

}
