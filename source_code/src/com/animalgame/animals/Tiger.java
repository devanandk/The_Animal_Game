package com.animalgame.animals;

import com.animalgame.animals.base.AnimalSize;
import com.animalgame.animals.base.Carnivore;
import com.animalgame.uimanagement.Animation;

public class Tiger extends Carnivore {

	public Tiger(String name, String food, String sound, Animation tigerAnimation) {
		super(name, food, sound, tigerAnimation);
		this.setMax_Life(3500);
		this.setLifeStrength(Max_Life);
		this.setSizeOfAnimal(AnimalSize.LARGE);
		}

}
