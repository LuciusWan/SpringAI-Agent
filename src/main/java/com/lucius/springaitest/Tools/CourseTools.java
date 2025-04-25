package com.lucius.springaitest.Tools;

import com.lucius.springaitest.Constant.RedisConstant;
import com.lucius.springaitest.VO.Course;
import com.lucius.springaitest.VO.CourseQuery;
import com.lucius.springaitest.VO.CourseReservation;
import com.lucius.springaitest.VO.School;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.lucius.springaitest.Constant.RedisConstant.RESERVATION;

@Component
public class CourseTools {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Tool(description = "根据条件查询课程")
    public List<Course> selectCourse(@ToolParam(description = "查询的条件")CourseQuery query){
        List<String> range = stringRedisTemplate.opsForList().range(RedisConstant.COURSE, 0, -1);
        if(range==null|| range.isEmpty()){
            return List.of();
        }
        List<Course> courses = new ArrayList<>();
        for (String s : range) {
            Course course = new Course();
            course.setName(s);
            course.setDuration(100L);
            course.setPrice(1000L);
            course.setType(query.getType());
            courses.add(course);
        }
        return courses;
    }
    @Tool(description = "查询所有校区")
    public List<School> querySchool(){
        List<String> range = stringRedisTemplate.opsForList().range(RedisConstant.SCHOOL, 0, -1);
        if(range==null|| range.isEmpty()){
            return List.of();
        }
        List<School> schools = new ArrayList<>();
        for (String s : range) {
            School school = new School();
            school.setName(s);
            schools.add(school);
        }
        return schools;
    }
    @Tool(description = "生成预约单,返回预约单号")
    public Long createCourseReservation(
            @ToolParam(description = "预约课程") String course,
            @ToolParam(description = "预约校区") String school,
            @ToolParam(description = "学生姓名") String studentName,
            @ToolParam(description = "联系电话") String contactInfo,
            @ToolParam(description = "备注", required = false) String remark) {
        CourseReservation reservation = new CourseReservation();
        reservation.setCourse(course);
        reservation.setSchool(school);
        reservation.setUserName(studentName);
        reservation.setPhoneNumber(contactInfo);
        reservation.setRemark(remark);
        long id=System.currentTimeMillis();
        System.out.println(reservation);
        String value=reservation.toString();
        stringRedisTemplate.opsForList().rightPush(RedisConstant.RESERVATION,value);
        return id;
    }
}
