package com.myproject.BankingApplication.service;

import com.myproject.BankingApplication.dto.*;
import com.myproject.BankingApplication.entity.User;
import com.myproject.BankingApplication.repository.UserRepository;
import com.myproject.BankingApplication.utils.AccountUtils;
import com.myproject.BankingApplication.utils.BankResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository; //
    AccountUtils accountUtils; //creating instance to use AccountUtils class method to generate an account number
    @Autowired
    EmailService emailService; //creating instance to use the email service


    //method to create a new account and saving it in the database
    public BankResponse createAccount(UserRequest userRequest) {

        //checking if account already exists
        if(userRepository.existsByEmail(userRequest.getEmail()) || userRepository.existsByPhoneNumber(userRequest.getPhoneNumber())){
            return BankResponse.builder()
                    .accountInfo(null)
                    .responseMessage(BankResponseUtils.ACCOUNT_ALREADY_EXISTS)
                    .responseCode(BankResponseUtils.TASK_NOT_COMPLETED_CODE)
                    .build();
        }

        //creating an instance and adding it to the database
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .accountNumber(accountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .status("ACTIVE")
                .build();

        User savedUser = userRepository.save(newUser);

        //Sending email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userRequest.getEmail())
                .subject("New Account Created")
                .messageBody("Congratulations! Your account was successfully created. " +
                        "\nAccount Details:" +
                        "\nName: " +savedUser.getFirstName()+" " +savedUser.getLastName()+
                        "\nAccount Number: "+savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);

        //returning a response from the bank
        return BankResponse.builder()
                .responseMessage(BankResponseUtils.ACCOUNT_CREATED_SUCCESSFULLY)
                .responseCode(BankResponseUtils.TASK_COMPLETED_SUCCESSFULLY_CODE)
                .accountInfo(AccountInfo.builder()
                        .accountName(savedUser.getFirstName()+" "+savedUser.getLastName())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountBalance(savedUser.getAccountBalance())
                        .build())
                .build();
    }


    //method to enquire about account balance
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest){
        //checking if account exists
        boolean accountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if(!accountExists) {
            return BankResponse.builder()
                    .accountInfo(null)
                    .responseMessage(BankResponseUtils.ACCOUNT_DOES_NOT_EXISTS)
                    .responseCode(BankResponseUtils.TASK_NOT_COMPLETED_CODE)
                    .build();
        }

        //getting the user details
        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
            return BankResponse.builder()
                    .accountInfo(AccountInfo.builder()
                            .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                            .accountNumber(foundUser.getAccountNumber())
                            .accountBalance(foundUser.getAccountBalance())
                            .build())
                    .responseMessage(BankResponseUtils.ACCOUNT_FOUND)
                    .responseCode(BankResponseUtils.TASK_COMPLETED_SUCCESSFULLY_CODE)
                    .build();
    }



    //method to enquire about name
    public BankResponse nameEnquiry(EnquiryRequest enquiryRequest){

        //checking if the credentials are correct or not
        boolean accountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if(!accountExists) {
            return BankResponse.builder()
                    .accountInfo(null)
                    .responseMessage(BankResponseUtils.ACCOUNT_DOES_NOT_EXISTS)
                    .responseCode(BankResponseUtils.TASK_NOT_COMPLETED_CODE)
                    .build();
        }

        //fetching details
        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .accountInfo(AccountInfo.builder()
                        .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                        .accountNumber(foundUser.getAccountNumber())
                        .accountBalance(null)
                        .build())
                .responseMessage(BankResponseUtils.ACCOUNT_FOUND)
                .responseCode(BankResponseUtils.TASK_COMPLETED_SUCCESSFULLY_CODE)
                .build();

    }


    //method to deposit or credit account
    public BankResponse deposit(CreditDebitRequest request) {

        //checking if credentials are correct
        boolean accountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!accountExists){
            return BankResponse.builder()
                    .accountInfo(null)
                    .responseMessage(BankResponseUtils.ACCOUNT_DOES_NOT_EXISTS)
                    .responseCode(BankResponseUtils.TASK_NOT_COMPLETED_CODE)
                    .build();
        }

        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        foundUser.setAccountBalance(foundUser.getAccountBalance().add(request.getAmount()));

        return BankResponse.builder()
                .accountInfo(AccountInfo.builder()
                        .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                        .accountNumber(foundUser.getAccountNumber())
                        .accountBalance(foundUser.getAccountBalance())
                        .build())
                .responseMessage(BankResponseUtils.ACCOUNT_CREDITED_SUCCESSFULLY)
                .responseCode(BankResponseUtils.TASK_COMPLETED_SUCCESSFULLY_CODE)
                .build();
    }

    //method to withdrawal or debit account
    public BankResponse withdrawal(CreditDebitRequest request) {

        //checking if credentials are correct
        boolean accountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!accountExists){
            return BankResponse.builder()
                    .accountInfo(null)
                    .responseMessage(BankResponseUtils.ACCOUNT_DOES_NOT_EXISTS)
                    .responseCode(BankResponseUtils.TASK_NOT_COMPLETED_CODE)
                    .build();
        }

        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());

        //checking if user has sufficient balance
        boolean sufficientFunds = foundUser.getAccountBalance().compareTo(request.getAmount()) >= 0;

        if(!sufficientFunds){
            return BankResponse.builder()
                    .accountInfo(AccountInfo.builder()
                            .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                            .accountNumber(foundUser.getAccountNumber())
                            .accountBalance(foundUser.getAccountBalance())
                            .build())
                    .responseMessage(BankResponseUtils.INSUFFICIENT_FUNDS)
                    .responseCode(BankResponseUtils.TASK_NOT_COMPLETED_CODE)
                    .build();
        }


        foundUser.setAccountBalance(foundUser.getAccountBalance().subtract(request.getAmount()));

        return BankResponse.builder()
                .accountInfo(AccountInfo.builder()
                        .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                        .accountNumber(foundUser.getAccountNumber())
                        .accountBalance(foundUser.getAccountBalance())
                        .build())
                .responseMessage(BankResponseUtils.ACCOUNT_DEBITED_SUCCESSFULLY)
                .responseCode(BankResponseUtils.TASK_COMPLETED_SUCCESSFULLY_CODE)
                .build();

    }



    //method for account to account transfer
    public BankResponse transactionRequest(TransactionRequest request){

        //checking if account information is correct
        boolean accountExists = userRepository.existsByAccountNumber(request.getToCreditAccountNumber());
        if(!accountExists){
            return BankResponse.builder()
                    .accountInfo(null)
                    .responseMessage(BankResponseUtils.ACCOUNT_NOT_FOUND)
                    .responseCode(BankResponseUtils.TASK_NOT_COMPLETED_CODE)
                    .build();
        }

        //checking if the user has sufficient funds
        User debitUser = userRepository.findByAccountNumber(request.getToDebitAccountNumber());
        boolean sufficientFunds = debitUser.getAccountBalance().compareTo(request.getAmount()) >= 0;

        if(!sufficientFunds){
            return BankResponse.builder()
                    .accountInfo(AccountInfo.builder()
                            .accountName(debitUser.getFirstName()+" "+debitUser.getLastName())
                            .accountNumber(debitUser.getAccountNumber())
                            .accountBalance(debitUser.getAccountBalance())
                            .build())
                    .responseMessage(BankResponseUtils.INSUFFICIENT_FUNDS)
                    .responseCode(BankResponseUtils.TASK_NOT_COMPLETED_CODE)
                    .build();
        }

        //creating instance to update credited user's account balance
        User userToCredit = userRepository.findByAccountNumber(request.getToCreditAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));

        //deducing the amount from the debited user
        debitUser.setAccountBalance(debitUser.getAccountBalance().subtract(request.getAmount()));

        //returning a response if the transaction was successful;
        return BankResponse.builder()
                .accountInfo(AccountInfo.builder()
                        .accountName(debitUser.getFirstName()+" "+debitUser.getLastName())
                        .accountNumber(debitUser.getAccountNumber())
                        .accountBalance(debitUser.getAccountBalance())
                        .build())
                .responseMessage(BankResponseUtils.TRANSACTION_SUCCESSFUL)
                .responseCode(BankResponseUtils.TASK_COMPLETED_SUCCESSFULLY_CODE)
                .build();
    }



}
