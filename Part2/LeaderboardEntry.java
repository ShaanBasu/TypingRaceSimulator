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
    
    /**
     * Records a new race result and updates the typist's statistics.
     * Tracks placements, win streaks, and burnout-free races.
     */
    public void addRaceResult(RaceResult result) {
        totalRaces++;
        
        // Update placement statistics and manage win streak
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
        
        // Keep track of the longest winning streak
        if (currentWinStreak > bestWinStreak) {
            bestWinStreak = currentWinStreak;
        }
        
        // Count races completed without burning out
        if (result.getBurnoutCount() == 0) {
            racesWithoutBurnout++;
        }
    }
    
    /**
     * Adds points to this typist's cumulative score.
     */
    public void addPoints(int points) {
        this.cumulativePoints += points;
    }
    
    /**
     * Adds earnings to this typist's total winnings.
     */
    public void addEarnings(double earnings) {
        this.totalEarnings += earnings;
    }
    
    /**
     * Compares this entry with another for sorting on the leaderboard.
     * First ranks by total points, then breaks ties by number of races.
     */
    @Override
    public int compareTo(LeaderboardEntry other) {
        if (this.cumulativePoints != other.cumulativePoints) {
            return Integer.compare(other.cumulativePoints, this.cumulativePoints);
        }
        return Integer.compare(other.totalRaces, this.totalRaces);
    }
    
    /**
     * Returns this typist's name.
     */
    public String getTypistName() {
        return typistName;
    }
    
    /**
     * Returns total cumulative points earned across all races.
     */
    public int getCumulativePoints() {
        return cumulativePoints;
    }
    
    /**
     * Returns the number of first place finishes.
     */
    public int getFirstPlaceWins() {
        return firstPlaceWins;
    }
    
    /**
     * Returns the number of second place finishes.
     */
    public int getSecondPlaceWins() {
        return secondPlaceWins;
    }
    
    /**
     * Returns the number of third place finishes.
     */
    public int getThirdPlaceWins() {
        return thirdPlaceWins;
    }
    
    /**
     * Returns total currency earned across all races.
     */
    public double getTotalEarnings() {
        return totalEarnings;
    }
    
    /**
     * Returns the total number of races completed by this typist.
     */
    public int getTotalRaces() {
        return totalRaces;
    }
    
    /**
     * Returns the current consecutive win streak.
     */
    public int getCurrentWinStreak() {
        return currentWinStreak;
    }
    
    /**
     * Returns the longest win streak ever achieved.
     */
    public int getBestWinStreak() {
        return bestWinStreak;
    }
    
    /**
     * Returns the number of races completed without burning out.
     */
    public int getRacesWithoutBurnout() {
        return racesWithoutBurnout;
    }
    
    /**
     * Returns this typist's achievement title or badge.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Updates this typist's achievement title.
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
