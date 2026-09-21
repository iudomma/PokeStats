package com.example.pokestats.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.pokestats.dto.PokemonHeight;
import com.example.pokestats.dto.PokemonInfo;

@Mapper(componentModel = "spring")
public interface HeightMapper {

	List<PokemonHeight> toPokeHeight(List<PokemonInfo> pokeInfo);

	PokemonHeight toPokeHeight(PokemonInfo pokeInfo);

}
