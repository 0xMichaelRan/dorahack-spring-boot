package com.magiplatform.dorahack.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @version V1.0
 * @Description: TODO
 * @class: PropertiesUtils
 * @Package com.magiplatform.dorahack.utils
 * @date 2020/8/26 15:30
 */
public class PropertiesUtils {
    private Properties properties;
    private static PropertiesUtils propertiesUtils = new PropertiesUtils();

    /**
     * 私有构造，禁止直接创建
     */
    private PropertiesUtils() {
        properties = new Properties();
        InputStream in = PropertiesUtils.class.getClassLoader()
                .getResourceAsStream("application-?.yml");
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取单例
     *
     * @return PropertiesUtils
     */
    public static PropertiesUtils getInstance() {
        if (propertiesUtils == null) {
            propertiesUtils = new PropertiesUtils();
        }
        return propertiesUtils;
    }

    /**
     * 根据属性名读取值
     *
     * @param name 名称
     */
    public Object getProperty(String name) {
        return properties.getProperty(name);
    }


    /*************************************************************************/
    /*****************************读取属性，封装字段**************************/
    /*************************************************************************/

    /**
     * 是否调试模式
     */
    public Boolean isDebug() {
        return "true".equals(properties.getProperty("isDebug"));
    }

    public String getAttachmentServer() {
        return properties.getProperty("attachmentServer");
    }

    public String getAttachmentPath() {
        return properties.getProperty("attachmentPath");
    }

    public String getAttachmentGainPath() {
        return properties.getProperty("attachmentGainPath");
    }
}