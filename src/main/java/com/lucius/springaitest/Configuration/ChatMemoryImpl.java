package com.lucius.springaitest.Configuration;

import com.lucius.springaitest.Constant.RedisConstant;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ChatMemoryImpl implements ChatMemory {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void add(String conversationId, List<Message> messages) {
        String msg=stringRedisTemplate.opsForValue().get(RedisConstant.REDIS_MEMORY_ID + conversationId);
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
        }
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<Message> messages=new ArrayList<>();
        String msg=stringRedisTemplate.opsForValue().get(RedisConstant.REDIS_MEMORY_ID+conversationId);
        if(msg==null){
            return messages;
        }else{
            messages.add(new UserMessage(msg));
            return messages;
        }

    }

    @Override
    public void clear(String conversationId) {
        stringRedisTemplate.delete(RedisConstant.REDIS_MEMORY_ID+conversationId);
    }
}
