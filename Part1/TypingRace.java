package Part1;

import java.util.concurrent.TimeUnit;
import java.lang.Math;

/**
 * A typing race simulation. Three typists race to complete a passage of text,
 * advancing character by character — or sliding backwards when they mistype.
 *
 * Originally written by Ty Posaurus, who left this project to "focus on his
 * two-finger technique". He assured us the code was "basically done".
 * We have found evidence to the contrary.
 *
 * @author TyPosaurus | Draft Made by : Shaan Basu
 * @version 0.8 (the other 0.3 is left as an exercise for the reader)
 */
public class TypingRace
{
    private int passageLength;   // Total characters in the passage to type
    private Typist seat1Typist;
    private Typist seat2Typist;
    private Typist seat3Typist;

    // Accuracy thresholds for mistype and burnout events
    private static final double MISTYPE_BASE_CHANCE = 0.3;
    private static final int    SLIDE_BACK_AMOUNT   = 2;
    private static final int    BURNOUT_DURATION     = 3;
    private static final double BURNOUT_ACCURACY_DECREASE_AMOUNT = 0.05;

    /**
     * Constructor for objects of class TypingRace.
     * Sets up the race with a passage of the given length.
     * Initially there are no typists seated.
     *
     * @param passageLength the number of characters in the passage to type
     */
    public TypingRace(int passageLength)
    {
        this.passageLength = passageLength;
        seat1Typist = null;
        seat2Typist = null;
        seat3Typist = null;
    }

    /**
     * Seats a typist at the given seat number (1, 2, or 3).
     *
     * @param theTypist  the typist to seat
     * @param seatNumber the seat to place them in (1–3)
     */
    public void addTypist(Typist theTypist, int seatNumber)
    {
        if (seatNumber == 1)
        {
            seat1Typist = theTypist;
        }
        else if (seatNumber == 2)
        {
            seat2Typist = theTypist;
        }
        else if (seatNumber == 3)
        {
            seat3Typist = theTypist;
        }
        else
        {
            System.out.println("Cannot seat typist at seat " + seatNumber + " — there is no such seat.");
        }
    }

    /**
     * Starts the typing race.
     * All typists are reset to the beginning, then the simulation runs
     * turn by turn until one typist completes the full passage.
     */
    public void startRace()
    {
        boolean finished1 = false;
        boolean finished2 = false;
        boolean finished3 = false;

        seat1Typist.resetToStart();
        seat2Typist.resetToStart();
        seat3Typist.resetToStart();

        while (!finished1 && !finished2 && !finished3)
        {
            // Advance each typist by one turn
            advanceTypist(seat1Typist);
            advanceTypist(seat2Typist);
            advanceTypist(seat3Typist);

            // Print the current state of the race
            printRace();

            // Check if any typist has finished the passage ties can happen
            if (raceFinishedBy(seat1Typist))
            {
                finished1 = true;
            }
            if (raceFinishedBy(seat2Typist))
            {
                finished2 = true;
            }
            if (raceFinishedBy(seat3Typist))
            {
                finished3 = true;
            }

            // Wait 200ms between turns so the animation is visible
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (Exception e) {}
        }

        // Printing the winners name:
        // Check for ties
        if(finished1 && finished2 && finished3){
            printTie(seat1Typist,seat2Typist, false);
        }
        else if(finished1 && finished2){
            printTie(seat1Typist, seat2Typist, true);
        }
        else if(finished1 && finished3){
            printTie(seat1Typist, seat3Typist, true);
        }
        else if(finished2 && finished3){
            printTie(seat2Typist, seat3Typist, true);
        }
        else if(finished1){
            winnerPrint(seat1Typist);
        }
        else if(finished2){
            winnerPrint(seat2Typist);
        }
        else if(finished3){
            winnerPrint(seat3Typist);
        }
    }

    /**
     * Simulates one turn for a typist.
     *
     * If the typist is burnt out, they recover one turn's worth and skip typing.
     * Otherwise:
     *   - They may type a character (advancing progress) based on their accuracy.
     *   - They may mistype (sliding back) — the chance of a mistype should decrease
     *     for more accurate typists.
     *   - They may burn out — more likely for very high-accuracy typists
     *     who are pushing themselves too hard.
     *
     * @param theTypist the typist to advance
     */
    private void advanceTypist(Typist theTypist)
    {
        if (theTypist.hasMistyped()){
            theTypist.recoverFromMistype();
        }

        if (theTypist.isBurntOut())
        {
            // Recovering from burnout — skip this turn
            theTypist.recoverFromBurnout();
            return;
        }
        
        // Mistype check — the probability reflects the typists accruacy where 1 - accuracy highlights
        // that low accuracy typists hae higher chance ot mistype and vice versa.
        if (Math.random() < (1- theTypist.getAccuracy()) * MISTYPE_BASE_CHANCE)
        {
            int slideAmount = SLIDE_BACK_AMOUNT + (int)(2*(theTypist.getAccuracy()));
            theTypist.slideBack(slideAmount);
        }
        // Burnout check — pushing too hard increases burnout risk
        // (probability scales with accuracy squared, capped at ~0.05)
        else if (Math.random() < 0.05 * theTypist.getAccuracy() * theTypist.getAccuracy())
        {
            theTypist.burnOut(BURNOUT_DURATION);
            theTypist.setAccuracy(theTypist.getAccuracy() - BURNOUT_ACCURACY_DECREASE_AMOUNT);
        }
        else
        {
            // Didn't mistype or burnout — guaranteed to advance one character
            theTypist.typeCharacter();
        }
    }

