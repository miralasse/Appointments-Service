ALTER TABLE specialists DROP COLUMN room_number;

ALTER TABLE schedules DROP COLUMN active;

ALTER TABLE schedules ADD COLUMN room_number VARCHAR(16) NOT NULL DEFAULT '';