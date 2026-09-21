package com.example.pokestats.dto;

import java.util.List;

public class PokemonResponseExp {

	private List<PokemonExp> pokeExp;

	public List<PokemonExp> getPokemonInfo() {
		return pokeExp;
	}

	public void setPokemonInfo(List<PokemonExp> pokeExp) {
		this.pokeExp = pokeExp;
	}

}
