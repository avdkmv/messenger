package com.unn.repository;

import com.unn.model.ListAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListAttributeRepo extends JpaRepository<ListAttribute, Long> {
}
