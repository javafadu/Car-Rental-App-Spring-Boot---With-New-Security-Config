package com.ascarrent.exception.message;

public class ErrorMessages {

    public final static String RESOURCE_NOT_FOUND_EXCEPTION = "Resource with id %d not found";
    public final static String ROLE_NOT_FOUND_EXCEPTION = "Role with name %s not found";
    public final static String USER_NOT_FOUND_EXCEPTION = "User with email %s not found";
    public final static String JWTTOKEN_ERROR_MESSAGE = "JWT Token Validation Error: %s";
    public final static String EMAIL_ALREADY_EXIST_MESSAGE = "Email: %s is already exist";
    public final static String PRINCIPAL_NOT_FOUND_MESSAGE = "User not found";
    public final static String NOT_PERMITTED_METHOD_MESSAGE = "You do not have any permission to change this data";
    public final static String PASSWORD_NOT_MATCHED_MESSAGE = "Your passwords are not matched";
    public final static String IMAGE_NOT_FOUND_EXCEPTION = "Image File with id %s not found";
    public final static String IMAGE_USED_MESSAGE = "This image is used for another car ";
    public final static String RESERVATION_TIME_INCORRECT_MESSAGE = "The reservation pick up time or drop off time is incorrect ";
    public final static String CAR_NOT_AVAILABLE_MESSAGE = "This car is not available for selected time ";
    public final static String RESERVATION_STATUS_CANT_CHANGE_MESSAGE = "Reservation can not be updated for cancelled or done reservations ";

}
