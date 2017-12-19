package com.wang.main;

/**   
  * @Title: BeanFactory.java
  * @Description:
  * @Company  电子科技大学自动化研究所
  * @author  杜松   
  * @date 2017年12月19日 上午9:44:37
  * @version V1.0   
*/


public interface BeanFactory {
    //核心方法getBean
    Object getBean(String name);
}