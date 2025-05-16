package com.lucius.springaitest.Controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lucius.springaitest.Constant.AIConstant;
import com.lucius.springaitest.Service.ChatService;
import com.lucius.springaitest.VO.MessageVO;
import jakarta.annotation.Resource;
import org.json.JSONObject;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/ai")
public class AIController {
    @Resource(name = "chat")
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
    @Autowired
    private ChatClient AICoding;
    @Autowired
    private ChatClient AICodingNext;
    @Autowired
    private ChatClient DesignPattern;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @RequestMapping(value = "/chat2", produces = "text/event-stream")
    public SseEmitter chat(String prompt, String chatId) {
        chatService.saveChatId(chatId, "chat");
        SseEmitter emitter = new SseEmitter(60_000000L); // 设置60秒超时

        executorService.submit(() -> {

            // 第一阶段：获取初始响应并生成任务列表
            chatClient.prompt(AIConstant.NekoPrompt)
                    .user(prompt)
                    .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                    .stream()
                    .content()
                    .subscribe(
                            // 每次收到 AI 生成的代码片段时发送 SSE 事件
                            codeChunk -> {
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("message", codeChunk);
                                    emitter.send(jsonObject.toString());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            },
                            emitter::completeWithError,
                            () -> {
                                try {
                                    emitter.send("end");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                emitter.complete();
                            }
                    );
        });
        return emitter;
    }

    @RequestMapping(value = "/chatTest", produces = "text/event-stream")
    public SseEmitter chatAlibaba(String prompt, String chatId) {
        chatService.saveChatId(chatId, "chat");
        SseEmitter emitter = new SseEmitter(60_000000L); // 设置60秒超时

        executorService.submit(() -> {

            // 第一阶段：获取初始响应并生成任务列表
            ComputerNetWorkChatClient.prompt()
                    .user(prompt + " /no-think ")
                    .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                    .stream()
                    .content()
                    .subscribe(
                            // 每次收到 AI 生成的代码片段时发送 SSE 事件
                            codeChunk -> {
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("message", codeChunk);
                                    emitter.send(jsonObject.toString());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            },
                            emitter::completeWithError,
                            () -> {
                                try {
                                    emitter.send("end");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                emitter.complete();
                            }
                    );
        });
        return emitter;
    }

    @RequestMapping(value = "/chat", produces = "text/event-stream")
    public SseEmitter chatOmni(@RequestParam("prompt") String prompt,
                               @RequestParam("chatId") String chatId,
                               @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        chatService.saveChatId(chatId, "chat");
        SseEmitter emitter = new SseEmitter(60_000000L); // 设置60秒超时
        if (files == null) {
            executorService.submit(() -> {

                // 第一阶段：获取初始响应并生成任务列表
                DesignPattern.prompt()
                        .user(prompt)
                        .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                        .stream()
                        .content()
                        .subscribe(
                                // 每次收到 AI 生成的代码片段时发送 SSE 事件
                                codeChunk -> {
                                    try {
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("message", codeChunk);
                                        emitter.send(jsonObject.toString());
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                },
                                emitter::completeWithError,
                                () -> {
                                    try {
                                        emitter.send("end");
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    emitter.complete();
                                }
                        );
            });
        } else {
            List<Media> mediaList = new ArrayList<>();
            for (MultipartFile file : files) {
                Media media = new Media(MimeType.valueOf(Objects.requireNonNull(file.getContentType())), file.getResource());
                mediaList.add(media);
            }
            executorService.submit(() -> {

                // 第一阶段：获取初始响应并生成任务列表
                OminiChatClient.prompt()
                        .user(p -> p.text(prompt).media(mediaList.toArray(Media[]::new)))
                        .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                        .stream()
                        .content()
                        .subscribe(
                                // 每次收到 AI 生成的代码片段时发送 SSE 事件
                                codeChunk -> {
                                    try {
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("message", codeChunk);
                                        emitter.send(jsonObject.toString());
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                },
                                emitter::completeWithError,
                                () -> {
                                    try {
                                        emitter.send("end");
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    emitter.complete();
                                }
                        );
            });
        }
        return emitter;
    }

    @GetMapping("/history/{type}")
    public List<String> history(@PathVariable String type) {
        return chatService.selectType(type);
    }

    @GetMapping("/history/{type}/{chatId}")
    public List<MessageVO> history(@PathVariable String type, @PathVariable String chatId) {
        return chatService.selectMessage(type, chatId);
    }

    @RequestMapping(value = "/game", produces = "text/event-stream")
    public Flux<String> chatGame(String prompt, String chatId) {
        //chatService.saveChatId(chatId,"chat");
        return GirlfriendSadChatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .stream()
                .content();
    }

    @RequestMapping(value = "/chat1", produces = "text/event-stream")
    public SseEmitter code(@RequestParam("prompt") String prompt,
                           @RequestParam("chatId") String chatId) {
        chatService.saveChatId(chatId, "chat");
        SseEmitter emitter = new SseEmitter(60_000000L); // 设置60秒超时

        executorService.submit(() -> {
            StringBuilder initialResponse = new StringBuilder();

            // 第一阶段：获取初始响应并生成任务列表
            AICoding.prompt()
                    .user(prompt)
                    .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                    .stream()
                    .content()
                    .subscribe(
                            chunk -> handleChunk(chunk, emitter, initialResponse),
                            error -> handleError(error, emitter),
                            () -> handleInitialComplete(initialResponse, emitter, chatId)
                    );
        });

        return emitter;
    }

    private void handleChunk(String chunk, SseEmitter emitter, StringBuilder accumulator) {
        try {
            JSONObject message = new JSONObject();
            message.put("message", chunk);
            emitter.send(message.toString());
            accumulator.append(chunk);
        } catch (IOException e) {
            throw new RuntimeException("SSE发送失败", e);
        }
    }

    private void handleError(Throwable error, SseEmitter emitter) {
        emitter.completeWithError(error);
    }

    private void handleInitialComplete(StringBuilder initialResponse, SseEmitter emitter, String chatId) {
        try {
            List<String> taskList = parseTaskList(initialResponse.toString());
            processTasksSequentially(taskList.iterator(), emitter, chatId);
        } catch (Exception e) {
            emitter.completeWithError(new RuntimeException("解析任务列表失败", e));
        }
    }

    private List<String> parseTaskList(String jsonResponse) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        JsonArray dataArray = jsonObject.getAsJsonArray("data");

        List<String> tasks = new ArrayList<>();
        for (JsonElement element : dataArray) {
            JsonObject taskObj = element.getAsJsonObject();
            tasks.add(taskObj.get("name").getAsString());
        }
        return tasks;
    }

    private void processTasksSequentially(Iterator<String> taskIterator, SseEmitter emitter, String chatId) {
        if (!taskIterator.hasNext()) {
            emitter.complete();
            return;
        }

        String currentTask = taskIterator.next();
        StringBuilder taskResponse = new StringBuilder();

        AICodingNext.prompt()
                .user(currentTask)
                .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .stream()
                .content()
                .subscribe(
                        chunk -> handleTaskChunk(chunk, emitter, taskResponse),
                        error -> handleTaskError(error, emitter),
                        () -> completeTask(currentTask, taskResponse, taskIterator, emitter, chatId)
                );
    }

    private void handleTaskChunk(String chunk, SseEmitter emitter, StringBuilder accumulator) {
        try {
            JSONObject message = new JSONObject();
            message.put("message", chunk);
            emitter.send(message.toString());
            accumulator.append(chunk);
        } catch (IOException e) {
            throw new RuntimeException("任务SSE发送失败", e);
        }
    }

    private void handleTaskError(Throwable error, SseEmitter emitter) {
        emitter.completeWithError(error);
    }

    private void completeTask(String taskName, StringBuilder response,
                              Iterator<String> taskIterator, SseEmitter emitter, String chatId) {
        try {
            writeFile(taskName, response.toString());
            processTasksSequentially(taskIterator, emitter, chatId);
        } catch (Exception e) {
            emitter.completeWithError(new RuntimeException("文件写入失败", e));
        }
    }

    public void writeFile(String fileName, String content) {
        String directoryPath = "C:\\Users\\86180\\Desktop\\新建文件夹 (2)";
        // 创建目录对象
        File directory = new File(directoryPath);

        // 如果目录不存在，则尝试创建它
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.out.println("无法创建目录: " + directoryPath);
                return;
            }
        }

        // 创建文件对象
        File file = new File(directory, fileName);

        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {

            // 写入内容
            writer.write(content);
            System.out.println("文件写入成功: " + file.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("写入文件时发生错误: ");
        }
    }
}
