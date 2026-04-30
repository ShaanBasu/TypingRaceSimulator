package Part2;

/**
 * Represents a sponsor that offers bonuses for specific achievements.
 */
public class Sponsor {
    private String name;
    private String description;
    private double bonusAmount;
    private SponsorBonus bonusType;
    
    public enum SponsorBonus {
        NO_BURNOUT("Complete without burning out", 50),
        FIRST_PLACE("Win the race", 100),
        HIGH_WPM_50("Achieve 50+ WPM", 30),
        HIGH_WPM_75("Achieve 75+ WPM", 75),
        PERFECT_ACCURACY("Maintain 95%+ accuracy", 60),
        CONSISTENCY("Win 3 consecutive races", 200);
        
        private String description;
        private double defaultBonus;
        
        SponsorBonus(String description, double defaultBonus) {
            this.description = description;
            this.defaultBonus = defaultBonus;
        }
        
        public String getDescription() {
            return description;
        }
        
        public double getDefaultBonus() {
            return defaultBonus;
        }
    }
    
    public Sponsor(String name, SponsorBonus bonusType) {
        this.name = name;
        this.bonusType = bonusType;
        this.description = bonusType.getDescription();
        this.bonusAmount = bonusType.getDefaultBonus();
    }
    
    public boolean evaluateBonus(RaceResult result, LeaderboardEntry entry) {
        switch (bonusType) {
            case NO_BURNOUT:
                return result.getBurnoutCount() == 0;
            case FIRST_PLACE:
                return result.getPosition() == 1;
            case HIGH_WPM_50:
                return result.calculateWPM() >= 50;
            case HIGH_WPM_75:
                return result.calculateWPM() >= 75;
            case PERFECT_ACCURACY:
                return result.getFinalAccuracy() >= 0.95;
            case CONSISTENCY:
                return entry.getCurrentWinStreak() >= 3;
            default:
                return false;
        }
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public double getBonusAmount() {
        return bonusAmount;
    }
    
    public SponsorBonus getBonusType() {
        return bonusType;
    }
    
    @Override
    public String toString() {
        return name + ": " + description + " (+" + (int)bonusAmount + " coins)";
    }
}
