package sadupstaff.entity.district;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import sadupstaff.enums.PositionSectionEmployeeEnum;
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
    @GeneratedValue(strategy = GenerationType.UUID)
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

    @Column(name = "position")
    @Enumerated(EnumType.STRING)
    @Nonnull
    private PositionSectionEmployeeEnum position;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
    @JoinColumn(name = "section_id")
    private Section section;
}