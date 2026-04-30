package Part2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Stores the complete results and metrics for a single race.
 */
public class RaceResult {
    private String typistName;
    private int position;  // 1st, 2nd, 3rd, etc.
    private long raceTimeMs;  // Time taken to finish in milliseconds
    private double finalAccuracy;
    private double initialAccuracy;
    private int passageLength;
    private int burnoutCount;
    private LocalDateTime raceDate;
    private String passage;
    private boolean isVoidRace;  // True if race was invalidated
    private String voidReason;  // Reason why race was void
    
    /**
     * Creates a new RaceResult with performance metrics.
     */
    public RaceResult(String typistName, int position, long raceTimeMs, double finalAccuracy, 
                     double initialAccuracy, int passageLength, int burnoutCount, String passage) {
        this.typistName = typistName;
        this.position = position;
        this.raceTimeMs = raceTimeMs;
        this.finalAccuracy = finalAccuracy;
        this.initialAccuracy = initialAccuracy;
        this.passageLength = passageLength;
        this.burnoutCount = burnoutCount;
        this.passage = passage;
        this.raceDate = LocalDateTime.now();
        this.isVoidRace = false;
        this.voidReason = null;
    }
    
    // Getters
    public String getTypistName() {
        return typistName;
    }
    
    /**
     * Returns the typist's final position in the race.
     * Position 1 is first place, 2 is second, etc.
     */
    public int getPosition() {
        return position;
    }
    
    /**
     * Returns the total time taken to complete the passage in milliseconds.
     */
    public long getRaceTimeMs() {
        return raceTimeMs;
    }
    
    /**
     * Returns the typist's accuracy at the end of the race.
     * This accounts for any penalties from burning out.
     */
    public double getFinalAccuracy() {
        return finalAccuracy;
    }
    
    /**
     * Returns the typist's accuracy before any race penalties were applied.
     */
    public double getInitialAccuracy() {
        return initialAccuracy;
    }
    
    public int getPassageLength() {
        return passageLength;
    }
    
    /**
     * Returns how many times this typist burned out during the race.
     */
    public int getBurnoutCount() {
        return burnoutCount;
    }
    
    /**
     * Returns the date and time when this race was completed.
     */
    public LocalDateTime getRaceDate() {
        return raceDate;
    }
    
    /**
     * Returns the passage text that was used in this race.
     */
    public String getPassage() {
        return passage;
    }
    
    /**
     * Marks this race as void with a reason.
     */
    public void setVoidRace(String reason) {
        this.isVoidRace = true;
        this.voidReason = reason;
    }
    
    /**
     * Checks if this race is void (no valid winner).
     */
    public boolean isVoidRace() {
        return isVoidRace;
    }
    
    /**
     * Gets the reason why this race was voided.
     */
    public String getVoidReason() {
        return voidReason;
    }
    
    /**
     * Calculates the Words Per Minute (WPM) metric for this race.
     * Uses standard 5 characters per word calculation.
     * Returns 0 if no time has elapsed to avoid division errors.
     * Assumes average word length of 5 characters.
     */
    public double calculateWPM() {
        double raceTimeMinutes = raceTimeMs / 60000.0;  // Convert ms to minutes
        if (raceTimeMinutes == 0) return 0;
        double wordsTyped = passageLength / 5.0;  // Average word length
        return wordsTyped / raceTimeMinutes;
    }
    
    /**
     * Calculates accuracy change from start to end of race.
     */
    public double getAccuracyChange() {
        return finalAccuracy - initialAccuracy;
    }
    
    /**
     * Gets formatted time taken as MM:SS.
     */
    public String getFormattedTime() {
        long seconds = raceTimeMs / 1000;
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
    
    /**
     * Gets formatted race date.
     */
    public String getFormattedDate() {
        return raceDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
    }
}
