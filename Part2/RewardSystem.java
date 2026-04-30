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
     * Records a race result and awards points and earnings to the typist's leaderboard entry.
     * Void races are not recorded on the leaderboard.
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
     * Calculates points for a single race result based on position, WPM, and burnout.
     * First place gets 3 points, second gets 2, third gets 1.
     * Bonus point awarded for achieving 50+ WPM.
     * Penalty of 1 point per burnout event.
     */
    private int calculatePointsForRace(RaceResult result) {
        int points = 0;
        
        // Calculate base points by position
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
        
        // Add bonus for high speed performances
        if (result.calculateWPM() > WPM_BONUS_THRESHOLD) {
            points += WPM_BONUS_POINTS;
        }
        
        // Deduct points for burnout events
        points += result.getBurnoutCount() * BURNOUT_PENALTY;
        
        return Math.max(0, points);  // Ensure no negative points
    }
    
    /**
     * Calculates earnings for a race based on placement, speed, and penalties.
     * First place earns 100 coins, second earns 75, third earns 50.
     * Bonus coins awarded for WPM above 50 threshold.
     * Burnout events reduce earnings by 10 coins each.
     */
    private double calculateEarningsForRace(RaceResult result) {
        double earnings = 0;
        
        // Base earnings determined by finishing position
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
        
        // Bonus earnings for exceeding speed threshold
        double wpm = result.calculateWPM();
        if (wpm > WPM_BONUS_THRESHOLD) {
            earnings += (wpm - WPM_BONUS_THRESHOLD) * WPM_BONUS_EARNING;
        }
        
        // Deduct earnings for each burnout occurrence
        earnings += result.getBurnoutCount() * BURNOUT_PENALTY_EARNING;
        
        return Math.max(0, earnings);
    }
    
    /**
     * Assigns achievement titles based on typist accomplishments.
     * Titles unlock at different milestones for motivation and recognition.
     */
    private String calculateTitle(LeaderboardEntry entry) {
        // Check for speed demon streaks
        if (entry.getCurrentWinStreak() >= 3) {
            return "🔥 Speed Demon";
        }
        
        // Check for consistency without burning out
        if (entry.getRacesWithoutBurnout() >= 5) {
            return "💪 Iron Fingers";
        }
        
        // Champion title unlocked at 10 wins
        if (entry.getFirstPlaceWins() >= 10) {
            return "👑 Champion";
        }
        
        // Contender title at 5 wins
        if (entry.getFirstPlaceWins() >= 5) {
            return "⭐ Contender";
        }
        
        // Rising star at 200 points
        if (entry.getCumulativePoints() >= 200) {
            return "🌟 Rising Star";
        }
        
        // Experienced racer at 10 races
        if (entry.getTotalRaces() >= 10) {
            return "📈 Experienced";
        }
        
        // Newer typists get encouragement
        if (entry.getTotalRaces() >= 3) {
            return "🏃 Racer";
        }
        
        return "🆕 Novice";
    }
    
    /**
     * Returns the complete leaderboard sorted by cumulative points.
     */
    public List<LeaderboardEntry> getLeaderboard() {
        List<LeaderboardEntry> sorted = new ArrayList<>(leaderboard.values());
        Collections.sort(sorted);
        return sorted;
    }
    
    /**
     * Returns a specific typist's leaderboard entry.
     */
    public LeaderboardEntry getEntry(String typistName) {
        return leaderboard.getOrDefault(typistName, new LeaderboardEntry(typistName));
    }
    
    /**
     * Returns the set of all typists currently tracked on the leaderboard.
     */
    public Set<String> getAllTypists() {
        return leaderboard.keySet();
    }
}
