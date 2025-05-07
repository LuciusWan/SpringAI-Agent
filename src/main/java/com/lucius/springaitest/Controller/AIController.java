package com.lucius.springaitest.Controller;
import com.lucius.springaitest.Constant.AIConstant;
import com.lucius.springaitest.Service.ChatService;
import com.lucius.springaitest.VO.MessageVO;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@RestController
@RequestMapping("/ai")
public class AIController {
    @Resource(name="chat")
    private ChatClient chatClient;
    @Autowired
    private ChatClient GirlfriendSadChatClient;
    @Autowired
    private ChatService chatService;
    @Autowired
    private ChatClient GirlfriendChatClient;
    @Autowired
    private ChatClient ComputerNetWorkChatClient;
    @Autowired
    private ChatClient Alibaba;
    @Autowired
    private ChatClient GirlfriendAIChatClient;
    @Autowired
    private ChatClient EnglishHomework;
    @Autowired
    private ChatClient DefaultChatClient;
    @Autowired
    private ChatClient OminiChatClient;
    @Autowired
    private ChatClient SQLChatClient;
    @Autowired
    private ChatClient LearningChatClient;
    @RequestMapping(value = "/chat2",produces = "text/html;charset=utf-8")
    public Flux<String> chat(String prompt,String chatId) {
        chatService.saveChatId(chatId,"chat");
        return chatClient.prompt(AIConstant.NekoPrompt)
                .user(prompt)
                .advisors(a->a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,chatId))
                .stream()
                .content();
    }
    @RequestMapping(value = "/chatTest",produces = "text/html;charset=utf-8")
    public Flux<String> chatAlibaba(String prompt,String chatId) {
        chatService.saveChatId(chatId,"chat");
        return ComputerNetWorkChatClient.prompt()
                .user(prompt+" /no-think ")
                .advisors(a->a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,chatId))
                .stream()
                .content();
    }
    @RequestMapping(value = "/chat",produces = "text/html;charset=utf-8")
    public Flux<String> chatOmni(@RequestParam("prompt") String prompt,
                                 @RequestParam("chatId") String chatId,
                                 @RequestParam(value = "files",required = false) List<MultipartFile> files) {
        chatService.saveChatId(chatId,"chat");
        if(files==null) {
            return LearningChatClient.prompt()
                    .user(prompt+" /no-think ")
                    .advisors(a->a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,chatId))
                    .stream()
                    .content();
        }else{
            List<Media> mediaList = new ArrayList<>();
            for (MultipartFile file : files) {
                Media media=new Media(MimeType.valueOf(Objects.requireNonNull(file.getContentType())),file.getResource());
                mediaList.add(media);
            }
            return OminiChatClient.prompt()
                    .user(p-> p.text(prompt).media(mediaList.toArray(Media[]::new)))
                    .advisors(a->a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,chatId))
                    .stream()
                    .content();

        }
    }
    @GetMapping("/history/{type}")
    public List<String> history(@PathVariable String type) {
        return chatService.selectType(type);
    }
    @GetMapping("/history/{type}/{chatId}")
    public List<MessageVO> history(@PathVariable String type, @PathVariable String chatId) {
        return chatService.selectMessage(type,chatId);
    }
    @RequestMapping(value = "/game",produces = "text/html;charset=utf-8")
    public Flux<String> chatGame(String prompt,String chatId) {
        //chatService.saveChatId(chatId,"chat");
        return GirlfriendSadChatClient.prompt()
                .user(prompt)
                .advisors(a->a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,chatId))
                .stream()
                .content();
    }
}
