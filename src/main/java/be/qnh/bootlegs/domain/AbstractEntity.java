package be.qnh.bootlegs.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class AbstractEntity {

    // FIELD MET OVERERVING NAAR AL DE ANDERE DOMAIN-OBJECTEN
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;


    // GETTER
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    @PrePersist // voegt datum (= createdOn) toe bij wegschrijven van een nieuw record
    public void prePersist() {
        createdOn = LocalDateTime.now();
    }

    @PreUpdate // voegt datum (= updatedOn) toe bij wegschrijven van een gewijzigd record
    public void preUpdate() {
        updatedOn = LocalDateTime.now();
    }
}
