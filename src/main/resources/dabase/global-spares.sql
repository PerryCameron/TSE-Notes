create table spares
(
    id                     INTEGER
        primary key autoincrement,
    pim                    TEXT,
    spare_item             TEXT,
    replacement_item       TEXT,
    standard_exchange_item TEXT,
    spare_description      TEXT,
    catalogue_version      TEXT,
    end_of_service_date    TEXT,
    last_update            TEXT,
    added_to_catalogue     TEXT,
    removed_from_catalogue TEXT,
    comments               TEXT,
    keywords               TEXT,
    archived               INTEGER not null,
    custom_add             INTEGER not null,
    last_updated_by        TEXT,
    check (archived IN (0, 1)),
    check (custom_add IN (0, 1))
);

create table spare_pictures
(
    id       INTEGER
        primary key autoincrement,
    spare_id INTEGER not null
        unique
        references spares
            on delete cascade,
    picture  BLOB    not null
);

create table ranges
(
    id               INTEGER
        primary key autoincrement,
    range            TEXT,
    range_additional TEXT,
    range_type       TEXT,
    last_update      TEXT default CURRENT_TIMESTAMP,
    last_updated_by  TEXT
);

create table properties
(
    created_by             TEXT,
    last_modified_by       TEXT,
    creation_date          TEXT,
    last_modification_date TEXT
);

create table replacement_cr
(
    id              INTEGER
        primary key autoincrement,
    item            TEXT,
    replacement     TEXT,
    comment         TEXT,
    old_qty         REAL,
    new_qty         REAL,
    last_update     TEXT,
    last_updated_by TEXT
);