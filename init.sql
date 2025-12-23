USE enadzornik;

CREATE TABLE IF NOT EXISTS skola
(
    skola_id     INT PRIMARY KEY AUTO_INCREMENT,
    skola_naziv  VARCHAR(150) NOT NULL,
    skola_adresa VARCHAR(255)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS korisnik
(
    korisnik_id       INT PRIMARY KEY AUTO_INCREMENT,
    ime_korisnika     VARCHAR(50)                               NOT NULL,
    prezime_korisnika VARCHAR(50)                               NOT NULL,
    email             VARCHAR(100)                              NOT NULL UNIQUE,
    lozinka           VARCHAR(255)                              NOT NULL,
    uloga_korisnika   ENUM ('admin', 'nastavnik', 'supervizor') NOT NULL,
    skola_id          INT                                       NOT NULL,
    aktivan           BOOLEAN  DEFAULT TRUE,
    datum_kreiranja   DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (skola_id) REFERENCES skola (skola_id) ON DELETE RESTRICT
) ENGINE = InnoDB;

CREATE TABLE materijal
(
    materijal_id             INT PRIMARY KEY AUTO_INCREMENT,
    naslov                   VARCHAR(200)                                      NOT NULL,
    tip_materijala           ENUM ('priprema', 'mesecni_plan', 'video_snimak') NOT NULL,
    format                   VARCHAR(10)                                       NOT NULL CHECK (format IN ('pdf', 'mp4')),
    putanja_fajla            VARCHAR(255)                                      NOT NULL,
    velicina_mb              DECIMAL(6, 2)                                     NOT NULL CHECK (velicina_mb > 0),
    datum_uploada            DATETIME                                    DEFAULT CURRENT_TIMESTAMP,
    nastavnik_id             INT                                               NOT NULL,
    predmet                  VARCHAR(100)                                      NOT NULL,
    razred                   VARCHAR(20)                                       NOT NULL,
    nastavna_jedinica        VARCHAR(200),
    datum_casa               DATE,
    mesec_planiranja         DATE,
    status                   ENUM ('na_cekanju', 'potvrdjen', 'odbijen') DEFAULT 'na_cekanju',
    poslednji_izmenio_status INT                                               NULL,
    FOREIGN KEY (nastavnik_id) REFERENCES korisnik (korisnik_id) ON DELETE CASCADE,
    FOREIGN KEY (poslednji_izmenio_status) REFERENCES korisnik (korisnik_id) ON DELETE SET NULL,
    INDEX idx_nastavnik (nastavnik_id),
    INDEX idx_status (status),
    INDEX idx_tip (tip_materijala),
    INDEX idx_datum_casa (datum_casa),
    INDEX idx_mesec_plan (mesec_planiranja),
    INDEX idx_predmet_razred (predmet, razred)
) ENGINE = InnoDB;

CREATE TABLE evaluacija
(
    evaluacija_id    INT PRIMARY KEY AUTO_INCREMENT,
    materijal_id     INT                           NOT NULL,
    evaluator_id     INT                           NOT NULL,
    status           ENUM ('potvrdjen', 'odbijen') NOT NULL,
    napomena         TEXT,
    datum_evaluacije DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (materijal_id) REFERENCES materijal (materijal_id) ON DELETE CASCADE,
    FOREIGN KEY (evaluator_id) REFERENCES korisnik (korisnik_id) ON DELETE CASCADE,
    UNIQUE uk_materijal_evaluator (materijal_id, evaluator_id)
) ENGINE = InnoDB;

CREATE TABLE istorija_promena
(
    promena_id    INT PRIMARY KEY AUTO_INCREMENT,
    materijal_id  INT                                         NOT NULL,
    stari_status  ENUM ('na_cekanju', 'potvrdjen', 'odbijen') NULL,
    novi_status   ENUM ('na_cekanju', 'potvrdjen', 'odbijen') NOT NULL,
    izmenio_id    INT                                         NOT NULL,
    napomena      TEXT,
    vreme_promene DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (materijal_id) REFERENCES materijal (materijal_id) ON DELETE CASCADE,
    FOREIGN KEY (izmenio_id) REFERENCES korisnik (korisnik_id) ON DELETE CASCADE,
    INDEX idx_materijal (materijal_id),
    INDEX idx_vreme (vreme_promene)
) ENGINE = InnoDB;
