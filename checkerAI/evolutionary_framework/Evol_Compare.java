/*
 * Ari Packer
 */

package evolutionary_framework;
import java.io.Serializable;
import java.util.Comparator;

public class Evol_Compare implements Comparator, Serializable
{
	public Evol_Compare() {}
	
	public int compare(Object puzz1, Object puzz2)
	{
		return( ((Evol_Object)puzz1).get_fitness() - ((Evol_Object)puzz2).get_fitness() );
	}
}