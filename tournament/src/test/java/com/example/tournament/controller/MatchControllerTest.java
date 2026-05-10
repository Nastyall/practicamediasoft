package com.example.tournament.controller;

import com.example.tournament.dto.MatchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testRegisterMatch() throws Exception {
        MatchRequest request = new MatchRequest();
        request.setSeason("2024/25");
        request.setMatchDate(LocalDate.of(2025, 5, 1));
        request.setHomeTeam("Spartak");
        request.setAwayTeam("Zenit");
        request.setHomeGoals(2);
        request.setAwayGoals(1);

        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testRegisterMatchWithSameTeams() throws Exception {
        MatchRequest request = new MatchRequest();
        request.setSeason("2024/25");
        request.setMatchDate(LocalDate.of(2025, 5, 1));
        request.setHomeTeam("Spartak");
        request.setAwayTeam("Spartak");
        request.setHomeGoals(2);
        request.setAwayGoals(1);

        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Home team and away team must be different"));
    }

    @Test
    public void testRegisterMatchWithFutureDate() throws Exception {
        MatchRequest request = new MatchRequest();
        request.setSeason("2024/25");
        request.setMatchDate(LocalDate.now().plusDays(1));
        request.setHomeTeam("Spartak");
        request.setAwayTeam("Zenit");
        request.setHomeGoals(2);
        request.setAwayGoals(1);

        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cannot register match with future date"));
    }

    @Test
    public void testGetStandings() throws Exception {
        mockMvc.perform(get("/api/matches/standings")
                        .param("date", "2025-05-20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetStandingsWithNoMatches() throws Exception {
        mockMvc.perform(get("/api/matches/standings")
                        .param("date", "2020-01-01"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testInvalidMatchRequest() throws Exception {
        MatchRequest request = new MatchRequest();
        request.setHomeGoals(-1);

        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}