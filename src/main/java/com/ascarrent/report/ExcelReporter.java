package com.ascarrent.report;

import com.ascarrent.domain.Car;
import com.ascarrent.domain.Reservation;
import com.ascarrent.domain.Role;
import com.ascarrent.domain.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

public class ExcelReporter {

    // USER Table Header
    static String SHEET_USER = "Users";
    static String[] USER_HEADERS = {"id", "FirstName", "LastName", "PhoneNumber", "Email", "Address", "ZipCode", "BirthDate", "Roles"};

    // CAR Table Header
    static String SHEET_CAR = "Cars";
    static String[] CAR_HEADERS = {"id", "Model", "Doors", "Seats", "Luggage", "Transmission", "Year", "pricePerHour", "fuelType"};

    // RESERVATION Table Header
    static String SHEET_RESERVATION = "Cars";
    static String[] RESERVATION_HEADERS = {"id", "CarId", "CarModel", "CustomerId", "CustomerFullName", "CustomerPhoneNumber", "PickUpTime", "DropOffTime", "PickUpLocation", "DropOffLocation", "Status", "TotalPrice"};


    // ****** USER REPORT ******
    public static ByteArrayInputStream getUserExcelReport(List<User> users) throws IOException {
        Workbook workbook = new XSSFWorkbook(); // excel creator

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet(SHEET_USER);     // 1- add sheet
        Row headerRow = sheet.createRow(0);         // 2- add header row
        for (int i = 0; i < USER_HEADERS.length; i++) {     // 3- add fields into row
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(USER_HEADERS[i]);
        }

        int rowId = 1;                                      // 4- add data into body rows
        for (User user : users
        ) {
            Row row = sheet.createRow(rowId++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getFirstName());
            row.createCell(2).setCellValue(user.getLastName());
            row.createCell(3).setCellValue(user.getPhoneNumber());
            row.createCell(4).setCellValue(user.getEmail());
            row.createCell(5).setCellValue(user.getAddress());
            row.createCell(6).setCellValue(user.getZipCode());
            row.createCell(7).setCellValue(user.getBirthDate().getYear() + "-" + user.getBirthDate().getMonth() + "-" + user.getBirthDate().getDayOfMonth());
            // roles
            StringJoiner sj = new StringJoiner(",");
            for (Role role : user.getRoles()
            ) {
                sj.add(role.getType().getName());
            }
            row.createCell(8).setCellValue(sj.toString());

        }

        workbook.write(out);                            // 5- Write data into excel
        workbook.close();                               // 6- Close the workbook

        return new ByteArrayInputStream(out.toByteArray());

    }


    // ****** CAR REPORT ******
    public static ByteArrayInputStream getCarExcelReport(List<Car> cars) throws IOException {
        Workbook workbook = new XSSFWorkbook(); // excel creator

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet(SHEET_CAR);      // 1- add sheet
        Row headerRow = sheet.createRow(0);         // 2- add header row
        for (int i = 0; i < CAR_HEADERS.length; i++) {      // 3- add fields into row
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(CAR_HEADERS[i]);
        }

        int rowId = 1;                                      // 4- add data into body rows
        for (Car car : cars
        ) {
            Row row = sheet.createRow(rowId++);
            row.createCell(0).setCellValue(car.getId());
            row.createCell(1).setCellValue(car.getModel());
            row.createCell(2).setCellValue(car.getDoors());
            row.createCell(3).setCellValue(car.getSeats());
            row.createCell(4).setCellValue(car.getLuggage());
            row.createCell(5).setCellValue(car.getTransmission());
            row.createCell(6).setCellValue(car.getYear());
            row.createCell(7).setCellValue(car.getPricePerHour());
            row.createCell(8).setCellValue(car.getFuelType());
        }

        workbook.write(out);                            // 5- Write data into excel
        workbook.close();                               // 6- Close the workbook

        return new ByteArrayInputStream(out.toByteArray());

    }



    // ****** RESERVATION REPORT ******
    public static ByteArrayInputStream getReservationExcelReport(List<Reservation> reservations) throws IOException {
        Workbook workbook = new XSSFWorkbook(); // excel creator

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet(SHEET_RESERVATION);      // 1- add sheet
        Row headerRow = sheet.createRow(0);         // 2- add header row
        for (int i = 0; i < RESERVATION_HEADERS.length; i++) {      // 3- add fields into row
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(RESERVATION_HEADERS[i]);
        }

        int rowId = 1;                                      // 4- add data into body rows
        for (Reservation reservation : reservations
        ) {
            Row row = sheet.createRow(rowId++);
            row.createCell(0).setCellValue(reservation.getId());
            row.createCell(1).setCellValue(reservation.getCar().getId());
            row.createCell(2).setCellValue(reservation.getCar().getModel());
            row.createCell(3).setCellValue(reservation.getUser().getId());
            row.createCell(4).setCellValue(reservation.getUser().getFirstName()+" "+reservation.getUser().getLastName());
            row.createCell(5).setCellValue(reservation.getUser().getPhoneNumber());
            row.createCell(6).setCellValue(reservation.getPickUpTime().toString());
            row.createCell(7).setCellValue(reservation.getDropOffTime().toString());
            row.createCell(8).setCellValue(reservation.getPickUpLocation());
            row.createCell(9).setCellValue(reservation.getDropOffLocation());
            row.createCell(10).setCellValue(reservation.getStatus().toString());
            row.createCell(11).setCellValue(reservation.getTotalPrice());

        }

        workbook.write(out);                            // 5- Write data into excel
        workbook.close();                               // 6- Close the workbook

        return new ByteArrayInputStream(out.toByteArray());

    }



}
