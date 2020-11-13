package com.example.consumer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program stream-demo
 * @description 
 * @author wq
 * created on 2020-11-13
 * @version  1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMessage {
    private Integer id;
    private String name;
}
