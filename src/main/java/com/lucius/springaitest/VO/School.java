package com.lucius.springaitest.VO;

public class School {
    private String name;
    private String city;

    public School() {
    }

    public School(String name, String city) {
        this.name = name;
        this.city = city;
    }

    /**
     * 获取
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    public String toString() {
        return "School{name = " + name + ", city = " + city + "}";
    }
}
