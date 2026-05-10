package com.example.tournament.dto;

public class StandingResponse {
    private String teamName;
    private int played;
    private int wins;
    private int draws;
    private int losses;
    private int points;

    public StandingResponse(String teamName, int played, int wins, int draws, int losses, int points) {
        this.teamName = teamName;
        this.played = played;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.points = points;
    }

    public String getTeamName() { return teamName; }
    public int getPlayed() { return played; }
    public int getWins() { return wins; }
    public int getDraws() { return draws; }
    public int getLosses() { return losses; }
    public int getPoints() { return points; }
}