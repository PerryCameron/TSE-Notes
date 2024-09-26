create table Notes
(
    id                             INTEGER
        primary key autoincrement,
    timestamp                      TEXT    not null,
    workOrder                      TEXT,
    caseNumber                     TEXT,
    serialNumber                   TEXT,
    modelNumber                    TEXT,
    callInPerson                   TEXT,
    callInPhoneNumber              TEXT,
    callInEmail                    TEXT,
    underWarranty                  INTEGER not null,
    activeServiceContract          TEXT,
    serviceLevel                   TEXT,
    schedulingTerms                TEXT,
    upsStatus                      TEXT,
    loadSupported                  INTEGER not null,
    issue                          TEXT,
    contactName                    TEXT,
    contactPhoneNumber             TEXT,
    contactEmail                   TEXT,
    street                         TEXT,
    installedAt                    TEXT,
    city                           TEXT,
    state                          TEXT,
    zip                            TEXT,
    country                        TEXT,
    createdWorkOrder               TEXT,
    tex                            TEXT,
    partsOrder                     INTEGER,
    completed                      INTEGER not null,
    isEmail                        INTEGER not null,
    additionalCorrectiveActionText TEXT,
    relatedCaseNumber              TEXT,
    title                          TEXT,
    check (completed IN (0, 1)),
    check (isEmail IN (0, 1)),
    check (loadSupported IN (0, 1)),
    check (underWarranty IN (0, 1))
);

create table PartOrders
(
    id          INTEGER primary key autoincrement,
    noteId      INTEGER not null references Notes,
    orderNumber TEXT
);

create table Parts
(
    id              INTEGER primary key autoincrement,
    partOrderId     INTEGER not null references PartOrders,
    partNumber      TEXT,
    partDescription TEXT,
    partQuantity    TEXT,
    serialReplaced  TEXT,
    partEditable    INTEGER not null,
    check (partEditable IN (0, 1))
);

CREATE TABLE user
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name  TEXT NOT NULL,
    last_name   TEXT NOT NULL,
    email       TEXT,
    sesa_number TEXT,
    url         TEXT
);

CREATE TABLE entitlements
(
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    name     TEXT NOT NULL,
    includes TEXT,
    url      TEXT
)