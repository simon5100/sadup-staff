package sadupstaff.entity.management;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import sadupstaff.enums.DepartmentNameEnum;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "departments", schema = "sudstaff")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    @Nonnull
    private DepartmentNameEnum name;

    @Column(name = "max_number_employees")
    @Nonnull
    private Integer maxNumberEmployees;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL},
            mappedBy = "department")
    private List<Employee> emps = new ArrayList<>();

}