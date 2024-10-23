/* *****************************************************************************
 *  Name:              Yun Zhu
 *  Coursera User ID:
 *  Last modified:     August 12, 2024
 **************************************************************************** */

public class HelloGoodbye {
    public static void main(String[] args) {
        if (args.length == 2) {
            String name1 = args[0];
            String name2 = args[1];
            System.out.println("Hello " + name1 + " and " + name2 + ".");
            System.out.println("Goodbye " + name2 + " and " + name1 + ".");
        } else {
            System.out.println("Please provide two names.");
        }
    }
}
