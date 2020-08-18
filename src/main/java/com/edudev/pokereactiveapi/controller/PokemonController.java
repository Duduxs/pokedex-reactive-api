package com.edudev.pokereactiveapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.edudev.pokereactiveapi.model.Pokemon;
import com.edudev.pokereactiveapi.repository.PokemonRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value="/pokemons")
public class PokemonController {

	private PokemonRepository pokeRepository;
	public PokemonController(PokemonRepository pokeRepository) {this.pokeRepository = pokeRepository;}
	
	@GetMapping
	public Flux<Pokemon> getAllPokemons(){return pokeRepository.findAll();}
	

	
	@GetMapping(value="/{id}")
	public Mono<ResponseEntity<Pokemon>> getPokemon(@PathVariable String id){
		return pokeRepository.findById(id)
				.map(pokemon -> ResponseEntity.ok(pokemon))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Pokemon> savePokemon(@RequestBody Pokemon pokemon){
		return pokeRepository.insert(pokemon);
	}
	
	@PutMapping(value="{id}")
	public Mono<ResponseEntity<Pokemon>> updatePokemon(@PathVariable(value="id") String id, @RequestBody Pokemon pokemon){
		
		return pokeRepository.findById(id)
				.flatMap(existingPokemon -> {
			existingPokemon.setName(pokemon.getName());
			existingPokemon.setWeight(pokemon.getWeight());
			existingPokemon.setCategory(pokemon.getCategory());
			existingPokemon.setHability(pokemon.getHability());
			return pokeRepository.save(existingPokemon);
			
		})
		.map(updatePokemon -> ResponseEntity.ok(updatePokemon))
		.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("{id}")
	public Mono<ResponseEntity<Void>> deletePokemon(@PathVariable(value="id") String id){
		return pokeRepository.findById(id)
			   .flatMap(existingPokemon ->
			   pokeRepository.delete(existingPokemon)
				.then(Mono.just(ResponseEntity.ok().<Void>build()))	   
				)
			   .defaultIfEmpty(ResponseEntity.notFound().build());
			   
				
	}
	@DeleteMapping
	public Mono<Void> deleteAllPokemons(){
		return pokeRepository.deleteAll();
	}
	
}
