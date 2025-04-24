package com.lucius.springaitest.Service.ServiceImpl;

import com.lucius.springaitest.Configuration.ChatMemoryImpl;
import com.lucius.springaitest.Constant.RedisConstant;
import com.lucius.springaitest.Service.ChatService;
import com.lucius.springaitest.VO.MessageVO;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;



@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ChatMemoryImpl chatMemoryImpl;

    @Override
    public List<String> selectType(String type) {
        List<String> list = new ArrayList<>();
        Set<String> strings = stringRedisTemplate.opsForZSet().reverseRange(RedisConstant.CHAT_ID + type, 0, -1);
        if (strings != null) {
            list.addAll(strings);
        }
        return list;
    }

    @Override
    public void saveChatId(String chatId,String type) {
        long time = System.currentTimeMillis();
        stringRedisTemplate.opsForZSet().add(RedisConstant.CHAT_ID+type,chatId,time);
    }

    @Override
    public List<MessageVO> selectMessage(String type, String chatId) {
        List<MessageVO> list = new ArrayList<>();
        List<Message> list1;
        list1=chatMemoryImpl.get(chatId,100);
        for (Message message : list1) {
            list.add(new MessageVO(message));
        }
        return list;
    }
}
