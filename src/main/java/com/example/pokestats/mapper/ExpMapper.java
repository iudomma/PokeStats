package com.example.pokestats.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.pokestats.dto.PokemonExp;
import com.example.pokestats.dto.PokemonInfo;

@Mapper(componentModel = "spring")
public interface ExpMapper {

	List<PokemonExp> toPokeExp(List<PokemonInfo> pokeInfo);

	PokemonExp toPokeExp(PokemonInfo pokeInfo);

}
