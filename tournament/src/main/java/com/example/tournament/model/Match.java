package com.example.tournament.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String season;

    @Column(name = "match_date", nullable = false)
    private LocalDate matchDate;

    @ManyToOne
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    @Column(name = "home_goals", nullable = false)
    private Integer homeGoals;

    @Column(name = "away_goals", nullable = false)
    private Integer awayGoals;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }
    public LocalDate getMatchDate() { return matchDate; }
    public void setMatchDate(LocalDate matchDate) { this.matchDate = matchDate; }
    public Team getHomeTeam() { return homeTeam; }
    public void setHomeTeam(Team homeTeam) { this.homeTeam = homeTeam; }
    public Team getAwayTeam() { return awayTeam; }
    public void setAwayTeam(Team awayTeam) { this.awayTeam = awayTeam; }
    public Integer getHomeGoals() { return homeGoals; }
    public void setHomeGoals(Integer homeGoals) { this.homeGoals = homeGoals; }
    public Integer getAwayGoals() { return awayGoals; }
    public void setAwayGoals(Integer awayGoals) { this.awayGoals = awayGoals; }
}