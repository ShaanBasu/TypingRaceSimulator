package Part2;

import Part1.Typist;

/**
 * TypistWrapper extends the functionality of a Part1 Typist with GUI-specific features
 * such as color display and configuration information. This class wraps the original
 * Typist to add Part2 functionality without modifying Part1.
 * 
 * @author Your Name
 * @version 1.0
 */
public class TypistWrapper {
    
    private Typist typist;
    private String color;  // CSS color for progress bar
    private double baseAccuracy;  // Original accuracy before modifiers
    private TypistConfiguration configuration;
    
    /**
     * Creates a new TypistWrapper around an existing Typist.
     * 
     * @param typist the Part1 Typist object to wrap
     * @param color the CSS color string for GUI display
     */
    public TypistWrapper(Typist typist, String color) {
        this.typist = typist;
        this.color = color;
        this.baseAccuracy = typist.getAccuracy();
    }
    
    /**
     * Creates a new TypistWrapper with configuration information.
     */
    public TypistWrapper(Typist typist, String color, TypistConfiguration configuration) {
        this.typist = typist;
        this.color = color;
        this.baseAccuracy = typist.getAccuracy();
        this.configuration = configuration;
    }
    
    // Delegate methods to the wrapped Typist
    public void burnOut(int turns) {
        typist.burnOut(turns);
    }
    
    public void recoverFromBurnout() {
        typist.recoverFromBurnout();
    }
    
    public void recoverFromMistype() {
        typist.recoverFromMistype();
    }
    
    public double getAccuracy() {
        return typist.getAccuracy();
    }
    
    public int getProgress() {
        return typist.getProgress();
    }
    
    public String getName() {
        return typist.getName();
    }
    
    public char getSymbol() {
        return typist.getSymbol();
    }
    
    public int getBurnoutTurnsRemaining() {
        return typist.getBurnoutTurnsRemaining();
    }
    
    public void resetToStart() {
        typist.resetToStart();
        typist.setAccuracy(baseAccuracy);  // Reset to base accuracy
    }
    
    public boolean isBurntOut() {
        return typist.isBurntOut();
    }
    
    public boolean hasMistyped() {
        return typist.hasMistyped();
    }
    
    public void typeCharacter() {
        typist.typeCharacter();
    }
    
    public void slideBack(int amount) {
        typist.slideBack(amount);
    }
    
    public void setAccuracy(double newAccuracy) {
        typist.setAccuracy(newAccuracy);
    }
    
    public void setSymbol(char newSymbol) {
        typist.setSymbol(newSymbol);
    }
    
    // GUI-specific methods
    public String getColor() {
        return color;
    }
    
    public void setColor(String newColor) {
        color = newColor;
    }
    
    public double getBaseAccuracy() {
        return baseAccuracy;
    }
    
    public TypistConfiguration getConfiguration() {
        return configuration;
    }
    
    public void setConfiguration(TypistConfiguration config) {
        this.configuration = config;
    }
    
    /**
     * Returns the underlying Part1 Typist object.
     */
    public Typist getTypist() {
        return typist;
    }
}
