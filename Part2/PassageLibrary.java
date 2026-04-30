package Part2;

/**
 * PassageLibrary provides a collection of predefined passages for the typing race.
 * Passages are organized by length (short, medium, long) to give users options.
 * 
 * @author Shaan Basu
 * @version 1.0
 */
public class PassageLibrary {
    
    public static final String SHORT_PASSAGE_1 = 
        "The quick brown fox jumps over the lazy dog. It was a bright cold day in April, and the clocks were striking thirteen.";
    
    public static final String SHORT_PASSAGE_2 = 
        "Pack my box with five dozen liquor jugs. The early bird gets the worm, but the second mouse gets the cheese.";
    
    public static final String MEDIUM_PASSAGE_1 = 
        "It was a bright cold day in April, and the clocks were striking thirteen. Winston Smith, his chin nuzzled into his breast in an effort to escape the vile wind, slipped quickly through the glass doors of Victory Mansions, though not quickly enough to prevent a swirl of gritty dust from entering along with him.";
    
    public static final String MEDIUM_PASSAGE_2 = 
        "The year was 2081, and everybody was finally equal. They weren't only equal before God and the law. They were equal every which way. Nobody was smarter than anybody else. Nobody was better looking than anybody else. Nobody was stronger or quicker than anybody else.";
    
    public static final String LONG_PASSAGE_1 = 
        "It was the best of times, it was the worst of times, it was the age of wisdom, it was the age of foolishness, it was the epoch of belief, it was the epoch of incredulity, it was the season of Light, it was the season of Darkness, it was the spring of hope, it was the winter of despair, we had everything before us, we had nothing before us, we were all going direct to Heaven, we were all going direct the other way – in short, the period was so far like the present period, that some of its noisiest authorities insisted on its being received, for good or for evil, in the superlative degree of comparison only.";
    
    public static final String LONG_PASSAGE_2 = 
        "Call me Ishmael. Some years ago—never mind how long precisely—having little or no money in my purse, and nothing particular to interest me on shore, I thought I would sail about a little and see the watery part of the world. It is a way I have of driving off the spleen and regulating the circulation. Whenever I find myself growing grim about the mouth; whenever it is a damp, drizzly November in my soul; whenever I find myself involuntarily pausing before coffin warehouses, and bringing up the rear of every funeral I meet; and especially whenever my hypos get such an upper hand of me, that it requires a strong moral principle to prevent me from deliberately stepping into the street.";
    
    /**
     * Returns a passage from the library by difficulty category.
     * Supported categories: SHORT, MEDIUM, LONG
     * Returns the first passage in the category if available.
     */
    public static String getPassage(String category) {
        switch (category.toUpperCase()) {
            case "SHORT":
                return SHORT_PASSAGE_1;
            case "MEDIUM":
                return MEDIUM_PASSAGE_1;
            case "LONG":
                return LONG_PASSAGE_1;
            default:
                return SHORT_PASSAGE_1;
        }
    }
    
    /**
     * Returns a specific passage by category and index.
     * Each category has 2 passages available (index 0 or 1).
     */
    public static String getPassage(String category, int index) {
        switch (category.toUpperCase()) {
            case "SHORT":
                return index == 0 ? SHORT_PASSAGE_1 : SHORT_PASSAGE_2;
            case "MEDIUM":
                return index == 0 ? MEDIUM_PASSAGE_1 : MEDIUM_PASSAGE_2;
            case "LONG":
                return index == 0 ? LONG_PASSAGE_1 : LONG_PASSAGE_2;
            default:
                return SHORT_PASSAGE_1;
        }
    }
    
    /**
     * Returns the character length of a passage in a given category.
     */
    public static int getPassageLength(String category) {
        return getPassage(category).length();
    }
}
