package com.yas.commonlibrary.model.listener;

import com.yas.commonlibrary.model.AbstractAuditEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

// AuditorAware_step_1: At Compile Time in common-library service (NO AuditorAware here)
@Configurable // The magic that lets Spring inject the dependencies into the objects that JPA creates
public class CustomAuditingEntityListener extends AuditingEntityListener {
    /* Without @Configurable, 3 things won't happen
        + No Constructor injection possible because JPA creates this listener(CustomAuditingEntityListener), not SPRING.
         => Result:
                + Handler would be "null".
                + Auditing would fail.
    */

    public CustomAuditingEntityListener(ObjectFactory<AuditingHandler> handler) {
        /*
            AuditingHandler is SpringDataJPA "core auditing component" that:
                1. Determines WHO made the change (the auditor):
                    + Calls your configured AuditorAware<String> bean
                    + This typically integrates with Spring Security to get the current username
                2. Determines WHEN the change happened:
                    + Uses @CreationTimestamp and @UpdateTimestamp logic
                    + Captures current time
                3. Applies these values to your entity fields
         */
        /* The "dependency": AuditingHandler which contains the logic to:
            + Call your "AuditorAware" bean to get the current user
            + Apply timestamps
            + Populate your audit fields
         */
        super.setAuditingHandler(handler);
    }

    @Override
    @PrePersist // Called immediately before the entity is inserted into the DB.
    public void touchForCreate(Object target) {
        AbstractAuditEntity entity = (AbstractAuditEntity) target;
        /* This will provide the flexibility while still maintaining the data integrity */
        if (entity.getCreatedBy() == null) {
            super.touchForCreate(target); // Let Spring's automatic auditing fill it in
        } else {
            // If createdBy is already set → Respect the manual value not automatic value,
            // but still ensure lastModifiedBy is also set to the same person
            if (entity.getLastModifiedBy() == null) {
                entity.setLastModifiedBy(entity.getCreatedBy()); // Set to the same person.
            }
        }
    }

    @Override
    @PreUpdate // Triggered RIGHT BEFORE entity is updated in database
    public void touchForUpdate(Object target) {
        AbstractAuditEntity entity = (AbstractAuditEntity) target;
        if (entity.getLastModifiedBy() == null) { // Q.3: Why call super Only when Null? ->
            // No manual override → use Spring's auto-auditing
            super.touchForUpdate(target);
        }
        // If lastModifiedBy is set → respect the manual value
    }
}

/*
    3. Q3:
        What "super.touchForCreate(target)" does:
            * Calls SpringData-JPA's "default auditing behavior"
            * Automatically populates createdBy, createdOn, lastModifiedBy, lastModifiedOn (by Spring's automatic-auditing)
              based on our "Spring-Security context" or "configured auditor".

        Why only call super() when null? ("convention over configuration with escape hatch" pattern)
            * If the field is already set: because the developer explicitly set it already => so don't overwrite it
            * If the field is "null" → Use Spring's "automatic auditing" to fill it in
 */

/*

    I. JPA lifecycle (Entities in JPA can exist in 4 statuses):

        https://medium.com/jpa-java-persistence-api-guide/jpa-entity-lifecycle-statuses-a-comprehensive-guide-e241d68cee58

        1. New:
            + When an entity instance is first created using "New".
              It's not associated with any persistence context.
              Any changes made to it won't affect the DB unless it's explicitly persisted by the Repository instance.
              EX:
                public User createUser(String name) {
                    User userObj = new User(name);
                    return userRepository.save(userObj); // persist the User entity to the DB
                }

        2. Managed:
            + The "Managed" entity (entity having state of "Managed") is associated with the persistence context.
              Any changes to it is automatically synchronized with the Database upon the transaction completion.
              There's no need to call any save/update method explicitly.
                EX:
                    public User updateUser(Long id, String newName) {
                        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"))
                        user.setName(newName);
                        // No need to call save/update metho eplicitly .
                        return user;
                    }

        3. Detached:
            + The entity becomes detached from the persistence context (is no longer monitored for changes by the
              EntityManager). Any changes made to these entities won't be automatically synchronized to the DB. For
              the changes to be reflected to the DB, the entity needs to be merged back to the persistence context.

            + When ("Managed" entities get detached)?:
              _ A transaction completes.
              _ EntityManager gets closed
              _ "detach()" gets called on EntityManager.
              _ When Entities are serialized in a remote call / or when sent to another layer of application.

        4. Removed:
            + The Entity is deleted from DB (when the transaction completes).
            + An "End" state before getting garbage collected by the Garbage collector.
            EX:
                public void deleteUser(Long id) {
                    User user = userRepository.findById(id).orElseThrow(() -> new RunTimeException("User not found"));
                    userRepository.delete(user); // The entity is marked for removal and will be deleted from the DB at the end of Transaction.
                }

 */
