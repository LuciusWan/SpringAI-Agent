package com.lucius.springaitest.VO;

public class Course {
    private String name;
    private String type;
    private Long price;
    private Long duration;

    public Course() {
    }

    public Course(String name, String type, Long price, Long duration) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.duration = duration;
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
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * 设置
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取
     * @return price
     */
    public Long getPrice() {
        return price;
    }

    /**
     * 设置
     * @param price
     */
    public void setPrice(Long price) {
        this.price = price;
    }

    /**
     * 获取
     * @return duration
     */
    public Long getDuration() {
        return duration;
    }

    /**
     * 设置
     * @param duration
     */
    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String toString() {
        return "Course{name = " + name + ", type = " + type + ", price = " + price + ", duration = " + duration + "}";
    }
}
