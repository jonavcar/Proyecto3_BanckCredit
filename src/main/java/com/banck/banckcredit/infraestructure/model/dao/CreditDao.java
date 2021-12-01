package com.banck.banckcredit.infraestructure.model.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author jonavcar
 */
@Data
@Document("credit")
public class CreditDao {

    @Id
    public String credit;
    public String creditType;
    public String customer;
    public String customerType;
    public String dateCreated;
    public int limitAmount;
    public boolean active;
}
