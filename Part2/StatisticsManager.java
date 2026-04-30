package Part2;

import java.util.*;

/**
 * Manages historical race data and personal bests for all typists.
 */
public class StatisticsManager {
    private Map<String, List<RaceResult>> typistHistory;  // Typist name -> list of race results
    private Map<String, Double> personalBestWPM;  // Typist name -> best WPM
    
    public StatisticsManager() {
        this.typistHistory = new HashMap<>();
        this.personalBestWPM = new HashMap<>();
    }
    
    /**
     * Records a race result in history.
     * Void races are not recorded in statistics.
     */
    public void recordRace(RaceResult result) {
        // Skip void races - they don't count for statistics
        if (result.isVoidRace()) {
            return;
        }
        
        String typistName = result.getTypistName();
        
        // Add to history
        typistHistory.computeIfAbsent(typistName, k -> new ArrayList<>()).add(result);
        
        // Update personal best
        double wpm = result.calculateWPM();
        personalBestWPM.put(typistName, Math.max(personalBestWPM.getOrDefault(typistName, 0.0), wpm));
    }
    
    /**
     * Gets all race history for a typist.
     */
    public List<RaceResult> getTypistHistory(String typistName) {
        return typistHistory.getOrDefault(typistName, new ArrayList<>());
    }
    
    /**
     * Gets personal best WPM for a typist.
     */
    public double getPersonalBestWPM(String typistName) {
        return personalBestWPM.getOrDefault(typistName, 0.0);
    }
    
    /**
     * Gets average WPM across all races for a typist.
     */
    public double getAverageWPM(String typistName) {
        List<RaceResult> history = getTypistHistory(typistName);
        if (history.isEmpty()) return 0;
        return history.stream()
                .mapToDouble(RaceResult::calculateWPM)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Gets average accuracy across all races for a typist.
     */
    public double getAverageAccuracy(String typistName) {
        List<RaceResult> history = getTypistHistory(typistName);
        if (history.isEmpty()) return 0;
        return history.stream()
                .mapToDouble(RaceResult::getFinalAccuracy)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Gets total races completed by a typist.
     */
    public int getTotalRaces(String typistName) {
        return getTypistHistory(typistName).size();
    }
    
    /**
     * Gets number of 1st place finishes for a typist.
     */
    public int getFirstPlaceCount(String typistName) {
        return (int) getTypistHistory(typistName).stream()
                .filter(r -> r.getPosition() == 1)
                .count();
    }
    
    /**
     * Gets total burnout events for a typist.
     */
    public int getTotalBurnoutEvents(String typistName) {
        return (int) getTypistHistory(typistName).stream()
                .mapToInt(RaceResult::getBurnoutCount)
                .sum();
    }
    
    /**
     * Gets all tracked typists.
     */
    public Set<String> getAllTypists() {
        return typistHistory.keySet();
    }
    
    /**
     * Gets total races completed across all typists.
     */
    public int getTotalRaces() {
        return (int) typistHistory.values().stream()
                .flatMap(List::stream)
                .count();
    }
    
    /**
     * Gets total burnout events across all typists.
     */
    public int getTotalBurnoutEvents() {
        return (int) typistHistory.values().stream()
                .flatMap(List::stream)
                .mapToInt(RaceResult::getBurnoutCount)
                .sum();
    }
}
