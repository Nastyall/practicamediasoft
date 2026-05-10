package com.example.tournament.service;

import com.example.tournament.dto.MatchRequest;
import com.example.tournament.model.Match;
import com.example.tournament.model.Team;
import com.example.tournament.repository.MatchRepository;
import com.example.tournament.repository.TeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Transactional
    public void registerMatch(MatchRequest request) {
        if (request.getHomeTeam().equals(request.getAwayTeam())) {
            throw new IllegalArgumentException("Home team and away team must be different");
        }

        if (request.getMatchDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot register match with future date");
        }

        Team homeTeam = teamRepository.findByName(request.getHomeTeam())
                .orElseGet(() -> {
                    Team newTeam = new Team(request.getHomeTeam());
                    return teamRepository.save(newTeam);
                });

        Team awayTeam = teamRepository.findByName(request.getAwayTeam())
                .orElseGet(() -> {
                    Team newTeam = new Team(request.getAwayTeam());
                    return teamRepository.save(newTeam);
                });

        Match match = new Match();
        match.setSeason(request.getSeason());
        match.setMatchDate(request.getMatchDate());
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setHomeGoals(request.getHomeGoals());
        match.setAwayGoals(request.getAwayGoals());

        matchRepository.save(match);
    }
}