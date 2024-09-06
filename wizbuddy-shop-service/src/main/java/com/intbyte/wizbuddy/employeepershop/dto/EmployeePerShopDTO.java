package com.intbyte.wizbuddy.employeepershop.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class EmployeePerShopDTO {
    private int shopCode;
    private String employeeCode;
    private int shopHourlyWage;
    private int shopMonthlyWage;
}
