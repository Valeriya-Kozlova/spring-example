package hexlet.code.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.Set;

import static jakarta.persistence.GenerationType.AUTO;

@Entity
@Getter
@Setter
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Task {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private TaskStatus taskStatus;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    private Set<Label> labels;

    @NotBlank
    @Size(min = 3, max = 1000)
    private String name;

    private Integer index;

    private String description;

    @CreatedDate
    private LocalDate createdAt;
}

