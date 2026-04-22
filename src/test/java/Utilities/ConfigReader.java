package Utilities;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private Properties prop;

    public Properties init_prop() {
        prop = new Properties();
        try {
            // the ClassLoader is used to find the file from the resources folder
            InputStream ip = getClass().getClassLoader().getResourceAsStream("config.properties");
            prop.load(ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }
}