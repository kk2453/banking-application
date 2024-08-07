package com.myproject.BankingApplication.controller;

import com.myproject.BankingApplication.dto.*;
import com.myproject.BankingApplication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    @Autowired
    public final UserService userService;

    @Operation(
            summary = "create a new user account",
            description = "creating a new user account and assigning a user/account id"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PostMapping
    public ResponseEntity<BankResponse> createAccount(@RequestBody UserRequest userRequest){
        return new ResponseEntity<>(userService.createAccount(userRequest), HttpStatus.CREATED);
    }

    @Operation(
            summary = "name enquiry",
            description = "given account number, check the name"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/name-enquiry")
    public ResponseEntity<BankResponse> nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return new ResponseEntity<>(userService.nameEnquiry(enquiryRequest), HttpStatus.FOUND);
    }

    @Operation(
            summary = "balance enquiry",
            description = "given account number, check the account balance"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/balance-enquiry")
    public ResponseEntity<BankResponse> balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return new ResponseEntity<>(userService.balanceEnquiry(enquiryRequest), HttpStatus.OK);
    }

    @Operation(
            summary = "deposit money in account",
            description = "creating a new user account and assigning a user/account id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PutMapping("/deposit")
    public ResponseEntity<BankResponse> deposit(@RequestBody CreditDebitRequest request){

        return new ResponseEntity<>(userService.deposit(request),HttpStatus.OK);
    }

    @Operation(
            summary = "create a new user account",
            description = "creating a new user account and assigning a user/account id"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PutMapping("/withdrawal")
    public ResponseEntity<BankResponse> withdrawal(@RequestBody CreditDebitRequest request){

        return new ResponseEntity<>(userService.withdrawal(request) ,HttpStatus.OK);
    }

    @Operation(
            summary = "create a new user account",
            description = "creating a new user account and assigning a user/account id"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PutMapping("/transaction")
    public ResponseEntity<BankResponse> transactionRequest(@RequestBody TransactionRequest transactionRequest){
        return new ResponseEntity<>(userService.transactionRequest(transactionRequest), HttpStatus.OK);
    }

}
