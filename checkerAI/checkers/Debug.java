package checkers;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Debug {
  public static boolean on = false;
  public static boolean printBoard = false;
  public Debug() {
  }
  public static void println(String s) {
    if(on)
      System.out.println(s);
  }
  public static void println(String s,boolean b) {
  if(!b)
    System.out.println(s);
  }
  public static void println() {
    if(on)
      System.out.println();
  }
  public static void print(String s) {
    if(on)
    System.out.print(s);
  }
}