package Part2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

/**
 * Enhanced TypingRace simulation supporting:
 * - Variable number of typists (2-6)
 * - Difficulty modifiers (Autocorrect, Caffeine Mode, Night Shift)
 * - Passage text display
 * - Customizable typist configurations
 * 
 * @author Shaan Basu
 * @version 1.0
 */
public class TypingRaceEnhanced {
    
    private String passage;
    private int passageLength;
    private List<TypistWrapper> typists;
    private List<Integer> raceOrder;  // Order in which typists finished
    private List<RaceResult> raceResults;  // Results with performance metrics
    private Runnable raceCompletionCallback;  // Callback when race finishes
    
    // Time tracking
    private long raceStartTimeMs;
    private Map<Integer, Long> typistFinishTimes;  // Typist index -> finish time in ms
    private Map<Integer, Integer> typistBurnoutCounts;  // Typist index -> burnout count
    private Map<Integer, Double> typistInitialAccuracy;  // Typist index -> initial accuracy
    
    // Difficulty modifiers
    private boolean autocorrectEnabled;
    private boolean caffeineMode;
    @SuppressWarnings("unused")
    private boolean nightShift;
    
    // Constants
    private static final double MISTYPE_BASE_CHANCE = 0.3;
    private static final int    SLIDE_BACK_AMOUNT   = 2;
    private static final int    BURNOUT_DURATION     = 3;
    private static final double BURNOUT_ACCURACY_DECREASE_AMOUNT = 0.05;
    
    // Caffeine mode tracking
    private int[] caffeineBoostTurns;
    private boolean[] caffeineBurnoutActive;
    
    /**
     * Constructor for enhanced typing race.
     * 
     * @param passage the passage text to type
     */
    public TypingRaceEnhanced(String passage) {
        this.passage = passage;
        this.passageLength = passage.length();
        this.typists = new ArrayList<>();
        this.raceOrder = new ArrayList<>();
        this.raceResults = new ArrayList<>();
        this.typistFinishTimes = new HashMap<>();
        this.typistBurnoutCounts = new HashMap<>();
        this.typistInitialAccuracy = new HashMap<>();
        this.autocorrectEnabled = false;
        this.caffeineMode = false;
        this.nightShift = false;
    }
    
    /**
     * Adds a typist to the race and initializes their tracking data.
     * Records initial accuracy and burnout count for later analysis.
     */
    public void addTypist(TypistWrapper typist) {
        int index = typists.size();
        typists.add(typist);
        typistInitialAccuracy.put(index, typist.getAccuracy());  // Track initial accuracy
        typistBurnoutCounts.put(index, 0);  // Initialize burnout count
    }
    
    /**
     * Enables or disables autocorrect difficulty modifier.
     * When enabled, halves the slide-back distance for mistypes.
     */
    public void setAutocorrect(boolean enabled) {
        this.autocorrectEnabled = enabled;
    }
    
    /**
     * Enables or disables caffeine mode difficulty modifier.
     * Grants a 10-turn speed boost followed by increased burnout risk.
     */
    public void setCaffeineMode(boolean enabled) {
        this.caffeineMode = enabled;
        if (enabled) {
            caffeineBoostTurns = new int[typists.size()];
            caffeineBurnoutActive = new boolean[typists.size()];
            for (int i = 0; i < typists.size(); i++) {
                caffeineBoostTurns[i] = 10;
                caffeineBurnoutActive[i] = false;
            }
        }
    }
    
    /**
     * Enables or disables night shift difficulty modifier.
     * Reduces all typists' accuracy by 15% to simulate fatigue.
     */
    public void setNightShift(boolean enabled) {
        this.nightShift = enabled;
        if (enabled) {
            // Apply accuracy reduction to all typists
            for (TypistWrapper typist : typists) {
                double reducedAccuracy = typist.getAccuracy() * 0.85;  // 15% reduction
                typist.setAccuracy(reducedAccuracy);
            }
        }
    }
    
    private Timeline raceTimeline;
    
    /**
     * Sets a callback to be called when the race completes.
     */
    public void setRaceCompletionCallback(Runnable callback) {
        this.raceCompletionCallback = callback;
    }
    
