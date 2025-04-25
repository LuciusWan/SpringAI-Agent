package com.lucius.springaitest.Configuration;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMemoryConfig {
    @Bean
    public ChatMemory chatMemory() {
        //是否使用Redis实现对话记忆
        return new ChatMemoryImpl();
        //return new InMemoryChatMemory();
    }
}
