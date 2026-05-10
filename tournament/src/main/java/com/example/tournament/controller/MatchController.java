package com.example.tournament.controller;

import com.example.tournament.dto.MatchRequest;
import com.example.tournament.dto.StandingResponse;
import com.example.tournament.service.MatchService;
import com.example.tournament.service.StandingsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private StandingsService standingsService;

    @PostMapping
    public ResponseEntity<Void> registerMatch(@Valid @RequestBody MatchRequest request) {
        matchService.registerMatch(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/standings")
    public ResponseEntity<List<StandingResponse>> getStandings(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<StandingResponse> standings = standingsService.getStandingsOnDate(date);
        return ResponseEntity.ok(standings);
    }
}