package com.banck.banckcredit.domain;

import lombok.Data;

/**
 *
 * @author jonavcar
 */
@Data
public class Credit {

    public String credit;
    public String creditType;
    public String customer;
    public String customerType;
    public String dateCreated;
    public int limitAmount;
    public boolean active;
}
