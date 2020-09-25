public class Main {

    static StringNumbersCollection tree = new StringNumbersCollection();
    static Thread2 thread2 = new Thread2(tree);
    static Thread1 thread1 = new Thread1(tree);
    public static void main(String[] args) {
        new Thread(thread1).start();
        new Thread(thread2).start();
    }
}
