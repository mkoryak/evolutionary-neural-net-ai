/*
 * Ari Packer
 */

/*
 * Represents a variable in the boolean satisfiability problem.
 * Used internally by the ThreeSAT class.
 */

package evolutionary_framework;

public class Representation_object 
{
	private int ID;					//variables identifier
	private boolean negated;		//is this variable negated?
	
	public Representation_object() {}
	
	public Representation_object(int id, boolean neg)
	{
		ID = id;
		negated = neg;
	}
	
	public boolean isNegated()
	{
		return(negated);
	}
	
	public int getID()
	{
		return(ID);
	}	
	
	public void setID(int id)
	{
		ID = id;
	}
	
	public void setNegated(boolean neg)
	{
		negated = neg;
	}
	
	public Object clone()
	{
		Representation_object new_obj = new Representation_object();
		new_obj.setID( this.ID );
		new_obj.setNegated( this.negated );
		
		return(new_obj);
	}
}
