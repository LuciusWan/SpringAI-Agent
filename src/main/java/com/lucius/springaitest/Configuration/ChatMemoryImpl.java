package com.lucius.springaitest.Configuration;

import com.lucius.springaitest.Constant.RedisConstant;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class ChatMemoryImpl implements ChatMemory {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void add(String conversationId, List<Message> messages) {
        /*String msg=stringRedisTemplate.opsForValue().get(RedisConstant.REDIS_MEMORY_ID + conversationId);
        if(msg==null){
            StringBuilder builder=new StringBuilder();
            for (Message message : messages) {
                builder.append(message);
            }
            msg=builder.toString();
            stringRedisTemplate.opsForValue().set(RedisConstant.REDIS_MEMORY_ID+conversationId,msg);
        }else{
            StringBuilder builder=new StringBuilder();
            for (Message message : messages) {
                builder.append(message);
            }
            msg=msg+builder;
            stringRedisTemplate.delete(RedisConstant.REDIS_MEMORY_ID+conversationId);
            stringRedisTemplate.opsForValue().set(RedisConstant.REDIS_MEMORY_ID+conversationId,msg);
        }*/
        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (Message message : messages) {
            sb.append(message);
        }
        stringRedisTemplate.opsForZSet().add(RedisConstant.REDIS_MEMORY_ID+conversationId,sb.toString(),start);
        Long size=stringRedisTemplate.opsForZSet().size(RedisConstant.REDIS_MEMORY_ID+conversationId);
        if(size!=null&&size>11){
            stringRedisTemplate.opsForZSet().removeRange(RedisConstant.REDIS_MEMORY_ID+conversationId,0,1);
        }
        stringRedisTemplate.boundZSetOps(RedisConstant.REDIS_MEMORY_ID+conversationId).expire(12, TimeUnit.HOURS);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        /*List<Message> messages=new ArrayList<>();
        String msg=stringRedisTemplate.opsForValue().get(RedisConstant.REDIS_MEMORY_ID+conversationId);
        if(msg==null){
            return messages;
        }else{
            messages.add(new UserMessage(msg));
            return messages;
        }*/
        Set<String> range = stringRedisTemplate.opsForZSet().range(RedisConstant.REDIS_MEMORY_ID + conversationId, 0, 9);
        if(range==null){
            return null;
        }else{
            System.out.println(range);
            List<Message> messages=new ArrayList<>();
            for (String s : range) {
                if(s.contains("AssistantMessage")){
                    messages.add(new AssistantMessage(s));
                }else if(s.contains("UserMessage")){
                    messages.add(new UserMessage(s));
                }
            }
            return messages;
        }
    }

    @Override
    public void clear(String conversationId) {
        stringRedisTemplate.delete(RedisConstant.REDIS_MEMORY_ID+conversationId);
    }
}
