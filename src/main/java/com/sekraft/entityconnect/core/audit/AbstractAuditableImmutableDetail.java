package com.sekraft.entityconnect.core.audit;

import jakarta.persistence.MappedSuperclass;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class AbstractAuditableImmutableDetail extends AbstractAuditableDetail
{

}