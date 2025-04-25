package com.lucius.springaitest.VO;

import org.springframework.ai.tool.annotation.ToolParam;

public class CourseReservation {
    private String course;
    private String school;
    private String userName;
    private String phoneNumber;
    private String remark;

    public CourseReservation() {
    }

    public CourseReservation(String course, String school, String userName, String phoneNumber, String remark) {
        this.course = course;
        this.school = school;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.remark = remark;
    }

    /**
     * 获取
     * @return course
     */
    public String getCourse() {
        return course;
    }

    /**
     * 设置
     * @param course
     */
    public void setCourse(String course) {
        this.course = course;
    }

    /**
     * 获取
     * @return school
     */
    public String getSchool() {
        return school;
    }

    /**
     * 设置
     * @param school
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * 获取
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * 设置
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * 获取
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String toString() {
        return "CourseReservation{course = " + course + ", school = " + school + ", userName = " + userName + ", phoneNumber = " + phoneNumber + ", remark = " + remark + "}";
    }
}
