package paradigmesdeprogrammation.projetnfp121.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class NotationId implements Serializable {
    private static final long serialVersionUID = -1147067083937223103L;
    @Column(name = "iddevoir", nullable = false)
    private Long iddevoir;

    @Column(name = "idetudiant", nullable = false)
    private Long idetudiant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        NotationId entity = (NotationId) o;
        return Objects.equals(this.idetudiant, entity.idetudiant) &&
                Objects.equals(this.iddevoir, entity.iddevoir);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idetudiant, iddevoir);
    }

}