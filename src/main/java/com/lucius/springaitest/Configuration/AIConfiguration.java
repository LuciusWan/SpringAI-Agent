package com.lucius.springaitest.Configuration;

import com.lucius.springaitest.Constant.AIConstant;
import com.lucius.springaitest.Tools.CourseTools;
import com.lucius.springaitest.VO.Course;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AIConfiguration {
    @Autowired
    private CourseTools courseTools;
    @Autowired
    private ChatMemory chatMemory;
    @Bean
    public ChatClient chat(OllamaChatModel moddle) {
        return ChatClient.builder(moddle)
                .defaultSystem(AIConstant.DEFAULT).defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new MessageChatMemoryAdvisor(chatMemory)
                ).build();
    }
    @Bean
    public ChatClient Alibaba(OpenAiChatModel moddle, ChatMemory chatMemory) {
        return ChatClient.builder(moddle)
                .defaultSystem(AIConstant.DEFAULT).defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new MessageChatMemoryAdvisor(chatMemory)
                ).build();
    }
    @Bean
    public ChatClient serviceChatClient(
            OpenAiChatModel model,
            ChatMemory chatMemory) {
        return ChatClient.builder(model)
                .defaultSystem(AIConstant.SERVER)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new SimpleLoggerAdvisor())
                .defaultTools(courseTools)
                .build();
    }
}
