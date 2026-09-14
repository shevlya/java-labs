INSERT INTO artist (artist_id, artist_name) VALUES
    (nextval('id_artist_seq'),'Georg Philipp Telemann'),
    (nextval('id_artist_seq'),'Johann Sebastian Bach'),
    (nextval('id_artist_seq'),'Wolfgang Amadeus Mozart'),
    (nextval('id_artist_seq'),'Ludwig van Beethoven'),
    (nextval('id_artist_seq'),'Antonio Vivaldi');

INSERT INTO album (album_id, album_name, genre, artist_id) VALUES
    (nextval('id_album_seq'),'Tafelmusik', 'Baroque', 1),
    (nextval('id_album_seq'),'Brandenburg Concertos', 'Baroque', 2),
    (nextval('id_album_seq'),'Symphony 40', 'Classical', 3),
    (nextval('id_album_seq'),'Symphony 5', 'Classical', 4),
    (nextval('id_album_seq'),'The Four Seasons', 'Baroque', 5),
    (nextval('id_album_seq'),'Sonatas and Partitas', 'Baroque', 2);

INSERT INTO composition (composition_id, composition_name, duration, album_id) VALUES
    (nextval('id_composition_seq'),'Overture in E# major', 6, 1),
    (nextval('id_composition_seq'),'Air in A minor', 5, 1),
    (nextval('id_composition_seq'),'Concerto 1 in F major', 4, 2),
    (nextval('id_composition_seq'),'Concerto 2 in F major', 6, 2),
    (nextval('id_composition_seq'),'Andante', 4, 3),
    (nextval('id_composition_seq'),'Allegro', 6, 4),
    (nextval('id_composition_seq'),'Spring', 10, 5),
    (nextval('id_composition_seq'),'Fugue in G minor', 6, 6);
