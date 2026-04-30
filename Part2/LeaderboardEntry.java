package Part2;

/**
 * Represents a typist's cumulative statistics and rewards on the leaderboard.
 */
public class LeaderboardEntry implements Comparable<LeaderboardEntry> {
    private String typistName;
    private int cumulativePoints;
    private int firstPlaceWins;
    private int secondPlaceWins;
    private int thirdPlaceWins;
    private double totalEarnings;
    private int totalRaces;
    private int currentWinStreak;
    private int bestWinStreak;
    private int racesWithoutBurnout;
    private String title;  // Title/badge
    
    public LeaderboardEntry(String typistName) {
        this.typistName = typistName;
        this.cumulativePoints = 0;
        this.firstPlaceWins = 0;
        this.secondPlaceWins = 0;
        this.thirdPlaceWins = 0;
        this.totalEarnings = 0;
        this.totalRaces = 0;
        this.currentWinStreak = 0;
        this.bestWinStreak = 0;
        this.racesWithoutBurnout = 0;
        this.title = "Novice";
    }
    
    public void addRaceResult(RaceResult result) {
        totalRaces++;
        
        // Track placements
        if (result.getPosition() == 1) {
            firstPlaceWins++;
            currentWinStreak++;
        } else {
            currentWinStreak = 0;
        }
        
        if (result.getPosition() == 2) {
            secondPlaceWins++;
        }
        
        if (result.getPosition() == 3) {
            thirdPlaceWins++;
        }
        
        // Update best streak
        if (currentWinStreak > bestWinStreak) {
            bestWinStreak = currentWinStreak;
        }
        
        // Track burnout-free races
        if (result.getBurnoutCount() == 0) {
            racesWithoutBurnout++;
        }
    }
    
    public void addPoints(int points) {
        this.cumulativePoints += points;
    }
    
    public void addEarnings(double earnings) {
        this.totalEarnings += earnings;
    }
    
    @Override
    public int compareTo(LeaderboardEntry other) {
        // Sort by cumulative points (descending), then by total races (descending)
        if (this.cumulativePoints != other.cumulativePoints) {
            return Integer.compare(other.cumulativePoints, this.cumulativePoints);
        }
        return Integer.compare(other.totalRaces, this.totalRaces);
    }
    
    // Getters and Setters
    public String getTypistName() {
        return typistName;
    }
    
    public int getCumulativePoints() {
        return cumulativePoints;
    }
    
    public int getFirstPlaceWins() {
        return firstPlaceWins;
    }
    
    public int getSecondPlaceWins() {
        return secondPlaceWins;
    }
    
    public int getThirdPlaceWins() {
        return thirdPlaceWins;
    }
    
    public double getTotalEarnings() {
        return totalEarnings;
    }
    
    public int getTotalRaces() {
        return totalRaces;
    }
    
    public int getCurrentWinStreak() {
        return currentWinStreak;
    }
    
    public int getBestWinStreak() {
        return bestWinStreak;
    }
    
    public int getRacesWithoutBurnout() {
        return racesWithoutBurnout;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
}