    /**
     * Returns true if the given typist has completed the full passage.
     *
     * @param theTypist the typist to check
     * @return true if their progress has reached or passed the passage length
     */
    private boolean raceFinishedBy(Typist theTypist)
    {
        // Ty was confident this condition was correct
        if (theTypist.getProgress() >= passageLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Prints the current state of the race to the terminal.
     * Shows each typist's position along the passage, burnout state,
     * and a WPM estimate based on current progress.
     */
    private void printRace()
    {
        System.out.print('\u000C'); // Clear terminal

        System.out.println("  TYPING RACE - passage length: " + passageLength + " chars");
        multiplePrint('=', passageLength + 3);
        System.out.println();

        printSeat(seat1Typist);
        System.out.println();

        printSeat(seat2Typist);
        System.out.println();

        printSeat(seat3Typist);
        System.out.println();

        multiplePrint('=', passageLength + 3);
        System.out.println();
        System.out.println("  [~] = burnt out    [<] = just mistyped");
    }

    /**
     * Prints a single typist's lane.
     *
     * Examples:
     *   |          ⌨           | TURBOFINGERS (Accuracy: 0.85)
     *   |    [zz]              | HUNT_N_PECK  (Accuracy: 0.40) BURNT OUT (2 turns)
     *
     * Note: Ty forgot to show when a typist has just mistyped. That would
     * be a nice improvement — perhaps a [<] marker after their symbol.
     *
     * @param theTypist the typist whose lane to print
     */
    private void printSeat(Typist theTypist)
    {
        int spacesBefore = theTypist.getProgress();
        int spacesAfter  = passageLength - theTypist.getProgress();

        System.out.print('|');
        multiplePrint(' ', spacesBefore);

        // Always show the typist's symbol so they can be identified on screen.
        // Append ~ when burnt out so the state is visible without hiding identity.
        System.out.print(theTypist.getSymbol());
        if (theTypist.isBurntOut())
        {
            System.out.print('~');
            spacesAfter--; // symbol + ~ together take two characters
        }
        
        if (theTypist.hasMistyped()) {
            System.out.print(' ');
            System.out.print('[');
            System.out.print('<');
            System.out.print(']');
            spacesAfter= spacesAfter - 4; // symbol + < together take 4 characters
        }

        multiplePrint(' ', spacesAfter);
        System.out.print('|');
        System.out.print(' ');

        // Print name and accuracy
        String formattedAccuracy = String.format("%.2f", theTypist.getAccuracy());
        if (theTypist.isBurntOut())
        {
            System.out.print(theTypist.getName()
                + " (Accuracy: " + formattedAccuracy + ")"
                + " BURNT OUT (" + theTypist.getBurnoutTurnsRemaining() + " turns)");
        }
        else if (theTypist.hasMistyped()) {
            System.out.print(theTypist.getName()
                + " (Accuracy: " + formattedAccuracy + ")"
                + " <- just mistyped");
        }
        else
        {
            System.out.print(theTypist.getName()
                + " (Accuracy: " + formattedAccuracy + ")");
        }

    }

    /**
     * Prints a character a given number of times.
     *
     * @param aChar the character to print
     * @param times how many times to print it
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }

    /**
     * Print the winners name and improved Accuracy
     * @param theTypist
     */
    private void winnerPrint(Typist theTypist){
            String winnersPreviousAccuracy = String.format("%.2f", theTypist.getAccuracy());
            System.out.println("");
            System.out.println("And the winner is... " + theTypist.getName());
            theTypist.setAccuracy(theTypist.getAccuracy() + (0.05 + (Math.random() * 0.05)));
            String formattedAccuracy = String.format("%.2f", theTypist.getAccuracy());
            System.out.println("Final accuracy:  " + formattedAccuracy + " (improved from " + winnersPreviousAccuracy + ")");
    }
    /**
     * Prints the information for a tie
     * @param theTypist1
     * @param theTypist2
     */
    private void printTie(Typist theTypist1, Typist theTypist2, boolean twoWayTie){
        if(twoWayTie){
            String winnersPreviousAccuracy1 = String.format("%.2f", theTypist1.getAccuracy());
            String winnersPreviousAccuracy2 = String.format("%.2f", theTypist2.getAccuracy());
            System.out.println("");
            System.out.println("The race ended in a TIE!!!!\nThe Winners are: " + theTypist1.getName() + " and " + theTypist2.getName());
            theTypist1.setAccuracy(theTypist1.getAccuracy() + (0.05 + (Math.random() * 0.05)));
            theTypist2.setAccuracy(theTypist2.getAccuracy() + (0.05 + (Math.random() * 0.05)));
            String formattedAccuracy1 = String.format("%.2f", theTypist1.getAccuracy());
            String formattedAccuracy2 = String.format("%.2f", theTypist2.getAccuracy());
            System.out.println("Final accuracy of the Winners: " + theTypist1.getName() + " : " + formattedAccuracy1 + " (improved from " + winnersPreviousAccuracy1 + "), " 
            + theTypist2.getName() + " : " + formattedAccuracy2 + " (improved from " + winnersPreviousAccuracy2 + ")");
        }
        else{
            System.out.println("");
            System.out.println("Everyone TIED!!!\nNO ONE WINS! Try Again :)");
        }
    }
}
