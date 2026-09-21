package com.example.pokestats.dto;

import java.util.List;

public class PokemonResponseHeight {

	private List<PokemonHeight> pokeHeight;

	public List<PokemonHeight> getPokemonInfo() {
		return pokeHeight;
	}

	public void setPokemonInfo(List<PokemonHeight> pokeHeight) {
		this.pokeHeight = pokeHeight;
	}

}
