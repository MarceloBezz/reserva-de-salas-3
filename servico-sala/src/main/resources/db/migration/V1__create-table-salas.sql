CREATE TABLE salas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    capacidade INT NOT NULL,
    ativa BOOLEAN NOT NULL
);