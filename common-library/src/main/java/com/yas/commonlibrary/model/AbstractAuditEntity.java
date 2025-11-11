package com.yas.commonlibrary.model;

import com.yas.commonlibrary.model.listener.CustomAuditingEntityListener;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

/* Class with audit fields getting inherited by its child entity classes */
@MappedSuperclass
@Getter
@Setter
@EntityListeners(CustomAuditingEntityListener.class)
public class AbstractAuditEntity {

    @CreationTimestamp
    private ZonedDateTime createdOn; // Automatically set when entity is first saved

    @CreatedBy
    private String createdBy; // Captures who created the record

    @UpdateTimestamp
    private ZonedDateTime lastModifiedOn; // Updates automatically on each save

    @LastModifiedBy
    private String lastModifiedBy; // Captures who last modified the record
}

/*
    1. @MappedSuperclass:
        + This class won't have its own database table.
        * Instead, its fields get inherited by child entity classes and added to their tables.
        * Common pattern for "shared fields" across "multiple entities".
    2. Lombok Annotations (@Getter, @Setter):
        + Automatically generates getter and setter methods for all fields at compile time.
        => The code gets clean and concise.
    3. EntityListeners(CustomAuditingEntityListener.class):
        + Registering a listener that "automatically" populates these audit fields.
        + The listeners intercept JPA lifecycle events (before persist, before update...)
    4. Audit fields:
        + Special data fields in a system that track the "History of changes" to a record/row,
          (Who created it, When it was created, Who last modified it, When it was last modified)
 */