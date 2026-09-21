package com.example.pokestats.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.pokestats.dto.PokemonInfo;
import com.example.pokestats.dto.PokemonWeight;

@Mapper(componentModel = "spring")
public interface WeightMapper {

	List<PokemonWeight> toPokeWeight(List<PokemonInfo> pokeInfo);

	PokemonWeight toPokeWeight(PokemonInfo pokeInfo);

}
