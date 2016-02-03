import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Subset {
    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        int i = 0;
        while (!StdIn.isEmpty())
        {
            String s = StdIn.readString();
            if (i < size) {
                queue.enqueue(s);
            } else {
                int num = StdRandom.uniform(i+1);
                if (num < size) {
                    queue.dequeue();
                    queue.enqueue(s);
                }
            }
            i++;
        }
        for(i = 0; i < size; i++) {
            StdOut.println(queue.dequeue());
        }
    }
}
