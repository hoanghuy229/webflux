package com.hoanghuy2209.accountservice.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table
@AllArgsConstructor
public class Account {
    @Id
    private String id;
    private String email;
    private String currency;
    private double balance;
    private double reserved;

}