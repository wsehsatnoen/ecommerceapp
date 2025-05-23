package me.wsehsatnoen.ecommerceapp.dao;

import me.wsehsatnoen.ecommerceapp.entities.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface DivisionRepository extends JpaRepository<Division,Long> {}
