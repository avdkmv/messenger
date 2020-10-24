package com.unn.repository;

import com.unn.model.Object;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ObjectRepo extends JpaRepository<Object, Long> {
    Optional<Object> getObjectById(Long objectId);
}
