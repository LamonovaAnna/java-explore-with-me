package ru.practicum.explore.model.compilation;

import lombok.Data;
import ru.practicum.explore.model.event.Event;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "compilations")
@Data
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Long id;

    @Column(name = "pinned", nullable = false)
    private Boolean pinned;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToMany
    @JoinTable(name = "events_compilations", joinColumns = @JoinColumn(name = "ec_compilation_id",
            referencedColumnName = "compilation_id"), inverseJoinColumns = @JoinColumn(name = "ec_event_id",
            referencedColumnName = "event_id"))
    private List<Event> events;
}