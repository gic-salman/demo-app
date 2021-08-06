package com.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.model.QbResponse;

@Repository
public interface QbResponseRepository extends JpaRepository<QbResponse, Long> {

}
