package sadupstaff.entity.district;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "section", schema = "sudstaff")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "personel_number")
    @Nonnull
    private String personelNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "max_number_employees_section")
    @Max(value = 4, message = "Сотрудников на участке не может быть больше 4-х")
    @Min(value = 3, message = "Сотрудников на участке не может быть меньше 3-х")
    @Nonnull
    private Integer maxNumberEmployeeSection;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
    @JoinColumn(name = "district_id")
    private District district;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL},
            mappedBy = "section")
    private List<SectionEmployee> empsSect = new ArrayList<>();
}