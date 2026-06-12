CREATE TABLE reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    usuario_id BIGINT NOT NULL,
    sala_id BIGINT NOT NULL,

    inicio DATETIME,
    fim DATETIME,

    status VARCHAR(50),
    quantidade INT NOT NULL
);
