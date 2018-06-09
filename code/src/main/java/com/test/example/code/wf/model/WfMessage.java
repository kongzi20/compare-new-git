package com.test.example.code.wf.model;

import java.io.Serializable;

public class WfMessage implements Serializable {
    private static final long serialVersionUID = -267456230629056152L;
    private Long id;
    private boolean result;
    private String messageZhCn;
    private String messageEnUs;
    private String messageZhTw;

    public WfMessage() {
        super();
    }

    public WfMessage(Long id, boolean result, String messageZhCn, String messageEnUs, String messageZhTw) {
        super();
        this.id = id;
        this.result = result;
        this.messageZhCn = messageZhCn;
        this.messageEnUs = messageEnUs;
        this.messageZhTw = messageZhTw;
    }

    public WfMessage(String messageZhCn, String messageEnUs, String messageZhTw) {
        super();
        this.messageZhCn = messageZhCn;
        this.messageEnUs = messageEnUs;
        this.messageZhTw = messageZhTw;
    }

    public WfMessage(Long id, String messageZhCn, String messageEnUs, String messageZhTw) {
        this.id = id;
        this.messageZhCn = messageZhCn;
        this.messageEnUs = messageEnUs;
        this.messageZhTw = messageZhTw;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageZhCn() {
        return messageZhCn;
    }

    public void setMessageZhCn(String messageZhCn) {
        this.messageZhCn = messageZhCn;
    }

    public String getMessageEnUs() {
        return messageEnUs;
    }

    public void setMessageEnUs(String messageEnUs) {
        this.messageEnUs = messageEnUs;
    }

    public String getMessageZhTw() {
        return messageZhTw;
    }

    public void setMessageZhTw(String messageZhTw) {
        this.messageZhTw = messageZhTw;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
