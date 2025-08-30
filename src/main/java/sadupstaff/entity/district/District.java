package sadupstaff.entity.district;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import sadupstaff.enums.DistrictNameEnum;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "district", schema = "sudstaff")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@DynamicUpdate
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    @Nonnull
    private DistrictNameEnum name;

    @Column(name = "description")
    private String description;

    @Column(name = "max_number_sections")
    @Max(value = 13, message = "Количество участков в районе не может быть больше 13-и")
    @Min(value = 2, message = "Количество участков в районе не может быть меньше 2-х")
    @Nonnull
    private Integer maxNumberSection;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL},
            mappedBy = "district")
    private List<Section> sections = new ArrayList<>();
}