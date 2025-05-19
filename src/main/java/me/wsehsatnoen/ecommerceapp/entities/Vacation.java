package me.wsehsatnoen.ecommerceapp.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class Vacation {

    private Long id;
    private String vacation_title;
    private String description;
    private BigDecimal travel_price;
    private String image_URL;
    private Date create_date;
    private Date last_update;
    private Set<Excursion> excursions;

}
