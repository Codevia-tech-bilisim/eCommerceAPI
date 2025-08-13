package com.huseyinsen.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyMessage {
    private String content;
    private String sender;

    @Override
    public String toString() {
        return "MyMessage{" +
                "content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                '}';
    }
}