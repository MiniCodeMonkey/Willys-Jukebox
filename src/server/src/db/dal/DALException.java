package db.dal;

/**
 * Exception-klasse til exceptions fra DataAccess laget
 */
public class DALException extends Exception
{
	private static final long serialVersionUID = -5490114628089339322L;
	
	/**
	 * Benyttes til at sende exceptions op fra DataAccess laget
	 * @param message
	 *            Tilh¿rende fejlmelding
	 */
	public DALException(String message)
	{
		super(message);
	}
	
	/**
	 * Benyttes til at sende exceptions op fra DataAccess laget
	 * @param e
	 *            Exception der skal wrappes
	 */
	public DALException(Exception e)
	{
		super(e.getMessage());
		
		System.out.println("**DAL EXCEPTION**");
		e.printStackTrace();
		System.out.println("**END DAL EXCEPTION**");
	}
}