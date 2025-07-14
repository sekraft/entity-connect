package com.sekraft.entityconnect.core.audit;

import java.time.LocalDateTime;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditableCreated extends AbstractEntityLifecycleHooks
{
    @CreatedDate
    @Column(updatable = false)
    @Audited
    private LocalDateTime createdOn;

    @CreatedBy
    @Column(updatable = false)
    @Audited
    private String createdBy;
}