package games;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cards implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7204321605113303720L;
	private int value;
	private String faceValue;
	private String fileName;
	
	Cards(String name, String faceValue, int value)
	{
		this.faceValue = faceValue;
		this.value = value; 
		this.fileName = name;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public String getFaceValue()
	{
		return faceValue;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public static List<Cards> generateCardsList()
	{
		List<Cards> cards = new ArrayList<>();
		for (int i=1; i < 53; i++)
		{
			int value = i % 13; // range from 1-13
			String faceValue;
			switch (value)
			{
			case 11:
				faceValue = "J";
				break;
			case 12:
				faceValue = "Q";
				break;
			case 0:
			case 13:
				faceValue = "K";
				break;
			case 1: 
				faceValue = "A";
				break;
			default:
				faceValue = Integer.toString(value);
				break;
			}
			String fileName = 	String.format("card_%s.gif", i);
			cards.add(new Cards(fileName, faceValue, value));
		}
		return cards;
	}
}
