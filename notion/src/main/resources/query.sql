CREATE
DATABASE notion;

USE
notion;

CREATE TABLE user
(
    userid      INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    emailid     VARCHAR(255) NOT NULL UNIQUE,
    xstatus     TINYINT      NOT NULL COMMENT '0: inactive, 1: active',
    lastloginat DATETIME,
    createdat   DATETIME     NOT NULL,
    updatedat   DATETIME
);

CREATE TABLE master_template
(
    templateid INT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    xstatus    TINYINT      NOT NULL COMMENT '0: inactive, 1: active',
    createdat  DATETIME     NOT NULL,
    createdby  INT          NOT NULL COMMENT 'userid',
    updatedat  DATETIME,
    updatedby  INT COMMENT 'userid',
    CONSTRAINT fk_createdby FOREIGN KEY (createdby) REFERENCES user (userid),
    CONSTRAINT fk_updatedby FOREIGN KEY (updatedby) REFERENCES user (userid)
);

-- Change createdby as per your user id
-- Insert Task Management Template
INSERT INTO master_template (title, content, xstatus, createdat, createdby)
VALUES ('Task Management Template',
        '<h2>Task Management</h2><ul><li><input type="checkbox" /> Task 1</li><li><input type="checkbox" /> Task 2</li><li><input type="checkbox" /> Task 3</li></ul><p>Use this template to manage your tasks efficiently.</p>',
        1,
        NOW(),
        10);

-- Insert Meeting Notes Template
INSERT INTO master_template (title, content, xstatus, createdat, createdby)
VALUES ('Meeting Notes Template',
        '<h2>Meeting Notes</h2><p><strong>Date</strong>: {{Date}}</p><p><strong>Attendees</strong>: {{Attendees}}</p><h3>Agenda</h3><ul><li>Item 1</li><li>Item 2</li></ul><h3>Notes</h3><ul><li>Note 1</li><li>Note 2</li></ul>',
        1,
        NOW(),
        10);

-- Insert Project Planning Template
INSERT INTO master_template (title, content, xstatus, createdat, createdby)
VALUES ('Project Planning Template',
        '<h2>Project Plan</h2><h3>Project Overview</h3><p>{{Project Overview}}</p><h3>Milestones</h3><ol><li>Milestone 1</li><li>Milestone 2</li></ol><h3>Tasks</h3><ul><li><input type="checkbox" /> Task 1</li><li><input type="checkbox" /> Task 2</li></ul>',
        1,
        NOW(),
        10);

-- Insert Personal Journal Template
INSERT INTO master_template (title, content, xstatus, createdat, createdby)
VALUES ('Personal Journal Template',
        '<h2>Daily Journal</h2><p><strong>Date</strong>: {{Date}}</p><h3>What went well today?</h3><ul><li></li></ul><h3>What could have been better?</h3><ul><li></li></ul><h3>Thoughts & Reflections</h3><ul><li></li></ul>',
        1,
        NOW(),
        10);

-- Insert Weekly Agenda Template
INSERT INTO master_template (title, content, xstatus, createdat, createdby)
VALUES ('Weekly Agenda Template',
        '<h2>Weekly Agenda</h2><h3>Monday</h3><ul><li><input type="checkbox" /> Task 1</li><li><input type="checkbox" /> Task 2</li></ul><h3>Tuesday</h3><ul><li><input type="checkbox" /> Task 3</li><li><input type="checkbox" /> Task 4</li></ul><h3>Notes</h3><ul><li></li></ul>',
        1,
        NOW(),
        10);

CREATE TABLE document
(
    documentid INT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    xstatus    TINYINT      NOT NULL COMMENT '0: inactive, 1: active',
    createdat  DATETIME     NOT NULL,
    createdby  INT          NOT NULL COMMENT 'userid',
    updatedat  DATETIME,
    updatedby  INT,
    parentid   INT,
    templateid INT,
    FOREIGN KEY (createdby) REFERENCES user (userid),
    FOREIGN KEY (updatedby) REFERENCES user (userid),
    FOREIGN KEY (parentid) REFERENCES document (documentid),
    FOREIGN KEY (templateid) REFERENCES master_template (templateid)
);

CREATE TABLE document_history
(
    documenthistoryid INT AUTO_INCREMENT PRIMARY KEY,
    title             VARCHAR(255) NOT NULL,
    content           TEXT         NOT NULL,
    documentid        INT          NOT NULL,
    createdat         DATETIME     NOT NULL,
    createdby         INT          NOT NULL,
    version           INT          NOT NULL,
    FOREIGN KEY (documentid) REFERENCES document (documentid),
    FOREIGN KEY (createdby) REFERENCES user (userid)
);

CREATE TABLE document_upload
(
    documentuploadid INT AUTO_INCREMENT PRIMARY KEY,
    documentname     VARCHAR(255)   NOT NULL,
    documenttype     VARCHAR(100)   NOT NULL,
    documentpath     VARCHAR(255)   NOT NULL,
    documentsize     DECIMAL(15, 2) NOT NULL,
    xstatus          TINYINT        NOT NULL COMMENT '0: inactive, 1: active',
    documentid       INT            NOT NULL,
    uploadedat       DATETIME       NOT NULL,
    uploadedby       INT            NOT NULL,
    FOREIGN KEY (documentid) REFERENCES document (documentid),
    FOREIGN KEY (uploadedby) REFERENCES user (userid)
);


