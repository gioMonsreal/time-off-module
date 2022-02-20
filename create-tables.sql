create table users (
	username varchar(50) primary key not null,
	password varchar(100) not null,
	enabled smallint not null default 1
);

create table authorities (
	username varchar(50) not null,
	authority varchar(50) not null,
	constraint fk_users_authorities_username foreign key (username) references users (username)
);

create table ids (
	id serial not null,
	username varchar(50) primary key not null,
	constraint fk_users_ids_username foreign key (username) references users (username)
);

create table oauth2_registered_client(
	id varchar(100) primary key not null,
	client_id varchar(100) not null,
	client_id_issued_at timestamp without time zone not null,
	client_secret varchar(200) not null,
	client_secret_expires_at timestamp without time zone,
	client_name varchar(200) not null,
	client_authentication_methods varchar(1000) not null,
	authorization_grant_types varchar(1000) not null,
	redirect_uris varchar(1000) not null,
	scopes varchar(1000) not null,
	client_settings varchar(2000) not null,
	token_settings varchar(2000) not null
);

insert into users (username, password) values
('carlos.reyes@theksquaregroup.com', '$2a$10$1sQX19Kb86igELRBIeJrAeZGWZSpYXSV1EAxTL7mDOkQTJJYbEuWC'),
('guillermo.ceme@theksquaregroup.com', '$2a$10$vEX9Z3KphBjT3Eq2VsnqSuz9.8Hu1exHb23gg038xeQgwZzhVRYQ6'),
('julio.vargas@theksquaregroup.com', '$2a$10$RYJNWpmq8eTUAyYqxH5o1Ova7qMc0J8YLrUtiu7mWfLOg.kMOAPtG');

insert into authorities values
('carlos.reyes@theksquaregroup.com', 'ROLE_ADMIN'),
('guillermo.ceme@theksquaregroup.com', 'ROLE_MANAGER'),
('julio.vargas@theksquaregroup.com', 'ROLE_USER');

insert into ids (username) values
('carlos.reyes@theksquaregroup.com'),
('guillermo.ceme@theksquaregroup.com'),
('julio.vargas@theksquaregroup.com');

insert into oauth2_registered_client (id, client_id, client_id_issued_at, client_secret, client_name, client_authentication_methods, authorization_grant_types, redirect_uris, scopes, client_settings, token_settings) values
('daf916a3-e56d-4fd3-bfc5-3d09d72f654e', 'itk-client', '2022-02-15 05:09:25.289471', '{noop}secret', 'daf916a3-e56d-4fd3-bfc5-3d09d72f654e', 'client_secret_basic', 'refresh_token,authorization_code', 'http://10.5.0.5:8080/authorized,http://10.5.0.5:8080/login/oauth2/code/itk-client-oidc', 'trust,openid', '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}', '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000]}');