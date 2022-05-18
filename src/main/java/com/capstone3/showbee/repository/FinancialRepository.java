package com.capstone3.showbee.repository;

import com.capstone3.showbee.entity.Financial;
import com.capstone3.showbee.entity.FinancialDTO;
import com.capstone3.showbee.entity.Schedule;
import com.capstone3.showbee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FinancialRepository extends JpaRepository<Financial, Long> {
    List<Financial> findFAllByUser(User user);
//    Long update(FinancialDTO financialDTO);


}
