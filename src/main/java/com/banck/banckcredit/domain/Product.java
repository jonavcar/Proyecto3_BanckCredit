package com.banck.banckcredit.domain;

import lombok.Data;

/**
 *
 * @author jonavcar
 */
@Data
public class Product {

    public String product;
    public String productType;
    public String customer;
    public String customerType;
    public String date;
    public boolean status;
}
