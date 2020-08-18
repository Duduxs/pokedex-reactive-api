package com.edudev.pokereactiveapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;

import com.edudev.pokereactiveapi.model.Pokemon;
import com.edudev.pokereactiveapi.repository.PokemonRepository;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class PokeReactiveApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PokeReactiveApiApplication.class, args);

	}
	@Bean
	CommandLineRunner init (ReactiveMongoOperations operations,
			PokemonRepository pokeRepository) {
		
		return args -> {
			Flux<Pokemon> pokemonFlux = Flux.just(
			new Pokemon(null, "Charmander", 4.58, "Fire", "UltraFireX"),
			new Pokemon(null, "Bulbassaur", 10.20, "Grass", "Leaft"),
			new Pokemon(null, "Squirtle", 22.58, "Water", "WaterBomb"))
			.flatMap(pokeRepository::save);
			
			pokemonFlux
					.thenMany(pokeRepository.findAll())
					.subscribe(System.out::println);
			
		};
		
	}
}
