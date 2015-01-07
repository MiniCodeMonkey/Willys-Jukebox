package model;

import db.daf.DAFException;
import db.daf.impl.SettingsDAF;
import db.daf.interfaces.ISettingsDAF;


public class SettingsData implements ISettingsData
{
	private static SettingsData instance = null;
	private ISettingsDAF settingsDAF = null;
	
	/**
	 * Contructor that initiates all settings
	 */
	protected SettingsData()
	{
		settingsDAF = new SettingsDAF();
		
		try
		{
			if (settingsDAF.getSettingsCount() <= 0)
			{
				setDefaultSettings();
			}
		}
		catch (DAFException e)
		{
			setDefaultSettings();
		}
	}
	
	public void setDefaultSettings()
	{
		this.set("crossFadingEnabled","false");
		this.set("crossFadeSeconds", "4");
		this.set("protectWithPasswordEnabled", "false");
		this.set("password", "");
		this.set("playlistManagerEnabled","false");
		this.set("queueSize", "7");
		this.set("artistThrottling", "1");
		this.set("songFrequency", "15");
		this.set("artistFrequency", "10");
	}

	/**
	 * gets the instance
	 * @return instance
	 */
	public static SettingsData getInstance()
	{
		if (instance == null)
		{
			instance = new SettingsData();
		}
		
		return instance;
	}
	
	
	public void set(String name, String value){
		try
		{
			settingsDAF.setSetting(name, value);
		}
		catch (DAFException e)
		{
			e.printStackTrace();
		}
	}
	
	public String get(String name)
	{
		try
		{
			return settingsDAF.getSetting(name);
		}
		catch (DAFException e)
		{
			e.printStackTrace();
		}
		
		return "";
	}
}
