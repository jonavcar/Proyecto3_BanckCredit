package com.banck.banckcredit.infraestructure.model.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author jonavcar
 */
@Data
@Document("schedule")
public class ScheduleDao {

    @Id
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
