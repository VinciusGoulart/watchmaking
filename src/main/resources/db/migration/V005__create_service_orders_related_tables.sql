CREATE TYPE service_type AS ENUM ('BATTERY_CHANGE', 'BRACELET_ADJUSTMENT', 'GLASS_REPLACEMENT', 'CLEANING',
                                        'MECHANISM_REPAIR', 'INSPECTION_ONLY');
CREATE TYPE service_status AS ENUM ('OPEN', 'IN_PROGRESS', 'DONE', 'DELIVERED', 'CANCELED');

CREATE TABLE IF NOT EXISTS watch_parts(
    uuid        UUID               PRIMARY KEY,
    name        VARCHAR(100)       NOT NULL,
    code        VARCHAR(50)        UNIQUE NOT NULL,
    description TEXT,
    stock       INTEGER            NOT NULL DEFAULT 0,
    unit_price  DECIMAL            NOT NULL,
    created_at  TIMESTAMP DEFAULT  CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT  CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS service_orders(
    uuid            UUID            PRIMARY KEY,
    description     TEXT            NOT NULL,
    service_type    service_type    NOT NULL DEFAULT('INSPECTION_ONLY'),
    status          service_status  NOT NULL DEFAULT('OPEN'),
    price           DECIMAL,
    entry_date      TIMESTAMP       NOT NULL,
    delivery_date   TIMESTAMP,
    note            TEXT,
    created_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    customer_uuid   UUID            NOT NULL,
    watch_uuid      UUID            NOT NULL,
    technician_uuid UUID            NOT NULL,

    CONSTRAINT fk_service_orders_customer FOREIGN KEY (customer_uuid) REFERENCES persons (uuid),
    CONSTRAINT fk_service_orders_watch FOREIGN KEY (watch_uuid) REFERENCES watches (uuid),
    CONSTRAINT fk_service_orders_technician FOREIGN KEY (technician_uuid) REFERENCES users (uuid)
);

CREATE TABLE IF NOT EXISTS service_order_items(
    uuid               UUID           PRIMARY KEY,
    quantity           INTEGER        NOT NULL CHECK (quantity > 0),
    unit_price         DECIMAL        NOT NULL,
    subtotal           DECIMAL        GENERATED ALWAYS AS (quantity * unit_price) STORED,

    service_order_uuid UUID           NOT NULL,
    watch_part_uuid    UUID           NOT NULL,

    CONSTRAINT fk_items_order FOREIGN KEY (service_order_uuid) REFERENCES service_orders (uuid),
    CONSTRAINT fk_items_part FOREIGN KEY (watch_part_uuid) REFERENCES watch_parts (uuid)
);
