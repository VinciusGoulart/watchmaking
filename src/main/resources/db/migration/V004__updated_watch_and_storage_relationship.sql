ALTER TABLE storage
    ADD COLUMN watch_uuid UUID,
ADD CONSTRAINT fk_watch FOREIGN KEY (watch_uuid) REFERENCES watches (uuid);

ALTER TABLE watches
    DROP COLUMN image_uuid;

