package com.capstone3.showbee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository {
    @Override
    List findAllById(Iterable iterable);

    @Override
    Optional findById(Object o);
}
