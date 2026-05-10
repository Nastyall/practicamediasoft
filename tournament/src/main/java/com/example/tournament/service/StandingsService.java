package com.example.tournament.service;

import com.example.tournament.dto.StandingResponse;
import com.example.tournament.model.Match;
import com.example.tournament.model.Standing;
import com.example.tournament.model.Team;
import com.example.tournament.repository.MatchRepository;
import com.example.tournament.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class StandingsService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    public List<StandingResponse> getStandingsOnDate(LocalDate date) {
        if (date == null) {
            return Collections.emptyList();
        }

        List<Match> matches = matchRepository.findAllByMatchDateLessThanEqualOrderByMatchDateAsc(date);
        List<Team> allTeams = teamRepository.findAll();

        Map<String, Standing> standingsMap = new HashMap<>();

        for (Team team : allTeams) {
            standingsMap.putIfAbsent(team.getName(), new Standing(team.getName()));
        }

        for (Match match : matches) {
            if (match.getHomeTeam() == null || match.getAwayTeam() == null) {
                continue;
            }

            String homeTeamName = match.getHomeTeam().getName();
            String awayTeamName = match.getAwayTeam().getName();

            standingsMap.putIfAbsent(homeTeamName, new Standing(homeTeamName));
            standingsMap.putIfAbsent(awayTeamName, new Standing(awayTeamName));

            Standing homeStanding = standingsMap.get(homeTeamName);
            Standing awayStanding = standingsMap.get(awayTeamName);

            homeStanding.addMatch();
            awayStanding.addMatch();

            if (match.getHomeGoals() > match.getAwayGoals()) {
                homeStanding.addWin();
                awayStanding.addLoss();
            } else if (match.getHomeGoals() < match.getAwayGoals()) {
                homeStanding.addLoss();
                awayStanding.addWin();
            } else {
                homeStanding.addDraw();
                awayStanding.addDraw();
            }
        }

        return standingsMap.values().stream()
                .map(s -> new StandingResponse(
                        s.getTeamName(),
                        s.getPlayed(),
                        s.getWins(),
                        s.getDraws(),
                        s.getLosses(),
                        s.getPoints()
                ))
                .sorted((a, b) -> {
                    int pointsCompare = Integer.compare(b.getPoints(), a.getPoints());
                    if (pointsCompare != 0) return pointsCompare;
                    int winsCompare = Integer.compare(b.getWins(), a.getWins());
                    if (winsCompare != 0) return winsCompare;
                    return a.getTeamName().compareTo(b.getTeamName());
                })
                .toList();
    }
}