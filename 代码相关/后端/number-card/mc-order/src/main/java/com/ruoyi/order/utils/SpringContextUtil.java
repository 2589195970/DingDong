package com.ruoyi.order.utils;


import com.ruoyi.common.exception.BizException;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/**
 * 工具接口service
 *
 * @Description
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextUtil.applicationContext == null) {
            SpringContextUtil.applicationContext = applicationContext;
        }
    }

    /**
     * 返回applicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 返回对应名字的bean
     *
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) throws BeansException {
        return (T) getApplicationContext().getBean(clazz);
    }

    /**
     * 获取方法参数类型
     *
     * @param clazz
     * @return
     * @throws BeansException
     */
    public static Class<T> getBeanParameterClass(Class clazz, String methodName, int num) throws Exception {
        try {
            Method[] methods = clazz.getMethods();
            for (Method m : methods) {
                if (m.getName().equals(methodName)) {
                    // 获取参数
                    Class<?>[] parameterTypes = m.getParameterTypes();
                    return (Class<T>) parameterTypes[num];
                }
            }
            throw new BizException("方法不存在");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("获取方法参数类型失败");
        }
    }

}