package com.lucius.springaitest.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper {
    @Insert("insert into hmdp.course(time) value (#{time})")
    void insert(Long time);
}
