package checkers_framework;
// Move.java
import java.io.*;
public class Move implements Serializable
{
    // The following static variable is used to represent
    // a pass.
    public static final Move pass = new Move(0, 0,0,0);

        public int currow;
        public int curcol;
        public int sucrow;
        public int succol;

        public Move(int r, int c, int r1, int c1) { currow = r; curcol = c; sucrow =r1; succol = c1;}

        public boolean isValid()
        {
                return (1 <= sucrow && sucrow <= 8 &&
                            1 <= succol && succol <= 8);
        }

        public String toString()
        {
                return "[" + currow + "][" + curcol + "] -> [" + sucrow +"][" + succol +"]";
        }

}

