package com.wang.config.parse;

/**   
  * @Title: ConfigManager.java
  * @Description:
  * @Company  电子科技大学自动化研究所
  * @author  杜松   
  * @date 2017年12月19日 上午9:27:38
  * @version V1.0   
*/

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.wang.config.Bean;
import com.wang.config.Property;

public class ConfigManager {

	private static Map<String, Bean> map = new HashMap<String, Bean>();

	// 读取配置文件并返回读取结果
	// 返回Map集合便于注入,key是每个Bean的name属性,value是对应的那个Bean对象
	@SuppressWarnings("unchecked")
	public static Map<String, Bean> getConfig(String path) {
		/*
		 * dom4j实现 1.创建解析器 2.加载配置文件,得到document对象 3.定义xpath表达式,取出所有Bean元素
		 * 4.对Bean元素继续遍历
		 *  4.1将Bean元素的name/class属性封装到bean类属性中
		 * 	4.2获得bean下的所有property子元素 
		 * 	4.3将属性name/value/ref分装到类Property类中
		 * 5.将property对象封装到bean对象中
		 * 6.将bean对象封装到Map集合中,返回map
		 */
		// 1.创建解析器
		SAXReader reader = new SAXReader();

		// 2.加载bean的配置文件
		InputStream is = ConfigManager.class.getResourceAsStream(path);
		// 3.获取文档中所有的bean元素
		String xpath = "//bean";
		Document document = null;
		try {
			document = reader.read(is);
		} catch (DocumentException e) {

			e.printStackTrace();
			throw new RuntimeException("请检查你的XML文档是否配置正确");

		}

		List<Element> elements = document.selectNodes(xpath);
		if (elements != null) {
			for (Element ele : elements) {
				Bean bean = new Bean();
				String className = ele.attributeValue("class");
				String beanName = ele.attributeValue("name");
				String scope = ele.attributeValue("scope");
				bean.setClassName(className);
				bean.setName(beanName);
				if (scope != null) {
					bean.setScope(scope);
				}

				List<Element> properties = ele.elements("property");

				if (properties != null) {
					for (Element element : properties) {
						Property property = new Property();
						String pName = element.attributeValue("name");
						String pValue = element.attributeValue("value");
						String pRef = element.attributeValue("ref");
						property.setName(pName);
						property.setValue(pValue);
						property.setRef(pRef);
						bean.getProperties().add(property);
					}

				}

				map.put(beanName, bean);
			}
		}

		return map;
	}

}