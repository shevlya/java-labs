package org.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "composition")
public class Composition {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comp_seq")
    @SequenceGenerator(name = "comp_seq", sequenceName = "id_composition_seq", allocationSize = 1)
    @Column(name = "composition_id")
    private Integer id;

    @Column(name = "composition_name", nullable = false)
    private String name;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    public Composition() {
    }

    public Composition(String name, Integer duration) {
        this.name = name;
        this.duration = duration;
    }

    public Composition(String name, Integer duration, Album album) {
        this.name = name;
        this.duration = duration;
        this.album = album;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
