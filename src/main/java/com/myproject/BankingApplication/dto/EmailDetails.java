package com.myproject.BankingApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDetails {

    public String recipient;
    public String subject;
    public String messageBody;
    //public String attachment;
    //public String;

}
