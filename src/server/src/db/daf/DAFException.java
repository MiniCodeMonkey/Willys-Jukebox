package db.daf;

/**
 * Exception-klasse til exceptions fra DataAccess laget
 */
public class DAFException extends Exception
{
	private static final long serialVersionUID = -5492114628089339322L;
	
	/**
	 * Benyttes til at sende exceptions op fra DataAccess laget
	 * @param message
	 *            Tilh¿rende fejlmelding
	 */
	public DAFException(String message)
	{
		super(message);
	}
	
	/**
	 * Benyttes til at sende exceptions op fra DataAccess laget
	 * @param e
	 *            Exception der skal wrappes
	 */
	public DAFException(Exception e)
	{
		super(e.getMessage());
		
		System.out.println("**DAF EXCEPTION**");
		this.printStackTrace();
	}
}