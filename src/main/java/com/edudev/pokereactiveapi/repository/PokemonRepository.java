package com.edudev.pokereactiveapi.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.edudev.pokereactiveapi.model.Pokemon;


public interface PokemonRepository extends ReactiveMongoRepository<Pokemon, String> {

}
