package sadupstaff.entity.district;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import sadupstaff.enums.DistrictName;
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
    private DistrictName name;

    @Column(name = "description")
    private String description;

    @Column(name = "max_number_sections")
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