package Part2;

import java.util.*;

/**
 * Manages the reward system including points, earnings, and titles/badges.
 * Implements leaderboard ranking with cumulative points based on performance.
 */
public class RewardSystem {
    private Map<String, LeaderboardEntry> leaderboard;  // Typist name -> entry
    
    // Points allocation
    private static final int POINTS_FIRST_PLACE = 3;
    private static final int POINTS_SECOND_PLACE = 2;
    private static final int POINTS_THIRD_PLACE = 1;
    private static final double WPM_BONUS_THRESHOLD = 50.0;  // Bonus if WPM > 50
    private static final int WPM_BONUS_POINTS = 1;
    private static final int BURNOUT_PENALTY = -1;  // Points deducted per burnout
    
    // Earnings algorithm
    private static final double EARNING_FIRST_PLACE = 100.0;
    private static final double EARNING_SECOND_PLACE = 75.0;
    private static final double EARNING_THIRD_PLACE = 50.0;
    private static final double WPM_BONUS_EARNING = 0.5;  // Per WPM above threshold
    private static final double BURNOUT_PENALTY_EARNING = -10.0;  // Per burnout
    
    public RewardSystem() {
        this.leaderboard = new HashMap<>();
    }
    
    /**
     * Records a race result and awards points/earnings.
     * Void races are not recorded in the leaderboard.
     */
    public void recordRaceResult(RaceResult result) {
        // Skip void races - they don't count for leaderboard
        if (result.isVoidRace()) {
            return;
        }
        
        String typistName = result.getTypistName();
        
        // Ensure entry exists
        leaderboard.putIfAbsent(typistName, new LeaderboardEntry(typistName));
        LeaderboardEntry entry = leaderboard.get(typistName);
        
        // Add race result to entry
        entry.addRaceResult(result);
        
        // Calculate and award points
        int points = calculatePointsForRace(result);
        entry.addPoints(points);
        
        // Calculate and award earnings
        double earnings = calculateEarningsForRace(result);
        entry.addEarnings(earnings);
        
        // Update title based on achievements
        String newTitle = calculateTitle(entry);
        entry.setTitle(newTitle);
    }
    
    /**
     * Calculates points for a single race result.
     * Points based on: position (3/2/1), WPM bonus, and burnout penalty.
     */
    private int calculatePointsForRace(RaceResult result) {
        int points = 0;
        
        // Base points for position
        switch (result.getPosition()) {
            case 1:
                points += POINTS_FIRST_PLACE;
                break;
            case 2:
                points += POINTS_SECOND_PLACE;
                break;
            case 3:
                points += POINTS_THIRD_PLACE;
                break;
            default:
                points += 0;  // No points for 4th place or lower
                break;
        }
        
        // WPM bonus
        if (result.calculateWPM() > WPM_BONUS_THRESHOLD) {
            points += WPM_BONUS_POINTS;
        }
        
        // Burnout penalty
        points += result.getBurnoutCount() * BURNOUT_PENALTY;
        
        return Math.max(0, points);  // Minimum 0 points
    }
    
    /**
     * Calculates earnings for a single race result.
     * Earnings based on: position, WPM bonus, and burnout penalties.
     */
    private double calculateEarningsForRace(RaceResult result) {
        double earnings = 0;
        
        // Base earnings for position
        switch (result.getPosition()) {
            case 1:
                earnings += EARNING_FIRST_PLACE;
                break;
            case 2:
                earnings += EARNING_SECOND_PLACE;
                break;
            case 3:
                earnings += EARNING_THIRD_PLACE;
                break;
            default:
                earnings += 25.0;  // Participation earnings for 4th+
                break;
        }
        
        // WPM bonus earnings
        double wpm = result.calculateWPM();
        if (wpm > WPM_BONUS_THRESHOLD) {
            earnings += (wpm - WPM_BONUS_THRESHOLD) * WPM_BONUS_EARNING;
        }
        
        // Burnout penalties
        earnings += result.getBurnoutCount() * BURNOUT_PENALTY_EARNING;
        
        return Math.max(0, earnings);
    }
    
    /**
     * Determines the title/badge based on typist achievements.
     */
    private String calculateTitle(LeaderboardEntry entry) {
        // Speed Demon: 3 consecutive wins
        if (entry.getCurrentWinStreak() >= 3) {
            return "🔥 Speed Demon";
        }
        
        // Iron Fingers: 5 races without burnout
        if (entry.getRacesWithoutBurnout() >= 5) {
            return "💪 Iron Fingers";
        }
        
        // Champion: 10 total wins
        if (entry.getFirstPlaceWins() >= 10) {
            return "👑 Champion";
        }
        
        // Contender: 5 total wins
        if (entry.getFirstPlaceWins() >= 5) {
            return "⭐ Contender";
        }
        
        // Rising Star: 200+ points
        if (entry.getCumulativePoints() >= 200) {
            return "🌟 Rising Star";
        }
        
        // Experienced: 10+ races
        if (entry.getTotalRaces() >= 10) {
            return "📈 Experienced";
        }
        
        // Default
        if (entry.getTotalRaces() >= 3) {
            return "🏃 Racer";
        }
        
        return "🆕 Novice";
    }
    
    /**
     * Gets the leaderboard sorted by cumulative points.
     */
    public List<LeaderboardEntry> getLeaderboard() {
        List<LeaderboardEntry> sorted = new ArrayList<>(leaderboard.values());
        Collections.sort(sorted);
        return sorted;
    }
    
    /**
     * Gets a specific typist's leaderboard entry.
     */
    public LeaderboardEntry getEntry(String typistName) {
        return leaderboard.getOrDefault(typistName, new LeaderboardEntry(typistName));
    }
    
    /**
     * Gets all tracked typists on the leaderboard.
     */
    public Set<String> getAllTypists() {
        return leaderboard.keySet();
    }
}
