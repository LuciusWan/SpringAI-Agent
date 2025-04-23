package com.lucius.springaitest.Configuration;

import com.lucius.springaitest.Constant.AIConstant;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AIConfiguration {
    @Autowired
    private ChatMemory chatMemory;
    @Bean
    public ChatClient chat(OllamaChatModel moddle) {
        return ChatClient.builder(moddle)
                .defaultSystem(AIConstant.MAIN).build();
    }
    @Bean
    public ChatClient Alibaba(OpenAiChatModel moddle, ChatMemory chatMemory) {
        return ChatClient.builder(moddle)
                .defaultSystem(AIConstant.DEFAULT).defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new MessageChatMemoryAdvisor(chatMemory)
                ).build();
    }
    /*@Bean
    public ChatClient MoonshotModel() {
        return ChatClient.builder(moonshotChatModel).defaultSystem("假如你是特朗普,模仿他的语气说话").build();
    }*/
}
