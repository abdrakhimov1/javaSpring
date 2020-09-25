import java.util.Scanner;


public class thread2 implements Runnable{

    public static void main(String[] args) {
        new thread2().run();
    }

    private StringNumbersCollection tree = new StringNumbersCollection();

    public void run() {

        while (true) {
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            tree.addElement(s);
        }
    }

    public long getNumber(){
        synchronized (tree){
            long minimum = tree.changeTree();
        }
        return minimum;
    }
}





