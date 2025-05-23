package me.wsehsatnoen.ecommerceapp.dao;

import me.wsehsatnoen.ecommerceapp.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface CountryRepository extends JpaRepository<Country, Long> {}
