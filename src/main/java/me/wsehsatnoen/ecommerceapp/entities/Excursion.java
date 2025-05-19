package me.wsehsatnoen.ecommerceapp.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class Excursion {

    private Long id;
    private String excursion_title;
    private BigDecimal excursion_price;
    private String image_URL;
    private Date created_date;
    private Date last_update;
    private Vacation vacation;
    private Set<CartItem> cart_items;

}
