package com.sekraft.entityconnect.core.audit;


import com.sekraft.entityconnect.core.entity.AbstractDetail;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractMutableDetail extends AbstractDetail
{
    
}