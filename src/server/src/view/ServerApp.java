package view;


import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class ServerApp extends SingleFrameApplication
{
	
	private static final int BUILD_NO = 1001;

	/**
	 * At startup create and show the main frame of the application.
	 */
	@Override
	protected void startup()
	{
		show(new view.ServerView(this));
	}
	
	/**
	 * This method is to initialize the specified window by injecting resources.
	 * Windows shown in our application come fully initialized from the GUI
	 * builder, so this additional configuration is not needed.
	 */
	@Override
	protected void configureWindow(java.awt.Window root)
	{
	}
	
	/**
	 * A convenient static getter for the application instance.
	 * 
	 * @return the instance of ServerApp
	 */
	public static ServerApp getApplication()
	{
		return Application.getInstance(ServerApp.class);
	}
	
	/**
	 * Main method launching the application.
	 */
	public static void main(String[] args)
	{	
		System.out.println("Build " + BUILD_NO);
		launch(ServerApp.class, args);
	}
}
