package com.capstone3.showbee.repository;

import com.capstone3.showbee.entity.Financial;
import com.capstone3.showbee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;
import java.util.List;

public interface FinancialRepository extends JpaRepository<Financial, Long> {
    List<Financial> findFAllByUser(User user);
    List<Financial> findAllByDate(Date date);

}
