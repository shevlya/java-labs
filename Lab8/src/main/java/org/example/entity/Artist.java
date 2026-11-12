package org.example.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "artist")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "artist_seq")
    @SequenceGenerator(name = "artist_seq", sequenceName = "id_artist_seq", allocationSize = 1)
    @Column(name = "artist_id")
    private Integer id;

    @Column(name = "artist_name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Album> albums;

    public Artist() {
    }

    public Artist(String name) {
        this.name = name;
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

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }
}
