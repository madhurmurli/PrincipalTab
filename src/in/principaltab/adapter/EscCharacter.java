package in.principaltab.adapter;

public class EscCharacter {
	
	public static String removeSplChar(String s){

		return s.replaceAll("[^a-zA-Z,/ ]+","");
	}

}
