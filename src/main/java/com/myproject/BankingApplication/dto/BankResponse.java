package com.myproject.BankingApplication.dto;

import com.myproject.BankingApplication.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankResponse {

    //the response that the bank is going to give back
    private String responseCode;
    private String responseMessage;
    private AccountInfo accountInfo;

}
