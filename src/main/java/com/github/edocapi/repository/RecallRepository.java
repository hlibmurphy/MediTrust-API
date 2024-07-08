package com.github.edocapi.repository;

import com.github.edocapi.model.Recall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecallRepository extends JpaRepository<Recall, Long> {
}
