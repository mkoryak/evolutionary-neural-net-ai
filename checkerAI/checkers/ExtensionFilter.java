package checkers;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/*
 * Created on Nov 30, 2004
 */

/**
 * @author Mikhail Koryak
 *
 * u_mkoryak@umassd.edu
 */
class ExtensionFilter extends FileFilter
{
  private String m_description = "Java .class file";
  private String m_extension = "class";
  public ExtensionFilter(String ext, String desc) {
      m_description = desc;
      m_extension = ext;
  }
  public String getDescription() {
      return m_description;
  }
  public boolean accept(File f) {
    if (f == null)
        return false;
    if (f.isDirectory())
        return true;
    return f.getName().toLowerCase().endsWith(m_extension);
  }
}
