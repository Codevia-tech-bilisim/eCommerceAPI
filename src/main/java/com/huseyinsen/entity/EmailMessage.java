package com.huseyinsen.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage {
    private String to;
    private String subject;
    private String templateName;
    private Map<String, Object> templateModel;

}