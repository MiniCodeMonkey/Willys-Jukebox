package control;

import model.SettingsData;

public class SettingsController
{
	public String getSetting(String name){
		return SettingsData.getInstance().get(name);
	}
	
	public void setSetting(String name, String value){
		SettingsData.getInstance().set(name, value);
	}
	
}
