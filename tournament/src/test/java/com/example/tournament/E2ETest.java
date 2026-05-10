package com.example.tournament;

import com.example.tournament.dto.MatchRequest;
import com.example.tournament.dto.StandingResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class E2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testFullScenario() {
        MatchRequest match1 = new MatchRequest();
        match1.setSeason("2024/25");
        match1.setMatchDate(LocalDate.of(2025, 5, 1));
        match1.setHomeTeam("Spartak");
        match1.setAwayTeam("Zenit");
        match1.setHomeGoals(2);
        match1.setAwayGoals(1);

        ResponseEntity<Void> response1 = restTemplate.postForEntity("/api/matches", match1, Void.class);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        MatchRequest match2 = new MatchRequest();
        match2.setSeason("2024/25");
        match2.setMatchDate(LocalDate.of(2025, 5, 2));
        match2.setHomeTeam("CSKA");
        match2.setAwayTeam("Spartak");
        match2.setHomeGoals(1);
        match2.setAwayGoals(1);

        ResponseEntity<Void> response2 = restTemplate.postForEntity("/api/matches", match2, Void.class);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<StandingResponse[]> standingsResponse = restTemplate.getForEntity(
                "/api/matches/standings?date=2025-05-20",
                StandingResponse[].class
        );

        assertThat(standingsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<StandingResponse> standings = Arrays.asList(standingsResponse.getBody());

        assertThat(standings).hasSize(3);

        StandingResponse spartak = standings.stream()
                .filter(s -> s.getTeamName().equals("Spartak"))
                .findFirst()
                .orElse(null);
        assertThat(spartak).isNotNull();
        assertThat(spartak.getPoints()).isEqualTo(4);
        assertThat(spartak.getPlayed()).isEqualTo(2);
        assertThat(spartak.getWins()).isEqualTo(1);
        assertThat(spartak.getDraws()).isEqualTo(1);

        StandingResponse cska = standings.stream()
                .filter(s -> s.getTeamName().equals("CSKA"))
                .findFirst()
                .orElse(null);
        assertThat(cska).isNotNull();
        assertThat(cska.getPoints()).isEqualTo(1);

        StandingResponse zenit = standings.stream()
                .filter(s -> s.getTeamName().equals("Zenit"))
                .findFirst()
                .orElse(null);
        assertThat(zenit).isNotNull();
        assertThat(zenit.getPoints()).isEqualTo(0);
    }
}