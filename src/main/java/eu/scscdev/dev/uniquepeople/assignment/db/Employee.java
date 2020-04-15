package eu.scscdev.dev.uniquepeople.assignment.db;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@With
@NamedQueries(
    @NamedQuery(name = "Employee.findByName", query = "select e from Employee e where e.firstName = :firstName and e.lastName = :lastName")
)
@ToString
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Version
    @ToString.Exclude
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Length(min = 1)
    private String firstName;

    @NotNull
    @Length(min = 1)
    private String lastName;

    @NotNull
    @Length(min = 1)
    private String address;

    @ManyToOne
    // Bi-directional recursion prevention. Workaround for an open Lombok issue 2255
    @ToString.Exclude
    private Company company;

    @Override
    public boolean equals(Object o) {
        Assert.notNull(id, "Cannot compare Entity before defining a key");
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        // Throw exception on null
        return id.equals(employee.id);
    }

    @Override
    public int hashCode() {
        Assert.notNull(id, "Cannot compare Entity before defining a key");
        return Objects.hash(id);
    }
}
