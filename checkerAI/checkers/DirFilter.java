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
class DirFilter extends FileFilter
{
  private String m_description = "Directory";
  public DirFilter() {}
  public String getDescription() {
      return m_description;
  }
  public boolean accept(File f) {
    if (f == null)
        return false;
    if (f.isDirectory())
        return true;
    return false;
  }
}