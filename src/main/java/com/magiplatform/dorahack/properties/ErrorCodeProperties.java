package com.magiplatform.dorahack.properties;

import lombok.Data;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Data
public class ErrorCodeProperties {

    private final Properties properties;
    private static ErrorCodeProperties errorCodeProperties = new ErrorCodeProperties();

    private ErrorCodeProperties() {
        properties = new Properties();
        InputStream in = ErrorCodeProperties.class.getClassLoader()
                .getResourceAsStream("errorcode.properties");
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ErrorCodeProperties getInstance() {
        if (errorCodeProperties == null) {
            errorCodeProperties = new ErrorCodeProperties();
        }
        return errorCodeProperties;
    }
}
