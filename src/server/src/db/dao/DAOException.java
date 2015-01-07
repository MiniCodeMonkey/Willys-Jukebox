package db.dao;

/**
 * DAOException klasse
 */
public class DAOException extends Exception
{
	private static final long serialVersionUID = -2465510200217174797L;

	/**
	 * Constructor
	 * @param message
	 */
	public DAOException(String message)
	{
		super(message);
	}
	
	/**
	 * Constructor
	 * @param e
	 */
	public DAOException(Exception e)
	{
		super(e.getMessage());
		
		System.out.println("**DAO EXCEPTION**");
		this.printStackTrace();
	}
}