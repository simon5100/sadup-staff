package sadupstaff.entity.district;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import sadupstaff.entity.management.Department;
import sadupstaff.enums.SectionEmployeeEnum;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "section_employee", schema = "sudstaff")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SectionEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "personel_number")
    @Nonnull
    private String personelNumber;

    @Column(name = "first_name")
    @Nonnull
    private String firstName;

    @Column(name = "last_name")
    @Nonnull
    private String lastName;

    @Column(name = "patronymic")
    private String patronymic;

    //переделать
    @Column(name = "position")
    @Enumerated(EnumType.STRING)
    private SectionEmployeeEnum position;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "section_id")
    private Section empSection;

}