    /**
     * Starts the race and runs it until completion using a Timeline for non-blocking updates.
     */
    public void startRace() {
        // Record race start time
        raceStartTimeMs = System.currentTimeMillis();
        
        // Clear results from previous races
        raceResults.clear();
        typistFinishTimes.clear();
        
        // Reset all typists
        for (TypistWrapper typist : typists) {
            typist.resetToStart();
        }
        
        // Initialize race tracking
        raceOrder.clear();
        
        // Initialize caffeine arrays if not already done
        if (caffeineBoostTurns == null) {
            caffeineBoostTurns = new int[typists.size()];
            caffeineBurnoutActive = new boolean[typists.size()];
            for (int i = 0; i < typists.size(); i++) {
                caffeineBoostTurns[i] = 10;
                caffeineBurnoutActive[i] = false;
            }
        }
        
        // Use a Timeline to advance the race non-blocking so display can update
        // Run at 50ms per turn for faster progression and visible updates
        raceTimeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            // Advance each typist multiple times per update cycle for faster progression
            for (int cycle = 0; cycle < 2; cycle++) {
                for (int i = 0; i < typists.size(); i++) {
                    if (!hasFinished(i)) {
                        advanceTypist(i);
                    }
                }
                
                // Check for finished typists
                for (int i = 0; i < typists.size(); i++) {
                    if (typists.get(i).getProgress() >= passageLength && !hasFinished(i)) {
                        raceOrder.add(i);
                        // Record finish time for this typist
                        long finishTime = System.currentTimeMillis() - raceStartTimeMs;
                        typistFinishTimes.put(i, finishTime);
                    }
                }
            }
            
            // Stop the timeline when the first typist finishes
            if (!raceOrder.isEmpty()) {
                raceTimeline.stop();
                // Generate race results for each typist
                generateRaceResults();
                // Call completion callback if set
                if (raceCompletionCallback != null) {
                    raceCompletionCallback.run();
                }
            }
        }));
        raceTimeline.setCycleCount(Timeline.INDEFINITE);
        raceTimeline.play();
    }
    
    /**
     * Simulates one turn of typing for a specific typist.
     * Handles mistypes, burnout, recovery, and difficulty modifiers.
     */
    private void advanceTypist(int typistIndex) {
        TypistWrapper typist = typists.get(typistIndex);
        
        // Skip if typist has already finished the passage
        if (hasFinished(typistIndex)) {
            return;
        }
        
        // Clear recovery conditions from previous turns
        if (typist.hasMistyped()) {
            typist.recoverFromMistype();
        }
        
        // Handle burnout recovery
        if (typist.isBurntOut()) {
            typist.recoverFromBurnout();
            return;
        }
        
        // Apply caffeine mode boost if active
        double accuracyModifier = 0;
        if (caffeineMode && caffeineBoostTurns[typistIndex] > 0) {
            // Speed boost provides accuracy bonus
            accuracyModifier = 0.15;
            caffeineBoostTurns[typistIndex]--;
        } else if (caffeineMode && !caffeineBurnoutActive[typistIndex]) {
            // After boost ends, mark for increased burnout risk
            caffeineBurnoutActive[typistIndex] = true;
        }
        
        // Autocorrect reduces the penalty for mistypes
        int slideBackAmount = SLIDE_BACK_AMOUNT;
        if (autocorrectEnabled) {
            slideBackAmount = slideBackAmount / 2;
        }
        
        // Calculate mistype probability with modifiers
        double mistypeChance = (1 - typist.getAccuracy() - accuracyModifier) * MISTYPE_BASE_CHANCE;
        double random = Math.random();
        
        if (random < mistypeChance) {
            // Mistype occurred - typist slides back
            int slideAmount = slideBackAmount + (int)(2 * typist.getAccuracy());
            typist.slideBack(slideAmount);
        }
        // Burnout check - increased risk during caffeine crash
        else if (Math.random() < 0.05 * typist.getAccuracy() * typist.getAccuracy() 
                 * (caffeineBurnoutActive[typistIndex] ? 2.0 : 1.0)) {
            // Typist burns out
            typist.burnOut(BURNOUT_DURATION);
            typist.setAccuracy(typist.getAccuracy() - BURNOUT_ACCURACY_DECREASE_AMOUNT);
            // Track burnout for statistics
            typistBurnoutCounts.put(typistIndex, typistBurnoutCounts.get(typistIndex) + 1);
        }
        else {
            // Successfully typed character
            typist.typeCharacter();
        }
    }
    
    /**
     * Checks if a typist has finished the passage.
     */
    private boolean hasFinished(int index) {
        return raceOrder.contains(index);
    }
    
    /**
     * Generates race results for all typists after the race completes.
     * Calculates positions, handles ties, and detects void race conditions.
     */
    private void generateRaceResults() {
        raceResults.clear();
        
        // Check if race meets void conditions (improper ties)
        String voidReason = checkForVoidRace();
        
        // Determine final positions for all typists with tie handling
        Map<Integer, Integer> typistPositions = assignPositions();
        
        // Create result for each typist with all relevant metrics
        for (int i = 0; i < typists.size(); i++) {
            TypistWrapper typist = typists.get(i);
            int position = typistPositions.getOrDefault(i, typists.size());
            long raceTimeMs = typistFinishTimes.getOrDefault(i, System.currentTimeMillis() - raceStartTimeMs);
            double finalAccuracy = typist.getAccuracy();
            double initialAccuracy = typistInitialAccuracy.get(i);
            int burnoutCount = typistBurnoutCounts.getOrDefault(i, 0);
            int lengthTyped = typist.getProgress();
            
            RaceResult result = new RaceResult(
                typist.getName(),
                position,
                raceTimeMs,
                finalAccuracy,
                initialAccuracy,
                lengthTyped,
                burnoutCount,
                passage
            );
            
            // Mark result as void if applicable
            if (voidReason != null) {
                result.setVoidRace(voidReason);
            }
            
            raceResults.add(result);
        }
    }
    
    /**
     * Checks if the race should be voided due to problematic tie conditions.
     * A race is void if: 3+ typists tie for first, or 2 typists tie for first with only 2 total.
     */
    private String checkForVoidRace() {
        if (raceOrder.isEmpty()) {
            return null;
        }
        
        // Collect all typists with the same finish time as the leader
        List<Integer> firstFinishers = new ArrayList<>();
        firstFinishers.add(raceOrder.get(0));
        long firstFinishTime = typistFinishTimes.get(raceOrder.get(0));
        
        for (int i = 1; i < raceOrder.size(); i++) {
            long finishTime = typistFinishTimes.get(raceOrder.get(i));
            if (finishTime == firstFinishTime) {
                firstFinishers.add(raceOrder.get(i));
            } else {
                break;
            }
        }
        
        // Check void conditions
        if (firstFinishers.size() >= 3) {
            return "Race void due to " + firstFinishers.size() + "-way tie for first place";
        }
        
        if (firstFinishers.size() == 2 && typists.size() == 2) {
            return "Race void due to 2-way tie (2 typists total)";
        }
        
        return null;
    }
    
    /**
     * Assigns positions to all typists based on finish times and progress.
     * Valid ties are preserved, invalid ties are broken by progress then list order.
     */
    private Map<Integer, Integer> assignPositions() {
        Map<Integer, Integer> positions = new HashMap<>();
        
        if (raceOrder.isEmpty()) {
            return positions;
        }
        
        // Group typists by their finish times for tie detection
        Map<Long, List<Integer>> timeToTypists = new HashMap<>();
        for (int typistIndex : raceOrder) {
            long finishTime = typistFinishTimes.get(typistIndex);
            timeToTypists.computeIfAbsent(finishTime, k -> new ArrayList<>()).add(typistIndex);
        }
        
        // Sort finish times to process results chronologically
        List<Long> sortedTimes = new ArrayList<>(timeToTypists.keySet());
        java.util.Collections.sort(sortedTimes);
        
        int position = 1;
        for (Long finishTime : sortedTimes) {
            List<Integer> typistAtTime = timeToTypists.get(finishTime);
            
            // For non-first-place ties, break ties using progress then list order
            if (typistAtTime.size() > 1 && position > 1) {
                typistAtTime.sort((a, b) -> {
                    int progressA = typists.get(a).getProgress();
                    int progressB = typists.get(b).getProgress();
                    if (progressA != progressB) {
                        return Integer.compare(progressB, progressA);
                    }
                    return Integer.compare(a, b);
                });
            }
            
            // Assign positions to all typists at this finish time
            for (Integer typistIndex : typistAtTime) {
                positions.put(typistIndex, position);
            }
            position += typistAtTime.size();
        }
        
        return positions;
    }

    
    // Getter methods for race data
    public String getPassage() {
        return passage;
    }
    
    public int getPassageLength() {
        return passageLength;
    }
    
    public List<TypistWrapper> getTypists() {
        return typists;
    }
    
    public List<Integer> getRaceOrder() {
        return raceOrder;
    }
    
    public List<RaceResult> getRaceResults() {
        return raceResults;
    }
    
    public int getTypistCount() {
        return typists.size();
    }
}
