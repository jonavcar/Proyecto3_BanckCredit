/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banck.banckcredit.domain;

import lombok.Data;

/**
 *
 * @author jnacarra
 */
@Data
public class Schedule {

    public String schedule;
    public String customer;
    public String product;
    public String productType;
    public String concept;
    public String expirationDate;
    public double amount;
    public double interest;
    public double total;
    public String observations;
    public String date;
    public String time;
    public String status;
}
