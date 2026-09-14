DROP TABLE IF EXISTS composition;
DROP TABLE IF EXISTS album;
DROP TABLE IF EXISTS artist;
DROP SEQUENCE IF EXISTS id_artist_seq;
DROP SEQUENCE IF EXISTS id_album_seq;
DROP SEQUENCE IF EXISTS id_composition_seq;

CREATE SEQUENCE id_artist_seq START 1;
CREATE SEQUENCE id_album_seq START 1;
CREATE SEQUENCE id_composition_seq START 1;

CREATE TABLE artist (
    artist_id INT NOT NULL PRIMARY KEY DEFAULT nextval('id_artist_seq'),
    artist_name TEXT NOT NULL
);

CREATE TABLE album (
    album_id INT NOT NULL PRIMARY KEY DEFAULT nextval('id_album_seq'),
    album_name TEXT NOT NULL,
    genre VARCHAR(50) NOT NULL,
    artist_id INT NOT NULL,
    CONSTRAINT album_artist_id_fk FOREIGN KEY (artist_id) REFERENCES artist(artist_id)
);

CREATE TABLE composition (
    composition_id INT NOT NULL PRIMARY KEY DEFAULT nextval('id_composition_seq'),
    composition_name TEXT NOT NULL,
    duration INT NOT NULL,
    album_id INT NOT NULL,
    CONSTRAINT composition_album_id_fk FOREIGN KEY (album_id) REFERENCES album(album_id)
);