package com.lucius.springaitest.Controller;

import com.lucius.springaitest.Constant.AIConstant;
import com.lucius.springaitest.Service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class CourseController {
    @Autowired
    private ChatClient serviceChatClient;
    @Autowired
    private ChatService chatService;
    @GetMapping(value = "/service",produces = "text/html;charset=utf-8")
    public Flux<String> getAllCourses(String prompt,String chatId) {
        chatService.saveChatId(chatId,"server");
        return serviceChatClient.prompt()
                .user(prompt)
                .advisors(a->a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,chatId))
                .stream()
                .content();
    }
}
