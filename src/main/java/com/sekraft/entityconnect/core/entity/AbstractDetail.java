package com.sekraft.entityconnect.core.entity;

import com.sekraft.entityconnect.core.annotation.IdentifierGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@SuperBuilder
public abstract class AbstractDetail
{
    @Id
    @IdentifierGenerator
    @Column(unique = true, updatable = false)
    private String id;
}