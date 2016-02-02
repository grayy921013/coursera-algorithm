import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Subset {
    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (!StdIn.isEmpty())
        {
            String s = StdIn.readString();
            queue.enqueue(s);
        }
        for(int i = 0; i < size; i++) {
            StdOut.println(queue.dequeue());
        }
    }
}
