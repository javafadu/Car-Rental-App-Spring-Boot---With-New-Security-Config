package com.ascarrent.service;

import com.ascarrent.domain.Car;
import com.ascarrent.domain.Reservation;
import com.ascarrent.domain.User;
import com.ascarrent.exception.message.ErrorMessages;
import com.ascarrent.report.ExcelReporter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReportService {

    private final UserService userService;
    private final CarService carService;
    private final ReservationService reservationService;

    public ReportService(UserService userService, CarService carService, ReservationService reservationService) {
        this.userService = userService;
        this.carService = carService;
        this.reservationService = reservationService;
    }

    public ByteArrayInputStream getUserReport() {
        List<User> users = userService.getUsers();
        try {
            return ExcelReporter.getUserExcelReport(users);
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessages.EXCEL_REPORT_ERROR_MESSAGE);
        }
    }

    public ByteArrayInputStream getCarReport() {
        List<Car> cars = carService.getCars();
        try {
            return ExcelReporter.getCarExcelReport(cars);
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessages.EXCEL_REPORT_ERROR_MESSAGE);
        }
    }

    public ByteArrayInputStream getReservationReport() {
        List<Reservation> reservations = reservationService.getReservations();
        try {
            return ExcelReporter.getReservationExcelReport(reservations);
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessages.EXCEL_REPORT_ERROR_MESSAGE);
        }
    }
}
