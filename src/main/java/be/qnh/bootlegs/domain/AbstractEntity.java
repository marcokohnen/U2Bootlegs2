package be.qnh.bootlegs.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity {

    // FIELD MET OVERERVING NAAR AL DE ANDERE DOMAIN-OBJECTEN
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // GETTER
    public Long getId() {
        return id;
    }
}
