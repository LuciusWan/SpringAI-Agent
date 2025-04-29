package com.lucius.springaitest.Configuration;
import com.lucius.springaitest.Constant.RedisConstant;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
        //Long size=stringRedisTemplate.opsForZSet().size(RedisConstant.REDIS_MEMORY_ID+conversationId);
        /*if(size!=null&&size>11){
            stringRedisTemplate.opsForZSet().removeRange(RedisConstant.REDIS_MEMORY_ID+conversationId,0,1);
        }*/
        //stringRedisTemplate.boundZSetOps(RedisConstant.REDIS_MEMORY_ID+conversationId).expire(12, TimeUnit.HOURS);
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
        Long size=stringRedisTemplate.opsForZSet().size(RedisConstant.REDIS_MEMORY_ID+conversationId);
        Set<String> range=new HashSet<>();
        if(size!=null){
            range = stringRedisTemplate.opsForZSet().range(RedisConstant.REDIS_MEMORY_ID+conversationId, 0, size-1L);
        }
        if(range==null){
            return null;
        }else{
            List<Message> messages=new ArrayList<>();
            for (String s : range) {
                if(s.contains("AssistantMessage")){
                    int startIndex = s.indexOf("textContent=") + "textContent=".length();
                    // 查找 textContent 的结束位置
                    int endIndex = s.indexOf(", metadata=", startIndex);

                    if (startIndex != -1 && endIndex != -1) {
                        String textContent = s.substring(startIndex, endIndex);
                        messages.add(new AssistantMessage(textContent));
                    } else {
                        System.out.println("textContent not found.");
                    }

                }else if(s.contains("UserMessage")){
                    // 查找 content 的起始位置
                    int startIndex = s.indexOf("content='") + "content='".length();
                    // 查找 content 的结束位置
                    int endIndex = s.indexOf("'", startIndex);
                    if (startIndex != -1 && endIndex != -1) {
                        String content = s.substring(startIndex, endIndex);
                        messages.add(new UserMessage(content));

                    } else {
                        System.out.println("content not found.");
                    }
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
