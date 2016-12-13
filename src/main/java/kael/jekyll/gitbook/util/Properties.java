package kael.jekyll.gitbook.util;

import java.io.*;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Created by kael on 2016/12/13.
 */
public class Properties{

    private final java.util.Properties properties;

    public Properties(File file) {
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

    public void forEach(BiConsumer action){
        properties.forEach(action);
    }
}
