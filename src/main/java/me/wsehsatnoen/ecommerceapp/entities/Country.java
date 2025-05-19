package me.wsehsatnoen.ecommerceapp.entities;

import java.util.Date;
import java.util.Set;

public class Country {
    private Long id;
    private String country_name;
    private Date create_date;
    private Date last_update;
    private Set<Division> divisions;
}
