package com.capstone3.showbee.repository;

import com.capstone3.showbee.entity.Schedule;
import com.capstone3.showbee.entity.Shared;
import com.capstone3.showbee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SharedRepository extends JpaRepository<Shared, Long> {
    List<Shared> findAllByUser(User user);
    List<Shared> findAllBySchedule(Schedule schedule);
}
