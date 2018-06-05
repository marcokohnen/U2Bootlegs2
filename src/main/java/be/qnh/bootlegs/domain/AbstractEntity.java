package be.qnh.bootlegs.domain;

import javax.persistence.*;

@MappedSuperclass
public abstract class AbstractEntity {

    // FIELD MET OVERERVING NAAR AL DE ANDERE DOMAIN-OBJECTEN
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    // GETTER
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
