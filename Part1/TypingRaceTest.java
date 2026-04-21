public class TypingRaceTest {
    public static void main(String[] args){
        System.out.println("========== TYPING RACE CLASS TESTING ==========\n");
        TypingRace test1 = new TypingRace(20);
        Typist typist1 = new Typist('1', "shaan1", 0.85);
        Typist typist2 = new Typist('2', "shaan2", 0.50);
        Typist typist3 = new Typist('3', "shaan3", 0.70);
        test1.addTypist(typist1, 1);
        test1.addTypist(typist2, 2);
        test1.addTypist(typist3, 3);
        test1.startRace();
        System.out.println("========== TYPING RACE CLASS TESTING END ==========\n");
    }
}
