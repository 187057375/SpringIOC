package com.wang.main;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;

import com.wang.config.Bean;
import com.wang.config.Property;
import com.wang.config.parse.ConfigManager;

public class ClassPathXmlApplicationContext implements BeanFactory {

    // 获得读取的配置文件中的Map信息
    private Map<String, Bean> map;
    // 作为IOC容器使用,放置sring放置的对象
    private Map<String, Object> context = new HashMap<String, Object>();

    public ClassPathXmlApplicationContext(String path) {
       map=ConfigManager.getConfig(path);
       for (Entry<String, Bean> en : map.entrySet()) {
    	   String beanName=en.getKey();
    	   Bean bean=en.getValue();
    	   if((!context.containsKey(beanName))&&(bean.getScope().equals("singleton")))
    		   context.put(beanName, createBean(bean));
	}

    }

    // 通过反射创建对象
    private Object createBean(Bean bean) {
        // 创建该类对象
        Class clazz = null;
        try {
        	clazz=Class.forName(bean.getClassName());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("请检查类路径"+bean.getClassName()+"是否正确");
		}
        
        Object beanObj = null;
		try {
			beanObj = clazz.newInstance();
		} catch (InstantiationException e) {
			
			e.printStackTrace();
			throw new RuntimeException("类"+clazz.getName()+"没有合适的构造方法");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
      
		List<Property> properties=bean.getProperties();
		if (properties!=null) {
			for (Property property : properties) {
				String name=property.getName();
				String value=property.getValue();
				String ref=property.getRef();
				if(value!=null){
					HashMap<String, String[]> pMap=new HashMap<String, String[]>();
					pMap.put(name, new String[]{value});
					
					try {
						BeanUtils.populate(beanObj, pMap);
					} catch (IllegalAccessException e) {
						
						e.printStackTrace();
						throw new RuntimeException("请检查你的"+name+"属性");
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Object exitObj=context.get(ref);
				if (ref!=null) {
					if (!context.containsKey(ref)) {
						 exitObj=createBean(map.get(ref));
						if (map.get(ref).getScope().equals("singleton")) {
							context.put(ref, exitObj);
						}
					}
					
					try {
						BeanUtils.setProperty(beanObj, name, exitObj);
					} catch (Exception e) {
						
						e.printStackTrace();
						throw new RuntimeException("你的bean属性"+name+"没有set方法");
					
				}
				
				
				
			}
			
		}
		}
        
        return beanObj;
    }

    public Object getBean(String name) {
        Object bean = context.get(name);
        // 如果为空说明scope不是singleton,那么容器中是没有的,这里现场创建
        if (bean == null) {
            bean = createBean(map.get(name));
        }
        if (bean==null) {
			throw new RuntimeException("请检查bean名称是否正确！");
		}
        return bean;
    }

}