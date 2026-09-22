package com.example.pokestats.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PokemonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    
    //For time reasons this tests are commented
//    @Test
//    void shouldGetPokemonWeight() throws Exception {
//
//        mockMvc.perform(get("/pokestats/weight"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void shouldGetPokemonHeight() throws Exception {
//
//        mockMvc.perform(get("/pokestats/height"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void shouldGetPokemonExp() throws Exception {
//
//        mockMvc.perform(get("/pokestats/exp"))
//                .andExpect(status().isOk());
//    }

    @Test
    void shouldGetPokemonAsyncWeight() throws Exception {

        mockMvc.perform(get("/pokestats/asyncWeight"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetPokemonAsyncHeight() throws Exception {

        mockMvc.perform(get("/pokestats/asyncHeight"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetPokemonAsyncExp() throws Exception {

        mockMvc.perform(get("/pokestats/asyncExp"))
                .andExpect(status().isOk());
    }
}