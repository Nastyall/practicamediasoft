package com.example.tournament.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class MatchRequest {
    @NotBlank(message = "Season is required")
    private String season;

    @NotNull(message = "Match date is required")
    private LocalDate matchDate;

    @NotBlank(message = "Home team is required")
    private String homeTeam;

    @NotBlank(message = "Away team is required")
    private String awayTeam;

    @NotNull(message = "Home goals are required")
    @Min(value = 0, message = "Home goals cannot be negative")
    private Integer homeGoals;

    @NotNull(message = "Away goals are required")
    @Min(value = 0, message = "Away goals cannot be negative")
    private Integer awayGoals;

    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }
    public LocalDate getMatchDate() { return matchDate; }
    public void setMatchDate(LocalDate matchDate) { this.matchDate = matchDate; }
    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }
    public Integer getHomeGoals() { return homeGoals; }
    public void setHomeGoals(Integer homeGoals) { this.homeGoals = homeGoals; }
    public Integer getAwayGoals() { return awayGoals; }
    public void setAwayGoals(Integer awayGoals) { this.awayGoals = awayGoals; }
}