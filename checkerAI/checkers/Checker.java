package checkers;



public class Checker
{
    // You should not rely on the specific values of
        // BLACK, WHITE, and EMPTY, but you can assume that
        // they are all different and that WHITE == -BLACK.
        public static final int EMPTY = 0;
        public static final int BLACK = 1;
        public static final int WHITE = -1;
        public static final int BKing = 2;
        public static final int WKing = -2;

        public static void main(String[] args)
        {
                CheckerFrame f = new CheckerFrame();
                f.setVisible(true);
        }
}

