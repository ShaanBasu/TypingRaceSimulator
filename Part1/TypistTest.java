package Part1;

public class TypistTest {
    public static void main(String[] args) {
        System.out.println("========== TYPIST CLASS TESTING ==========\n");
        
        // Test 1: Progress cannot go below zero after calling slideBack()
        System.out.println("TEST 1: Progress cannot go below zero after slideBack()");
        System.out.println("-".repeat(50));

        Typist typist1 = new Typist('①', "TEST_TYPIST_1", 0.8);
        System.out.println("Initial progress: " + typist1.getProgress());

        typist1.typeCharacter();
        typist1.typeCharacter();
        typist1.typeCharacter();
        System.out.println("After 3 typeCharacter() calls: " + typist1.getProgress());

        typist1.slideBack(2);
        System.out.println("After slideBack(2): " + typist1.getProgress());

        typist1.slideBack(10); // Attempt to slide back more than current progress
        System.out.println("After slideBack(10) [trying to go below 0]: " + typist1.getProgress());
        System.out.println("Progress correctly clamped to 0\n");
        
        // Test 2: Burnout correctly counts down turn by turn and clears at zero
        System.out.println("TEST 2: Burnout counts down and clears at zero");
        System.out.println("-".repeat(50));

        Typist typist2 = new Typist('②', "TEST_TYPIST_2", 0.9);
        System.out.println("Initial burnout status: " + typist2.isBurntOut());
        System.out.println("Initial burnout turns remaining: " + typist2.getBurnoutTurnsRemaining());

        typist2.burnOut(3);
        System.out.println("After burnOut(3):");
        System.out.println("  - Is burnt out: " + typist2.isBurntOut());
        System.out.println("  - Turns remaining: " + typist2.getBurnoutTurnsRemaining());

        typist2.recoverFromBurnout();
        System.out.println("After 1st recoverFromBurnout():");
        System.out.println("  - Is burnt out: " + typist2.isBurntOut());
        System.out.println("  - Turns remaining: " + typist2.getBurnoutTurnsRemaining());

        typist2.recoverFromBurnout();
        System.out.println("After 2nd recoverFromBurnout():");
        System.out.println("  - Is burnt out: " + typist2.isBurntOut());
        System.out.println("  - Turns remaining: " + typist2.getBurnoutTurnsRemaining());

        typist2.recoverFromBurnout();
        System.out.println("After 3rd recoverFromBurnout():");
        System.out.println("  - Is burnt out: " + typist2.isBurntOut());
        System.out.println("  - Turns remaining: " + typist2.getBurnoutTurnsRemaining());
        System.out.println("Burnout correctly counts down and clears at zero\n");
        
        // Test 3: resetToStart() clears both progress and burnout state
        System.out.println("TEST 3: resetToStart() clears progress and burnout");
        System.out.println("-".repeat(50));

        Typist typist3 = new Typist('③', "TEST_TYPIST_3", 0.7);

        typist3.typeCharacter();
        typist3.typeCharacter();
        typist3.typeCharacter();
        typist3.typeCharacter();
        typist3.typeCharacter();
        typist3.burnOut(2);
        System.out.println("Before resetToStart():");
        System.out.println("  - Progress: " + typist3.getProgress());
        System.out.println("  - Is burnt out: " + typist3.isBurntOut());
        System.out.println("  - Burnout turns remaining: " + typist3.getBurnoutTurnsRemaining());

        typist3.resetToStart();
        System.out.println("After resetToStart():");
        System.out.println("  - Progress: " + typist3.getProgress());
        System.out.println("  - Is burnt out: " + typist3.isBurntOut());
        System.out.println("  - Burnout turns remaining: " + typist3.getBurnoutTurnsRemaining());
        System.out.println("Both progress and burnout cleared\n");
        
        // Test 4: Accuracy cannot be set outside the 0.0–1.0 range
        System.out.println("TEST 4: Accuracy clamped to 0.0 - 1.0 range");
        System.out.println("-".repeat(50));

        Typist typist4 = new Typist('④', "TEST_TYPIST_4", 0.5);
        System.out.println("Initial accuracy: " + typist4.getAccuracy());

        typist4.setAccuracy(1.5);
        System.out.println("After setAccuracy(1.5): " + typist4.getAccuracy());
        System.out.println("Clamped to 1.0");

        typist4.setAccuracy(-0.5);
        System.out.println("After setAccuracy(-0.5): " + typist4.getAccuracy());
        System.out.println("Clamped to 0.0");

        typist4.setAccuracy(0.75);
        System.out.println("After setAccuracy(0.75): " + typist4.getAccuracy());
        System.out.println("Valid value accepted\n");
        
        // Test 5: Normal forward movement via typeCharacter()
        System.out.println("TEST 5: Normal forward movement via typeCharacter()");
        System.out.println("-".repeat(50));

        Typist typist5 = new Typist('⑤', "TEST_TYPIST_5", 0.95);
        System.out.println("Initial progress: " + typist5.getProgress());

        typist5.typeCharacter();
        System.out.println("After 1st typeCharacter(): " + typist5.getProgress());

        typist5.typeCharacter();
        System.out.println("After 2nd typeCharacter(): " + typist5.getProgress());

        typist5.typeCharacter();
        System.out.println("After 3rd typeCharacter(): " + typist5.getProgress());

        typist5.typeCharacter();
        System.out.println("After 4th typeCharacter(): " + typist5.getProgress());
        System.out.println("Progress increments correctly\n");
        
        // Bonus Test: Test that typeCharacter() is blocked during burnout
        System.out.println("BONUS: typeCharacter() is blocked during burnout");
        System.out.println("-".repeat(50));

        Typist typist6 = new Typist('⑥', "TEST_TYPIST_6", 0.8);
        System.out.println("Initial progress: " + typist6.getProgress());

        typist6.typeCharacter();
        System.out.println("After typeCharacter(): " + typist6.getProgress());

        typist6.burnOut(1);
        System.out.println("After burnOut(1), is burnt out: " + typist6.isBurntOut());
        
        typist6.typeCharacter();
        System.out.println("After typeCharacter() while burnt out: " + typist6.getProgress());
        System.out.println("Progress correctly blocked during burnout\n");
        
        System.out.println("========== ALL TESTS COMPLETED ==========");
    }
}
