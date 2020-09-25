import java.util.Scanner;


public class Thread2 implements Runnable{

    private StringNumbersCollection tree;

    Thread2(StringNumbersCollection tree) {
        this.tree = tree;
    }

    public void run() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            tree.addElement(s);
        }
    }
}





