package com.lucius.springaitest.Tools;

import com.lucius.springaitest.Constant.RedisConstant;
import com.lucius.springaitest.Mapper.CourseMapper;
import com.lucius.springaitest.VO.Course;
import com.lucius.springaitest.VO.CourseQuery;
import com.lucius.springaitest.VO.CourseReservation;
import com.lucius.springaitest.VO.School;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Component
public class CourseTools {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private CourseMapper courseMapper;
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
    @Tool(description = "查询校区列表")
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
    @Tool(description = "告知开发者已预约成功")
    public void adminReservation(){
        Long time=System.currentTimeMillis();
        stringRedisTemplate.opsForList().rightPush("adminResp",time.toString());
        courseMapper.insert(time);
    }
    @Tool(description = "查询当地的天气")
    public String weather(@ToolParam(description = "查询用户所说校区所在的城市adcode") String adcode) throws IOException, InterruptedException {
        // 创建HttpClient实例
        HttpClient client = HttpClient.newHttpClient();
        // 构建请求的URL
        String key = "1a3d892d650f273ea24b3e2be9beea00";
        String url = "https://restapi.amap.com/v3/weather/weatherInfo?key=" + key + "&city=" + adcode;

        // 构建请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        // 发送请求并接收响应
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());
        return response.body();
    }
    @Tool(description = "查询学校附近有什么")
    public String place(@ToolParam(description = "学校经纬度坐标,用之前查询校区坐标返回的结果的第一个location字段下的坐标规则： 经度和纬度用英文逗号,分割，经度在前，纬度在后，经纬度小数点后不得超过6位") String location) throws IOException, InterruptedException {
        // 创建HttpClient实例
        HttpClient client = HttpClient.newHttpClient();
        // 构建请求的URL
        String key = "1a3d892d650f273ea24b3e2be9beea00";
        String url = "https://restapi.amap.com/v3/place/around?key=" + key +"&keywords=" + "美食"+ "&location=" + location;

        // 构建请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        // 发送请求并接收响应
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());
        return response.body();
    }
    @Tool(description = "查询目标校区坐标")
    public String localtion(@ToolParam(description = "校区名称") String school) throws IOException, InterruptedException {
        // 创建HttpClient实例
        HttpClient client = HttpClient.newHttpClient();
        // 构建请求的URL
        String key = "1a3d892d650f273ea24b3e2be9beea00";
        String url = "https://restapi.amap.com/v3/place/text?offset=20&page=1&key=1a3d892d650f273ea24b3e2be9beea00&extensions=all&" + "keywords="+school;

        // 构建请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        // 发送请求并接收响应
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());
        return response.body();
    }
}
