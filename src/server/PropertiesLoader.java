package server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class PropertiesLoader {
	private Properties prop;

	public PropertiesLoader()
	{
		prop = new Properties();
	}
	
	public void loadProperties() {
		String filename = "config.properties";
		try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename)){

			if (input == null) {
				System.out.println("Sorry, unable to find " + filename);
				return;
			}

			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
	private void printThemAll() {
		Enumeration<?> e = prop.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String value = prop.getProperty(key);
			System.out.println("Key : " + key + ", Value : " + value);
		}
	}
	
	public String getProperty(String key)
	{
		return prop.getProperty(key);
	}
	
	public Properties getProperties() {
		return prop;
	}
	
}
