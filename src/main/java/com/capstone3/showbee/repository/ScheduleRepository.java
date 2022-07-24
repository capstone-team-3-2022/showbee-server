package com.capstone3.showbee.repository;

import com.capstone3.showbee.entity.Schedule;
import com.capstone3.showbee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByStitle(String stitle);
    // findAllByUser : 전체 레코드 불러오기. 정렬(sort), 페이징(pageable) 가능
    List<Schedule> findAllByUser(User user);
}
