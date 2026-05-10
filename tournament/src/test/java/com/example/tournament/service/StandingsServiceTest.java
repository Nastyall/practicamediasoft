package com.example.tournament.service;

import com.example.tournament.dto.StandingResponse;
import com.example.tournament.model.Match;
import com.example.tournament.model.Team;
import com.example.tournament.repository.MatchRepository;
import com.example.tournament.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StandingsServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private StandingsService standingsService;

    @Test
    public void testStandingsWithNoMatchesButTeamsExist() {
        Team spartak = new Team("Spartak");
        Team zenit = new Team("Zenit");

        when(teamRepository.findAll()).thenReturn(List.of(spartak, zenit));
        when(matchRepository.findAllByMatchDateLessThanEqualOrderByMatchDateAsc(any(LocalDate.class)))
                .thenReturn(List.of());

        List<StandingResponse> standings = standingsService.getStandingsOnDate(LocalDate.of(2025, 5, 20));

        assertEquals(2, standings.size());

        for (StandingResponse standing : standings) {
            assertEquals(0, standing.getPoints());
            assertEquals(0, standing.getPlayed());
            assertEquals(0, standing.getWins());
            assertEquals(0, standing.getDraws());
            assertEquals(0, standing.getLosses());
        }
    }

    @Test
    public void testStandingsWithOneMatch() {
        Team homeTeam = new Team("Spartak");
        Team awayTeam = new Team("Zenit");

        Match match = new Match();
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setHomeGoals(2);
        match.setAwayGoals(0);
        match.setMatchDate(LocalDate.of(2025, 5, 1));

        when(teamRepository.findAll()).thenReturn(List.of(homeTeam, awayTeam));
        when(matchRepository.findAllByMatchDateLessThanEqualOrderByMatchDateAsc(any(LocalDate.class)))
                .thenReturn(List.of(match));

        List<StandingResponse> standings = standingsService.getStandingsOnDate(LocalDate.of(2025, 5, 20));

        assertEquals(2, standings.size());

        StandingResponse spartak = standings.get(0);
        assertEquals("Spartak", spartak.getTeamName());
        assertEquals(3, spartak.getPoints());
        assertEquals(1, spartak.getPlayed());
        assertEquals(1, spartak.getWins());

        StandingResponse zenit = standings.get(1);
        assertEquals("Zenit", zenit.getTeamName());
        assertEquals(0, zenit.getPoints());
    }

    @Test
    public void testStandingsWithDraw() {
        Team homeTeam = new Team("Spartak");
        Team awayTeam = new Team("Zenit");

        Match match = new Match();
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setHomeGoals(1);
        match.setAwayGoals(1);
        match.setMatchDate(LocalDate.of(2025, 5, 1));

        when(teamRepository.findAll()).thenReturn(List.of(homeTeam, awayTeam));
        when(matchRepository.findAllByMatchDateLessThanEqualOrderByMatchDateAsc(any(LocalDate.class)))
                .thenReturn(List.of(match));

        List<StandingResponse> standings = standingsService.getStandingsOnDate(LocalDate.of(2025, 5, 20));

        assertEquals(2, standings.size());
        assertEquals(1, standings.get(0).getPoints());
        assertEquals(1, standings.get(1).getPoints());
        assertEquals(1, standings.get(0).getDraws());
    }

    @Test
    public void testStandingsWithMultipleMatches() {
        Team spartak = new Team("Spartak");
        Team zenit = new Team("Zenit");
        Team cska = new Team("CSKA");

        Match match1 = new Match();
        match1.setHomeTeam(spartak);
        match1.setAwayTeam(zenit);
        match1.setHomeGoals(2);
        match1.setAwayGoals(0);
        match1.setMatchDate(LocalDate.of(2025, 5, 1));

        Match match2 = new Match();
        match2.setHomeTeam(cska);
        match2.setAwayTeam(spartak);
        match2.setHomeGoals(1);
        match2.setAwayGoals(1);
        match2.setMatchDate(LocalDate.of(2025, 5, 2));

        when(teamRepository.findAll()).thenReturn(List.of(spartak, zenit, cska));
        when(matchRepository.findAllByMatchDateLessThanEqualOrderByMatchDateAsc(any(LocalDate.class)))
                .thenReturn(List.of(match1, match2));

        List<StandingResponse> standings = standingsService.getStandingsOnDate(LocalDate.of(2025, 5, 20));

        assertEquals(3, standings.size());

        StandingResponse spartakStanding = standings.stream()
                .filter(s -> s.getTeamName().equals("Spartak"))
                .findFirst()
                .orElse(null);
        assertNotNull(spartakStanding);
        assertEquals(4, spartakStanding.getPoints());
    }

    @Test
    public void testStandingsWithNullDate() {
        List<StandingResponse> standings = standingsService.getStandingsOnDate(null);
        assertTrue(standings.isEmpty());
    }
}