package com.example.tournament.model;

public class Standing {
    private String teamName;
    private int played = 0;
    private int wins = 0;
    private int draws = 0;
    private int losses = 0;

    public Standing(String teamName) {
        this.teamName = teamName;
    }

    public void addMatch() { played++; }
    public void addWin() { wins++; }
    public void addDraw() { draws++; }
    public void addLoss() { losses++; }
    public int getPoints() { return wins * 3 + draws; }

    public String getTeamName() { return teamName; }
    public int getPlayed() { return played; }
    public int getWins() { return wins; }
    public int getDraws() { return draws; }
    public int getLosses() { return losses; }
}