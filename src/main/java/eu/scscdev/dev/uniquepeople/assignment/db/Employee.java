package eu.scscdev.dev.uniquepeople.assignment.db;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
@With
@NamedQueries(
    @NamedQuery(name = "Employee.findByName", query = "select e from Employee e where e.firstName = :firstName and e.lastName = :lastName")
)
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Company company;
}
