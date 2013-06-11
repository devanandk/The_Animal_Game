package com.animalgame.animals;

import com.animalgame.animals.base.AnimalSize;
import com.animalgame.animals.base.AnimalState;
import com.animalgame.animals.base.Carnivore;
import com.animalgame.uimanagement.Animation;

public class Lion extends Carnivore {

	public Lion(String name, String food, String sound, Animation lionAnimation) {
		super(name, food, sound, lionAnimation);
		this.setCurrentAction(AnimalState.ROAMING);
		this.setMax_Life(3500);
		this.setLifeStrength(Max_Life);
		this.setSizeOfAnimal(AnimalSize.LARGE);
	}
	
}
