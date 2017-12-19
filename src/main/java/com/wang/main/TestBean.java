package com.wang.main;

import org.junit.Test;

import com.wang.entity.Person;
import com.wang.entity.Student;


public class TestBean {
public static void main(String[] args) {
	 
    BeanFactory bf=new ClassPathXmlApplicationContext("/ApplicationContext.xml");
    Person s=(Person)bf.getBean("person");
    Person s1=(Person)bf.getBean("person");
    System.out.println(s==s1);
    System.out.println(s1.getStudent().getName());
    Student stu1=(Student) bf.getBean("student");
    Student stu2=(Student) bf.getBean("student");
    String name=stu1.getName();
    System.out.println(name);
    System.out.println(stu1==stu2);
}
   
}
