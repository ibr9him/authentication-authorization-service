/***********************************************************************************************************************
 * APPLICATION SETUP
 **********************************************************************************************************************/
/* Any application specific database configuration goes here */
CREATE DATABASE IF NOT EXISTS management_system;
USE management_system;

/***********************************************************************************************************************
 * ENTITIES DEFINITIONS
 **********************************************************************************************************************/
/**---------------------------------------------------------------------------------------------------------------------
 - Security Entities
 ---------------------------------------------------------------------------------------------------------------------*/
CREATE TABLE IF NOT EXISTS authority
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS role
(
    id                 BINARY(16) PRIMARY KEY,
    name               VARCHAR(255) NOT NULL,
    name_ar            VARCHAR(255) NOT NULL,
    code               VARCHAR(255) NOT NULL,
    created_by         VARCHAR(255) NOT NULL,
    created_date       DATETIME     NOT NULL,
    last_modified_by   VARCHAR(255),
    last_modified_date DATETIME,
    client_id          BINARY(16)   NOT NULL
);

CREATE TABLE IF NOT EXISTS role_authority
(
    role_id      BINARY(16) NOT NULL,
    authority_id BIGINT     NOT NULL
);

CREATE TABLE IF NOT EXISTS oauth_access_token
(
    token_id          VARCHAR(255),
    token             MEDIUMBLOB,
    authentication_id VARCHAR(255) PRIMARY KEY,
    user_name         VARCHAR(255),
    client_id         VARCHAR(255),
    authentication    MEDIUMBLOB,
    refresh_token     VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS oauth_refresh_token
(
    token_id       VARCHAR(255),
    token          MEDIUMBLOB,
    authentication MEDIUMBLOB
);

/**--------------------------------- Security Entities Relations -----------------------------------------------------*/
ALTER TABLE role_authority
    ADD CONSTRAINT pk_role_authority PRIMARY KEY (role_id, authority_id);
ALTER TABLE role_authority
    ADD CONSTRAINT fk_role_authority_role FOREIGN KEY (role_id) REFERENCES role (id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE role_authority
    ADD CONSTRAINT fk_role_authority_authority FOREIGN KEY (authority_id) REFERENCES authority (id) ON UPDATE CASCADE ON DELETE RESTRICT;

/**---------------------------------------------------------------------------------------------------------------------
 - Business Entities
 ---------------------------------------------------------------------------------------------------------------------*/
CREATE TABLE IF NOT EXISTS client
(
    id                 BINARY(16) PRIMARY KEY,
    name               VARCHAR(255)  NOT NULL,
    name_ar            NVARCHAR(255) NOT NULL,
    properties         JSON,
    contact_info       JSON,
    enabled            BOOLEAN       NOT NULL,
    created_by         VARCHAR(255)  NOT NULL,
    created_date       DATETIME      NOT NULL,
    last_modified_by   VARCHAR(255),
    last_modified_date DATETIME,
    activity_id        BINARY(16)    NOT NULL
);

CREATE TABLE IF NOT EXISTS user
(
    id                 BINARY(16) PRIMARY KEY,
    username           VARCHAR(255) NOT NULL UNIQUE,
    password_hash      VARCHAR(255) NOT NULL,
    properties         JSON,
    contact_info       JSON,
    name               VARCHAR(255),
    name_ar            NVARCHAR(255),
    enabled            BOOLEAN      NOT NULL,
    created_by         VARCHAR(255) NOT NULL,
    created_date       DATETIME     NOT NULL,
    last_modified_by   VARCHAR(255),
    last_modified_date DATETIME,
    client_id          BINARY(16)   NOT NULL,
    role_id            BINARY(16)   NOT NULL,
    structure_level_id BINARY(16)   NOT NULL
);

CREATE TABLE IF NOT EXISTS activity
(
    id                 BINARY(16) PRIMARY KEY,
    name               VARCHAR(255) NOT NULL UNIQUE,
    name_ar            VARCHAR(255) NOT NULL UNIQUE,
    enabled            BOOLEAN      NOT NULL,
    created_by         VARCHAR(255) NOT NULL,
    created_date       DATETIME     NOT NULL,
    last_modified_by   VARCHAR(255),
    last_modified_date DATETIME
);

CREATE TABLE IF NOT EXISTS tag_name
(
    `key`              VARCHAR(255) NOT NULL,
    locale             VARCHAR(255) NOT NULL,
    message            VARCHAR(255) NOT NULL,
    activity_id        BINARY(16)   NOT NULL,
    created_by         VARCHAR(255) NOT NULL,
    created_date       DATETIME     NOT NULL,
    last_modified_by   VARCHAR(255),
    last_modified_date DATETIME
);

CREATE TABLE IF NOT EXISTS subscription
(
    id                 BINARY(16) PRIMARY KEY,
    start_date         DATE         NOT NULL,
    end_date           DATE         NOT NULL,
    expired            BOOLEAN      NOT NULL,
    paused             BOOLEAN      NOT NULL,
    paused_since       DATETIME,
    created_by         VARCHAR(255) NOT NULL,
    created_date       DATETIME     NOT NULL,
    last_modified_by   VARCHAR(255),
    last_modified_date DATETIME,
    bundle_id          BINARY(16)   NOT NULL,
    client_id          BINARY(16)   NOT NULL
);

CREATE TABLE IF NOT EXISTS bundle
(
    id                           BINARY(16) PRIMARY KEY,
    name                         VARCHAR(255) NOT NULL UNIQUE,
    name_ar                      VARCHAR(255) NOT NULL UNIQUE,
    price                        VARCHAR(255) NOT NULL,
    `period`                     VARCHAR(255),
    currency                     VARCHAR(255),
    limited_to_number_of_users   BOOLEAN      NOT NULL,
    limited_to_number_of_clients BOOLEAN      NOT NULL,
    limited_period               BOOLEAN      NOT NULL,
    number_of_users_limit        INT,
    number_of_clients_limit      INT,
    number_of_users              INT,
    number_of_clients            INT,
    enabled                      BOOLEAN      NOT NULL,
    created_by                   VARCHAR(255) NOT NULL,
    created_date                 DATETIME     NOT NULL,
    last_modified_by             VARCHAR(255),
    last_modified_date           DATETIME
);

CREATE TABLE IF NOT EXISTS structure_level
(
    id                        BINARY(16) PRIMARY KEY,
    name                      VARCHAR(255) NOT NULL,
    name_ar                   VARCHAR(255) NOT NULL,
    properties                JSON,
    structure_level_parent_id BINARY(16),
    created_by                VARCHAR(255) NOT NULL,
    created_date              DATETIME     NOT NULL,
    last_modified_by          VARCHAR(255),
    last_modified_date        DATETIME,
    manager_user_id           BINARY(16),
    client_id                 BINARY(16)   NOT NULL
);

/**--------------------------------- Business Entities Relations -----------------------------------------------------*/
ALTER TABLE role
    ADD CONSTRAINT fk_role_client FOREIGN KEY (client_id) REFERENCES client (id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE user
    ADD CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role (id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE user
    ADD CONSTRAINT fk_user_client FOREIGN KEY (client_id) REFERENCES client (id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE user
    ADD CONSTRAINT fk_user_structure_level FOREIGN KEY (structure_level_id) REFERENCES structure_level (id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE client
    ADD CONSTRAINT fk_client_activity FOREIGN KEY (activity_id) REFERENCES activity (id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE subscription
    ADD CONSTRAINT fk_subscription_bundle FOREIGN KEY (bundle_id) REFERENCES bundle (id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE subscription
    ADD CONSTRAINT fk_subscription_client FOREIGN KEY (client_id) REFERENCES client (id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE tag_name
    ADD CONSTRAINT pk_tag_name PRIMARY KEY (`key`, locale, activity_id);
ALTER TABLE tag_name
    ADD CONSTRAINT fk_tag_name_activity FOREIGN KEY (activity_id) REFERENCES activity (id) ON UPDATE CASCADE ON DELETE CASCADE;

/***********************************************************************************************************************
 * BASIC DATA SETUP
 **********************************************************************************************************************/
/**---------------------------------------------------------------------------------------------------------------------
 - Adding Authorities For Each Entity
 ---------------------------------------------------------------------------------------------------------------------*/
INSERT INTO authority (name)
VALUES
/**--------------------------------- Role Entity ---------------------------------------------------------------------*/
    ('CREATE_ROLE'),
    ('READ_ROLE'),
    ('READ_ROLE_DETAILS'),
    ('UPDATE_ROLE'),
    ('DELETE_ROLE'),
/**--------------------------------- User Entity ---------------------------------------------------------------------*/
    ('CREATE_USER'),
    ('READ_USER'),
    ('READ_USER_DETAILS'),
    ('UPDATE_USER'),
    ('DELETE_USER'),
/**--------------------------------- Client Entity -------------------------------------------------------------------*/
    ('CREATE_CLIENT'),
    ('READ_CLIENT'),
    ('READ_CLIENT_DETAILS'),
    ('UPDATE_CLIENT'),
    ('DELETE_CLIENT'),
/**--------------------------------- Activity Entity -----------------------------------------------------------------*/
    ('CREATE_ACTIVITY'),
    ('READ_ACTIVITY'),
    ('READ_ACTIVITY_DETAILS'),
    ('UPDATE_ACTIVITY'),
    ('DELETE_ACTIVITY'),
/**--------------------------------- Subscription Entity -------------------------------------------------------------*/
    ('CREATE_SUBSCRIPTION'),
    ('READ_SUBSCRIPTION'),
    ('READ_SUBSCRIPTION_DETAILS'),
    ('UPDATE_SUBSCRIPTION'),
    ('DELETE_SUBSCRIPTION'),
/**--------------------------------- Bundle Entity -------------------------------------------------------------------*/
    ('CREATE_BUNDLE'),
    ('READ_BUNDLE'),
    ('READ_BUNDLE_DETAILS'),
    ('UPDATE_BUNDLE'),
    ('DELETE_BUNDLE'),
/**--------------------------------- Structure Level Entity ----------------------------------------------------------*/
    ('CREATE_STRUCTURE_LEVEL'),
    ('READ_STRUCTURE_LEVEL'),
    ('READ_STRUCTURE_LEVEL_DETAILS'),
    ('UPDATE_STRUCTURE_LEVEL'),
    ('DELETE_STRUCTURE_LEVEL'),
/**--------------------------------- UI Elements ----------------------------------------------------------*/
    ('ADMIN_TOOLS');

/**---------------------------------------------------------------------------------------------------------------------
 - Setting up system admin user
 ---------------------------------------------------------------------------------------------------------------------*/
/**-------------------------------- Adding The Default Bundle --------------------------------------------------------*/
INSERT INTO bundle (id, name, name_ar, price, enabled, limited_to_number_of_users, limited_to_number_of_clients,
                    limited_period, number_of_users_limit, number_of_clients_limit, number_of_users, number_of_clients,
                    created_by, created_date)
VALUES (0xB426179879D211EA88190242AC180002, 'NAME', 'NAMEAR', 0, TRUE, TRUE, TRUE, FALSE, 1, 1, 1, 1, 'CREATED BY',
        '2019-01-01');

/**-------------------------------- Adding The Default Activity ------------------------------------------------------*/
INSERT INTO activity (id, name, name_ar, enabled, created_by, created_date)
VALUES (0xAD37EBA787F711EABEBE0242AC180002, 'DEFAULT ACTIVITY', 'النشاط التلقائي', TRUE, 'CREATED BY', '2019-01-01');

/**--------------------------------- Adding The System Admin Client --------------------------------------------------*/
INSERT INTO client (id, name, name_ar, enabled, created_by, created_date, activity_id)
VALUES (0x21CF82FF2F784D4FA3199BD274D3A051, 'System Admin', 'مسؤول النظام', TRUE, 'CREATED_BY', '2020-01-01 00:00:00',
        0xAD37EBA787F711EABEBE0242AC180002);

/**-------------------------------- Adding The System Admin Structure Level ------------------------------------------*/
INSERT INTO structure_level (id, name, name_ar, created_by, created_date, client_id)
VALUES (0xDB2209A883AD11EA83390242AC180002, 'DEFAULT STRUCTURE LEVELS', 'الهيكلة الادارية التلقائي', 'CREATED BY',
        '2019-01-01',
        0x21CF82FF2F784D4FA3199BD274D3A051);

/**--------------------------------- Adding System Admin Role --------------------------------------------------------*/
INSERT INTO role(id, name, name_ar, code, created_by, created_date, client_id)
VALUES (0x99DE27BE79D211EA88190242AC180002, 'SYSTEM ADMIN', 'مسؤول النظام', 'SYSTEM_ADMIN', 'SYSTEM', '2019-01-01',
        0x21CF82FF2F784D4FA3199BD274D3A051);

/**--------------------------------- Adding The System Admin User ----------------------------------------------------*/
INSERT INTO user (id, username, password_hash, name, name_ar, enabled, created_by, created_date, client_id, role_id,
                  structure_level_id)
VALUES (0x8BE3391ACBD34672A82CC9B3D4511461, 'sysadmin', '$2a$10$1E3.WsTRX2OXCOsWniUXSOwTIWMSsYtlyKunymRKtwlniLGw.a3Gm',
        'System Administrator', 'مسؤول النظام', TRUE, 'CREATED_BY', '2020-03-21 20:48:28',
        0x21CF82FF2F784D4FA3199BD274D3A051, 0x99DE27BE79D211EA88190242AC180002, 0xDB2209A883AD11EA83390242AC180002);

/**--------------------------------- Giving The System Admin User All Authorities ------------------------------------*/
INSERT INTO role_authority (role_id, authority_id)
VALUES (0x99DE27BE79D211EA88190242AC180002, 1),
       (0x99DE27BE79D211EA88190242AC180002, 2),
       (0x99DE27BE79D211EA88190242AC180002, 3),
       (0x99DE27BE79D211EA88190242AC180002, 4),
       (0x99DE27BE79D211EA88190242AC180002, 5),

       (0x99DE27BE79D211EA88190242AC180002, 6),
       (0x99DE27BE79D211EA88190242AC180002, 7),
       (0x99DE27BE79D211EA88190242AC180002, 8),
       (0x99DE27BE79D211EA88190242AC180002, 9),
       (0x99DE27BE79D211EA88190242AC180002, 10),

       (0x99DE27BE79D211EA88190242AC180002, 11),
       (0x99DE27BE79D211EA88190242AC180002, 12),
       (0x99DE27BE79D211EA88190242AC180002, 13),
       (0x99DE27BE79D211EA88190242AC180002, 14),
       (0x99DE27BE79D211EA88190242AC180002, 15),

       (0x99DE27BE79D211EA88190242AC180002, 16),
       (0x99DE27BE79D211EA88190242AC180002, 17),
       (0x99DE27BE79D211EA88190242AC180002, 18),
       (0x99DE27BE79D211EA88190242AC180002, 19),
       (0x99DE27BE79D211EA88190242AC180002, 20),

       (0x99DE27BE79D211EA88190242AC180002, 21),
       (0x99DE27BE79D211EA88190242AC180002, 22),
       (0x99DE27BE79D211EA88190242AC180002, 23),
       (0x99DE27BE79D211EA88190242AC180002, 24),
       (0x99DE27BE79D211EA88190242AC180002, 25),

       (0x99DE27BE79D211EA88190242AC180002, 26),
       (0x99DE27BE79D211EA88190242AC180002, 27),
       (0x99DE27BE79D211EA88190242AC180002, 28),
       (0x99DE27BE79D211EA88190242AC180002, 29),
       (0x99DE27BE79D211EA88190242AC180002, 30),

       (0x99DE27BE79D211EA88190242AC180002, 31),
       (0x99DE27BE79D211EA88190242AC180002, 32),
       (0x99DE27BE79D211EA88190242AC180002, 33),
       (0x99DE27BE79D211EA88190242AC180002, 34),
       (0x99DE27BE79D211EA88190242AC180002, 35),
       (0x99DE27BE79D211EA88190242AC180002, 36);
