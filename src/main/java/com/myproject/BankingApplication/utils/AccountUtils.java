package com.myproject.BankingApplication.utils;

import com.myproject.BankingApplication.dto.BankResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Year;
import java.util.Random;

@NoArgsConstructor
@AllArgsConstructor
public class AccountUtils {



    Random rand = new Random();
    String base = "NM";
    // creating a random account number
    // format "NM00000000000000" i.e.: NM00-0000-0000-0000
    public String generateAccountNumber(){

        StringBuilder acNum = new StringBuilder(base);

        for(int i=0; i<14; i++){
            int n = rand.nextInt(10);
            acNum.append(n);
        }

        return acNum.toString();
    }

}

