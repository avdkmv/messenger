package com.unn.repository;

import com.unn.model.ObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectTypeRepo extends JpaRepository<ObjectType, Long> {
}
