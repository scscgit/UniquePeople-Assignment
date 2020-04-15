package eu.scscdev.dev.uniquepeople.assignment.db;

import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@With
@ToString
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Version
    @ToString.Exclude
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(mappedBy = "company")
    @NotNull
    @Builder.Default
    // Explicitly unique instead of using Set<Employee>
    @Column(unique = true)
    private List<Employee> employees = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        Assert.notNull(id, "Cannot compare Entity before defining a key");
        if (this == o) return true;
        if (!(o instanceof Company)) return false;
        Company company = (Company) o;
        return id.equals(company.id);
    }

    @Override
    public int hashCode() {
        Assert.notNull(id, "Cannot compare Entity before defining a key");
        return Objects.hash(id);
    }
}
