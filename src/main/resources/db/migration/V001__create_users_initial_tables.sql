CREATE TABLE IF NOT EXISTS user_types (
    uuid UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS addresses (
    uuid UUID PRIMARY KEY,
    street VARCHAR(100) NOT NULL,
    number VARCHAR(20) NOT NULL,
    neighborhood VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    complement VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS persons (
    uuid UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    cpf VARCHAR(15) NOT NULL UNIQUE,
    birth_date TIMESTAMP NOT NULL CHECK (birth_date <= CURRENT_DATE),
    phone VARCHAR(100) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    address_uuid UUID,

    CONSTRAINT fk_persons_address FOREIGN KEY (address_uuid)
    REFERENCES addresses(uuid)
    ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS users (
    uuid UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_types_uuid UUID NOT NULL,
    persons_uuid UUID NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_users_user_type FOREIGN KEY (user_types_uuid)
    REFERENCES user_types(uuid)
    ON DELETE RESTRICT,

    CONSTRAINT fk_users_person FOREIGN KEY (persons_uuid)
    REFERENCES persons(uuid)
    ON DELETE CASCADE
    );
