package com.unn.repository;

import com.unn.model.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValueRepo extends JpaRepository<Value, Long> {
    Optional<Value> getValueByObjectIdAndAttributeId(Long objectId, Long attributeId);
}
