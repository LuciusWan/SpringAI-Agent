package com.lucius.springaitest.Service;

import com.lucius.springaitest.VO.MessageVO;

import java.util.List;

public interface ChatService {

     List<String> selectType(String type);

    void saveChatId(String chatId,String type);

    List<MessageVO> selectMessage(String type, String chatId);
}
