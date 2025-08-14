package sadupstaff.entity.district;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
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