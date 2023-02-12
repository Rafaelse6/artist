INSERT INTO tb_artist(name, country) VALUES ('Dream Theater', 'USA');
INSERT INTO tb_artist(name, country) VALUES ('Sabaton', 'Sweden');
INSERT INTO tb_artist(name, country) VALUES ('Sepultura', 'Brazil');

INSERT INTO tb_album(name, img_Url) VALUES ('Images and Words', 'https://www.musik-sammler.de/cover/1500/1189_1564052579.jpg');
INSERT INTO tb_album(name, img_Url) VALUES ('Heroes', 'https://4.bp.blogspot.com/-3BDsAt0INC4/U5duPxuYiEI/AAAAAAAACoI/6p-YShXFJMo/s1600/Sabaton_-_Heroes_-_Artwork.jpg');
INSERT INTO tb_album(name, img_Url) VALUES ('Arise', 'https://1.bp.blogspot.com/-NKypTWD7nxk/UTWWfAoF34I/AAAAAAAAAGo/Sl1295mU7S0/s1600/url.jpg');

INSERT INTO tb_album_artist(album_id, artist_id) VALUES (1,1);
INSERT INTO tb_album_artist(album_id, artist_id) VALUES (2,2);
INSERT INTO tb_album_artist(album_id, artist_id) VALUES (3,3);