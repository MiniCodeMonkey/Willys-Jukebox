package model;

/**
 * Exception-klasse til exceptions fra Data laget
 */
public class DataException extends Exception
{
	private static final long serialVersionUID = -5490114628289339322L;
	
	/**
	 * Benyttes til at sende exceptions op fra Data laget
	 * @param message
	 *            Tilh¿rende fejlmelding
	 */
	public DataException(String message)
	{
		super(message);
	}
	
	/**
	 * Benyttes til at sende exceptions op fra Data laget
	 * @param e
	 *            Exception der skal wrappes
	 */
	public DataException(Exception e)
	{
		super(e.getMessage());
		
		System.out.println("**DATA EXCEPTION**");
		this.printStackTrace();
	}
}