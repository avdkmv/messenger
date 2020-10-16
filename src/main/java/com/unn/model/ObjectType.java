package com.unn.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "m_object_type")
public class ObjectType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ObjectType parentId;

    @Size(min = 3, max = 25, message = "Name size must be 3 to 25 symbols.")
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ObjectType> childrenId;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "objectTypeId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Object> objectTypeId;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "objectTypeId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ListAttribute> objectTypeIdAtList;
}
