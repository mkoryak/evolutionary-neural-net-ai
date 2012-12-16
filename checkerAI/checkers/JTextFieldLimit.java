package checkers;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/*
 * Created on Nov 12, 2004
 */

/**
 * @author Mikhail Koryak
 *
 * u_mkoryak@umassd.edu
 */
public class JTextFieldLimit extends PlainDocument {
    private int limit;   
    public JTextFieldLimit(int limit) {
     super();
     this.limit = limit;
    }
    public JTextFieldLimit(int limit, boolean upper) {
     super();
     this.limit = limit;
    }
    public void insertString (int offset, String  str, AttributeSet attr)
        throws BadLocationException {
     if (str == null) return;
     if ((getLength() + str.length()) <= limit) {
       super.insertString(offset, str, attr);
       }
     }
  }
