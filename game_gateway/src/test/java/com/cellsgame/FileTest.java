package com.cellsgame;

import com.cellsgame.gateway.utils.FileWatchdog;
import com.cellsgame.gateway.utils.Utils;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by Yang on 16/7/6.
 */
public class FileTest {
    public static void main(String[] args) throws Exception {
//        FileTest.class.getResourceAsStream("config.properties");
        String path = FileTest.class.getResource("/config.properties").getFile();
        FileWatchdog f = new FileWatchdog(path) {
            @Override
            protected void doOnChange() {
                FileInputStream stream = null;
                try {
                    stream = new FileInputStream(filename);
                    Properties properties = new Properties();
                    properties.load(stream);
                    for (Object key : properties.keySet()) {
                        System.out.println(String.format("%s = %s", key, properties.get(key)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (stream != null) Utils.closeQuietly(stream);
                }
            }
        };
        f.setDaemon(false);
        f.setDelay(1000);
        f.start();
    }
}
