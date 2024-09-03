package com.intbyte.wizbuddy.employeepershop.domain.entity;

import com.intbyte.wizbuddy.employeepershop.domain.EditEmployeePerShopInfo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employeepershop")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@IdClass(EmployeePerShopId.class)
public class EmployeePerShop {

    @Id
    @Column
    private int shopCode;

    @Id
    @Column
    private String employeeCode;

    @Column
    private int shopHourlyWage;

    @Column
    private int shopMonthlyWage; // 월급일

    public void modify(int shopValue, String employeeValue, EditEmployeePerShopInfo info) {
        this.shopCode = shopValue;
        this.employeeCode = employeeValue;
        this.shopHourlyWage = info.getShopHourlyWage();
        this.shopMonthlyWage = info.getShopMonthlyWage();
    }
}
