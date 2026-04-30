package Part2;

/**
 * TypistConfiguration encapsulates all customization options for a typist,
 * including typing style, keyboard type, appearance, and accessories.
 * This class calculates how each customization affects the typist's
 * accuracy, speed, and burnout characteristics.
 * 
 * @author Shaan Basu
 * @version 1.0
 */
public class TypistConfiguration {
    
    public enum TypingStyle {
        TOUCH_TYPIST(0.0, 1.0, 1.0),           // No modifier, baseline speed, baseline burnout
        HUNT_AND_PECK(-0.15, 0.8, 0.9),        // Lower accuracy, slower speed, less burnout
        PHONE_THUMBS(-0.25, 0.6, 1.2),         // Much lower accuracy, much slower, more burnout
        VOICE_TO_TEXT(-0.10, 1.3, 0.7);        // Slightly lower accuracy, faster, less burnout
        
        private double accuracyModifier;
        private double speedModifier;
        private double burnoutModifier;
        
        TypingStyle(double accuracyMod, double speedMod, double burnoutMod) {
            this.accuracyModifier = accuracyMod;
            this.speedModifier = speedMod;
            this.burnoutModifier = burnoutMod;
        }
        
        public double getAccuracyModifier() { return accuracyModifier; }
        public double getSpeedModifier() { return speedModifier; }
        public double getBurnoutModifier() { return burnoutModifier; }
    }
    
    public enum KeyboardType {
        MECHANICAL(0.05, 1.0, 1.0),            // Better accuracy, baseline speed, baseline burnout
        MEMBRANE(0.0, 0.95, 1.0),              // Baseline accuracy, slightly slower, baseline burnout
        TOUCHSCREEN(-0.10, 0.9, 1.1),          // Lower accuracy, slower, more burnout
        STENOGRAPHY(0.10, 1.2, 0.8);           // Better accuracy, faster, less burnout
        
        private double accuracyModifier;
        private double speedModifier;
        private double burnoutModifier;
        
        KeyboardType(double accuracyMod, double speedMod, double burnoutMod) {
            this.accuracyModifier = accuracyMod;
            this.speedModifier = speedMod;
            this.burnoutModifier = burnoutMod;
        }
        
        public double getAccuracyModifier() { return accuracyModifier; }
        public double getSpeedModifier() { return speedModifier; }
        public double getBurnoutModifier() { return burnoutModifier; }
    }
    
    public enum Accessory {
        WRIST_SUPPORT(0.0, 1.0, 0.7),          // No accuracy change, baseline speed, less burnout
        ENERGY_DRINK(0.08, 1.0, 1.0),          // Slightly better accuracy, baseline speed, baseline burnout
        NOISE_CANCELLING(0.05, 1.0, 1.0);      // Better accuracy (fewer mistypes), baseline speed, baseline burnout
        
        private double accuracyModifier;
        private double speedModifier;
        private double burnoutModifier;
        
        Accessory(double accuracyMod, double speedMod, double burnoutMod) {
            this.accuracyModifier = accuracyMod;
            this.speedModifier = speedMod;
            this.burnoutModifier = burnoutMod;
        }
        
        public double getAccuracyModifier() { return accuracyModifier; }
        public double getSpeedModifier() { return speedModifier; }
        public double getBurnoutModifier() { return burnoutModifier; }
    }
    
    private String typistName;
    private String symbol;  // Can be emoji or character
    private String color;   // CSS color for the progress bar
    private TypingStyle typingStyle;
    private KeyboardType keyboardType;
    private Accessory accessory;  // Optional; can be null
    
    /**
     * Creates a new TypistConfiguration with all customization options.
     */
    public TypistConfiguration(String name, String symbol, String color,
                               TypingStyle style, KeyboardType keyboard, Accessory accessory) {
        this.typistName = name;
        this.symbol = symbol;
        this.color = color;
        this.typingStyle = style;
        this.keyboardType = keyboard;
        this.accessory = accessory;
    }
    
    /**
     * Calculates the effective accuracy modifier based on all customizations.
     */
    public double calculateAccuracyModifier() {
        double modifier = typingStyle.getAccuracyModifier() + keyboardType.getAccuracyModifier();
        if (accessory != null) {
            modifier += accessory.getAccuracyModifier();
        }
        return modifier;
    }
    
    /**
     * Calculates the effective speed modifier based on all customizations.
     */
    public double calculateSpeedModifier() {
        double modifier = typingStyle.getSpeedModifier() * keyboardType.getSpeedModifier();
        if (accessory != null) {
            modifier *= accessory.getSpeedModifier();
        }
        return modifier;
    }
    
    /**
     * Calculates the effective burnout modifier based on all customizations.
     */
    public double calculateBurnoutModifier() {
        double modifier = typingStyle.getBurnoutModifier() * keyboardType.getBurnoutModifier();
        if (accessory != null) {
            modifier *= accessory.getBurnoutModifier();
        }
        return modifier;
    }
    
    // Getters for typist configuration details
    /**
     * Returns the typist's name.
     */
    public String getTypistName() { 
        return typistName; 
    }
    
    /**
     * Returns the symbol or emoji representing this typist.
     */
    public String getSymbol() { 
        return symbol; 
    }
    
    /**
     * Returns the CSS color for this typist's display.
     */
    public String getColor() { 
        return color; 
    }
    
    /**
     * Returns the typing style selected for this typist.
     */
    public TypingStyle getTypingStyle() { 
        return typingStyle; 
    }
    
    /**
     * Returns the keyboard type selected for this typist.
     */
    public KeyboardType getKeyboardType() { 
        return keyboardType; 
    }
    
    /**
     * Returns the accessory selected for this typist (may be null).
     */
    public Accessory getAccessory() { 
        return accessory; 
    }
    
    /**
     * Returns a formatted description of all customization choices and their impacts.
     */
    public String getAttributeImpactDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Typing Style: ").append(typingStyle).append("\n");
        sb.append("  Accuracy Impact: ").append(String.format("%.1f%%", typingStyle.getAccuracyModifier() * 100)).append("\n");
        sb.append("  Speed Impact: ").append(String.format("%.1f%%", (typingStyle.getSpeedModifier() - 1.0) * 100)).append("\n");
        sb.append("  Burnout Impact: ").append(String.format("%.1f%%", (typingStyle.getBurnoutModifier() - 1.0) * 100)).append("\n\n");
        
        sb.append("Keyboard Type: ").append(keyboardType).append("\n");
        sb.append("  Accuracy Impact: ").append(String.format("%.1f%%", keyboardType.getAccuracyModifier() * 100)).append("\n");
        sb.append("  Speed Impact: ").append(String.format("%.1f%%", (keyboardType.getSpeedModifier() - 1.0) * 100)).append("\n");
        sb.append("  Burnout Impact: ").append(String.format("%.1f%%", (keyboardType.getBurnoutModifier() - 1.0) * 100)).append("\n\n");
        
        if (accessory != null) {
            sb.append("Accessory: ").append(accessory).append("\n");
            sb.append("  Accuracy Impact: ").append(String.format("%.1f%%", accessory.getAccuracyModifier() * 100)).append("\n");
            sb.append("  Speed Impact: ").append(String.format("%.1f%%", (accessory.getSpeedModifier() - 1.0) * 100)).append("\n");
            sb.append("  Burnout Impact: ").append(String.format("%.1f%%", (accessory.getBurnoutModifier() - 1.0) * 100)).append("\n");
        }
        
        return sb.toString();
    }
}
