package com.lucius.springaitest.Controller;
import com.lucius.springaitest.Service.ChatService;
import com.lucius.springaitest.VO.MessageVO;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/ai")
public class AIController {
    @Resource(name="chat")
    private ChatClient chatClient;
    @Resource(name = "Alibaba")
    private ChatClient chatClient2;
    @Autowired
    private ChatService chatService;
    @RequestMapping(value = "/chat2",produces = "text/html;charset=utf-8")
    public Flux<String> chat(String prompt,String chatId) {
        chatService.saveChatId(chatId,"chat");
        return chatClient.prompt()
                .user(prompt)
                .advisors(a->a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,chatId))
                .stream()
                .content();
    }
    @RequestMapping(value = "/chat",produces = "text/html;charset=utf-8")
    public Flux<String> chatAlibaba(String prompt,String chatId) {
        System.out.println(chatId);
        return chatClient2.prompt()
                .user(prompt)
                .advisors(a->a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,chatId))
                .stream()
                .content();
    }
    @GetMapping("/history/{type}")
    public List<String> history(@PathVariable String type) {
        return chatService.selectType(type);
    }
    @GetMapping("/history/{type}/{chatId}")
    public List<MessageVO> history(@PathVariable String type, @PathVariable String chatId) {
        return chatService.selectMessage(type,chatId);
    }
}
