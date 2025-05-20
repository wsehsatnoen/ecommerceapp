package me.wsehsatnoen.ecommerceapp.dao;

import me.wsehsatnoen.ecommerceapp.entities.Excursion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface ExcursionRepository extends JpaRepository<Excursion,Long> {
}
