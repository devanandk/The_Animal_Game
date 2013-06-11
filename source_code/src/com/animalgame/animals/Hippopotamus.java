package com.animalgame.animals;

import com.animalgame.animals.base.AnimalSize;
import com.animalgame.animals.base.Herbivore;
import com.animalgame.uimanagement.Animation;

public class Hippopotamus extends Herbivore {

	public Hippopotamus(String name, String food, String sound, Animation hippoAnimation) {
		super(name, food, sound, hippoAnimation);
		this.setMax_Life(3000);
		this.setLifeStrength(Max_Life);	
		this.setSizeOfAnimal(AnimalSize.LARGE);
	}

}
