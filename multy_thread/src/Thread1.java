import java.util.Scanner;

public class Thread1 implements Runnable {

    StringNumbersCollection tree;

    Thread1(StringNumbersCollection tree) {
        this.tree = tree;
    }

    public void run() {
        while (true) {
            System.out.println(tree.changeTree());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
