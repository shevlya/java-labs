package org.example.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "album")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "album_seq")
    @SequenceGenerator(name = "album_seq", sequenceName = "id_album_seq", allocationSize = 1)
    @Column(name = "album_id")
    private Integer id;

    @Column(name = "album_name", nullable = false)
    private String name;

    @Column(name = "genre", nullable = false)
    private String genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Composition> compositions;

    public Album() {
    }

    public Album(String name, String genre) {
        this.name = name;
        this.genre = genre;
    }

    public Album(String name, String genre, Artist artist) {
        this.name = name;
        this.genre = genre;
        this.artist = artist;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public List<Composition> getCompositions() {
        return compositions;
    }

    public void setCompositions(List<Composition> compositions) {
        this.compositions = compositions;
    }
}
