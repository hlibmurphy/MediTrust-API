package com.github.meditrust.repository;

import com.github.meditrust.model.Recall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecallRepository extends JpaRepository<Recall, Long> {
}
