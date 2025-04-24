package com.lucius.springaitest.VO;

import org.springframework.ai.chat.messages.Message;

public class MessageVO {
    private String role;
    private String content;
    public MessageVO(Message message) {
        switch (message.getMessageType()) {
            case USER:
                this.role = "user";
                break;
            case ASSISTANT:
                this.role = "assistant";
                break;
            default:
                role = "";
                break;
        }
        this.content= message.getText();
    }

    public MessageVO() {
    }

    /**
     * 获取
     * @return role
     */
    public String getRole() {
        return role;
    }

    /**
     * 设置
     * @param role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * 获取
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    public String toString() {
        return "MessageVO{role = " + role + ", content = " + content + "}";
    }
}

