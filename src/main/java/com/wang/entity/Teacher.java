package com.wang.entity;

/**   
  * @Title: Teacher.java
  * @Description:
  * @Company  电子科技大学自动化研究所
  * @author  杜松   
  * @date 2017年12月19日 上午9:21:59
  * @version V1.0   
*/

//Teacher类
public class Teacher {

  private Student student;

  public Student getStudent() {
      return student;
  }

  public void setStudent(Student student) {
      this.student = student;
  }
   
}
