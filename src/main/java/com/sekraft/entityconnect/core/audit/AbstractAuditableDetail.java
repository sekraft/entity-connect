package com.sekraft.entityconnect.core.audit;

import com.sekraft.entityconnect.core.entity.AbstractDetail;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class AbstractAuditableDetail extends AbstractDetail
{

}