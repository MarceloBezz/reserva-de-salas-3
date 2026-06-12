CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(150) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    nome VARCHAR(50) NOT NULL,
    telefone VARCHAR(12),

    CONSTRAINT pk_usuarios PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_email UNIQUE (email)
);

CREATE TABLE roles (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,

    CONSTRAINT pk_roles PRIMARY KEY (id),
    CONSTRAINT uk_roles_nome UNIQUE (nome)
);

CREATE TABLE usuarios_roles (
    usuario_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    CONSTRAINT pk_usuario_roles PRIMARY KEY (usuario_id, role_id),

    CONSTRAINT fk_usuario_roles_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_usuario_roles_role
        FOREIGN KEY (role_id)
        REFERENCES roles (id)
        ON DELETE CASCADE
);

INSERT INTO roles(nome) VALUES('CLIENTE');
INSERT INTO roles(nome) VALUES('ADMIN');

