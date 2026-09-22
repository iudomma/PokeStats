package com.example.pokestats.dto;

import java.util.ArrayList;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PokemonsGeneral {

	private int count;
	private String next;
	private String previous;
	private ArrayList<PokemonUri> results;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public ArrayList<PokemonUri> getResults() {
		return results;
	}

	public void setResults(ArrayList<PokemonUri> results) {
		this.results = results;
	}

}
