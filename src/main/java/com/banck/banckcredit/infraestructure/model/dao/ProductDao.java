package com.banck.banckcredit.infraestructure.model.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author jonavcar
 */
@Data
@Document("product")
public class ProductDao {

    @Id
    public String product;
    public String productType;
    public String customer;
    public String customerType;
    public String date;
    public boolean status;
}
