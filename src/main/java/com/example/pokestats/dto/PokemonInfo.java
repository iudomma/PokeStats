package com.example.pokestats.dto;

public class PokemonInfo {

	private String name;
	private int height;
	private int weight;
	Long base_experience;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public Long getBase_experience() {
		return base_experience;
	}

	public void setBase_experience(Long base_experience) {
		this.base_experience = base_experience;
	}

}
