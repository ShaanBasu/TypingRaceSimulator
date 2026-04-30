package Part2;

import Part1.Typist;

/**
 * TypistWrapper extends the functionality of a Part1 Typist with GUI-specific features
 * such as color display and configuration information. This class wraps the original
 * Typist to add Part2 functionality without modifying Part1.
 * 
 * @author Shaan Basu
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
    
    // Methods delegated to the wrapped Typist object
    public void burnOut(int turns) {
        typist.burnOut(turns);
    }
    
    /**
     * Recovers the typist from burnout condition.
     */
    public void recoverFromBurnout() {
        typist.recoverFromBurnout();
    }
    
    /**
     * Recovers the typist from a mistype error.
     */
    public void recoverFromMistype() {
        typist.recoverFromMistype();
    }
    
    /**
     * Returns the typist's current accuracy as a decimal.
     */
    public double getAccuracy() {
        return typist.getAccuracy();
    }
    
    /**
     * Returns the typist's current progress in the passage.
     */
    public int getProgress() {
        return typist.getProgress();
    }
    
    /**
     * Returns the typist's name.
     */
    public String getName() {
        return typist.getName();
    }
    
    /**
     * Returns the character symbol representing this typist.
     */
    public char getSymbol() {
        return typist.getSymbol();
    }
    
    /**
     * Returns the remaining turns in the current burnout period.
     */
    public int getBurnoutTurnsRemaining() {
        return typist.getBurnoutTurnsRemaining();
    }
    
    /**
     * Resets the typist to the starting position with base accuracy restored.
     */
    public void resetToStart() {
        typist.resetToStart();
        typist.setAccuracy(baseAccuracy);
    }
    
    /**
     * Checks if the typist is currently burned out.
     */
    public boolean isBurntOut() {
        return typist.isBurntOut();
    }
    
    /**
     * Checks if the typist has made a typing error.
     */
    public boolean hasMistyped() {
        return typist.hasMistyped();
    }
    
    /**
     * Advances the typist by one character in the passage.
     */
    public void typeCharacter() {
        typist.typeCharacter();
    }
    
    /**
     * Moves the typist back by the specified number of characters.
     */
    public void slideBack(int amount) {
        typist.slideBack(amount);
    }
    
    /**
     * Sets the typist's accuracy to a new value.
     */
    public void setAccuracy(double newAccuracy) {
        typist.setAccuracy(newAccuracy);
    }
    
    /**
     * Changes the symbol representing this typist.
     */
    public void setSymbol(char newSymbol) {
        typist.setSymbol(newSymbol);
    }
    
    // GUI-specific methods for display and configuration
    /**
     * Returns the CSS color string for displaying this typist's progress.
     */
    public String getColor() {
        return color;
    }
    
    /**
     * Updates the CSS color for this typist's display.
     */
    public void setColor(String newColor) {
        color = newColor;
    }
    
    /**
     * Returns the original accuracy before any configuration modifiers were applied.
     */
    public double getBaseAccuracy() {
        return baseAccuracy;
    }
    
    /**
     * Returns the configuration object containing this typist's customizations.
     */
    public TypistConfiguration getConfiguration() {
        return configuration;
    }
    
    /**
     * Updates the configuration for this typist.
     */
    public void setConfiguration(TypistConfiguration config) {
        this.configuration = config;
    }
    
    /**
     * Returns the wrapped Typist object from Part1.
     */
    public Typist getTypist() {
        return typist;
    }
}
