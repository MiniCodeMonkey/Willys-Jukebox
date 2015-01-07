package text;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Text
{
	public void Write(String wish){
		
		try{
			FileWriter file = new FileWriter("wishlist.txt", true);
			BufferedWriter out = new BufferedWriter(file);
			
			out.write(wish);
			out.write("\n");
			out.close();
		}
		catch (Exception e){
			
		}
	}
	
	public void Read(){
		try{
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec("Open wishlist.txt");
		}
		catch(Exception e){
			
		}
	}
}
