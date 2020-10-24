package com.unn.repository;

import com.unn.model.AttributeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeTypeRepo extends JpaRepository<AttributeType, Long> {
}
