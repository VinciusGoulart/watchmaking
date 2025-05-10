CREATE TABLE IF NOT EXISTS brands (
    uuid UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    country VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS watch_types (
    uuid UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS watch_categories (
    uuid UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS storage (
    uuid UUID PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    url TEXT NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    size BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS watches (
    uuid UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL NOT NULL,
    quantity INT NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    reference VARCHAR(100) NOT NULL,
    mechanism VARCHAR(50),
    gender VARCHAR(20),
    material VARCHAR(100),
    water_resistance VARCHAR(50),
    dial_color VARCHAR(50),
    strap_material VARCHAR(100),

    brand_uuid UUID,
    watch_type_uuid UUID,
    category_uuid UUID,
    image_uuid UUID,

    CONSTRAINT fk_watch_brand FOREIGN KEY (brand_uuid) REFERENCES brands(uuid),
    CONSTRAINT fk_watch_type FOREIGN KEY (watch_type_uuid) REFERENCES watch_types(uuid),
    CONSTRAINT fk_watch_category FOREIGN KEY (category_uuid) REFERENCES watch_categories(uuid),
    CONSTRAINT fk_watch_image FOREIGN KEY (image_uuid) REFERENCES storage(uuid)
    );
