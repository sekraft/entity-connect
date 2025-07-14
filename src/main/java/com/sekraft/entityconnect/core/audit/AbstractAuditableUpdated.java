package com.sekraft.entityconnect.core.audit;

import java.sql.Date;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditableUpdated extends AbstractEntityLifecycleHooks
{
    @LastModifiedDate
    @Audited
    private Date updatedOn;

    @LastModifiedBy
    @Audited
    private String updatedBy;
}