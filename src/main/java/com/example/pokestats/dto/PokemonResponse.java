package com.example.pokestats.dto;

import java.util.List;

public class PokemonResponse {

	private List<PokemonInfo> pokemonInfo;

	public List<PokemonInfo> getPokemonInfo() {
		return pokemonInfo;
	}

	public void setPokemonInfo(List<PokemonInfo> pokemonInfo) {
		this.pokemonInfo = pokemonInfo;
	}

}
