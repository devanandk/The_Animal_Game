package com.animalgame.animals.base;

public enum AnimalSize {

	LARGE(15),
	SMALL(10),
	TINY(5),
	UNDEFINED(0);
	
	private int size;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	AnimalSize(int size){
		this.size = size;
	}
}
