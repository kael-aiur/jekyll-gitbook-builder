package kael.jekyll.gitbook.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Created by kael on 2016/12/13.
 */
public class Properties {

    private final java.util.Properties properties;

    public Properties(String file) {
        properties = new java.util.Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            properties.load(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String get(String key){
        return Objects.toString(properties.get(key));
    }
}
