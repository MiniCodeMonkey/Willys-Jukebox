package control;

public class PlaylistManagerException extends Exception
{
	private static final long serialVersionUID = 3449291537116633349L;
	private String exception;
	
	public PlaylistManagerException(String exception)
	{
		this.exception = exception;
	}
	
	public String getMessage()
	{
		return exception;
	}
}
