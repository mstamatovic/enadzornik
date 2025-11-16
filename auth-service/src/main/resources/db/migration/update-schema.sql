CREATE TABLE korisnik
(
    korisnik_id       BIGINT AUTO_INCREMENT NOT NULL,
    ime_korisnika     VARCHAR(50)  NOT NULL,
    prezime_korisnika VARCHAR(50)  NOT NULL,
    email             VARCHAR(100) NOT NULL,
    lozinka           VARCHAR(255) NOT NULL,
    uloga_korisnika   VARCHAR(255) NOT NULL,
    aktivan           BIT(1)       NOT NULL,
    datum_kreiranja   datetime NULL,
    skola_id          BIGINT       NOT NULL,
    CONSTRAINT pk_korisnik PRIMARY KEY (korisnik_id)
);

CREATE TABLE skola
(
    skola_id     BIGINT AUTO_INCREMENT NOT NULL,
    skola_naziv  VARCHAR(150) NOT NULL,
    skola_adresa VARCHAR(255) NULL,
    CONSTRAINT pk_skola PRIMARY KEY (skola_id)
);

ALTER TABLE korisnik
    ADD CONSTRAINT uc_korisnik_email UNIQUE (email);

ALTER TABLE korisnik
    ADD CONSTRAINT FK_KORISNIK_ON_SKOLA FOREIGN KEY (skola_id) REFERENCES skola (skola_id);