-- Creación de la base de datos

drop database if exists MegustApp;
create database MegustApp;
use MegustApp;

-- Creación de tablas y claves foráneas

CREATE TABLE usuarios (
    id int auto_increment PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    clave VARCHAR(100) NOT NULL
);

create table clientes (
	id int auto_increment primary key,
    nombre varchar(50) not null,
    ciudad varchar(50) not null,
    telefono varchar(15) unique not null,
    numero_valoraciones int not null,
    email varchar(50) unique not null,
    clave varchar(100) not null,
    ruta_foto_perfil varchar(2048) not null
);

create table restaurantes(
    id int auto_increment primary key,
    nombre varchar(50) not null,
    telefono varchar(15) not null,
    ciudad varchar(50) not null,
    calle varchar(100) not null,
    email varchar(50) unique not null,
    clave varchar(100) not null,
    ruta_foto_perfil varchar(2048) not null
);

create table valoraciones (
    id int auto_increment primary key,
    nota int not null check (nota >= 1 and nota <= 5),
    comentario text null,
    cliente int not null,
    restaurante int not null
);

create table platos (
    id int auto_increment primary key,
    nombre varchar(50) not null,
    precio float not null,
    ruta_imagen varchar(2048) not null,
    restaurante int not null
);

create table ingredientes (
    id int auto_increment primary key,
    nombre varchar(100) not null,
    es_alergeno boolean not null default false
);

create table plato_ingrediente (
    id_plato int not null,
    id_ingrediente int not null,
    primary key(id_plato, id_ingrediente)
);

alter table platos add constraint fk_restaurante foreign key (restaurante) references restaurantes(id);
alter table valoraciones add constraint fk_cliente foreign key (cliente) references clientes(id);
alter table valoraciones add constraint fk_restaurante foreign key (restaurante) references restaurantes(id);
alter table plato_ingrediente add constraint fk_pi_plato foreign key (id_plato) references platos(id);
alter table plato_ingrediente add constraint fk_pi_ingrediente foreign key(id_ingrediente) references ingredientes(id);

-- Insercción de datos

insert into usuarios (email, clave) values ("samuelvergaramartin@protonmail.com", "A123456a");
insert into clientes (nombre, ciudad, telefono, numero_valoraciones, email, clave, ruta_foto_perfil) values (
    "Samuel Vergara Martín",
    "Málaga",
    "+34722688078",
    0,
    "samuelvergaramartin@protonmail.com",
    "A123456a",
    "C://Users/anony/image.png"
);
insert into restaurantes (nombre, telefono, ciudad, calle, email, clave, ruta_foto_perfil) values (
    "Restaurante 1",
    "+34722688079",
    "Málaga",
    "Sillita de la reina",
    "restaurante1@gmail.com",
    "A123456a",
    "C://Users/restaurante1/image.png"
);
insert into valoraciones (nota, comentario, cliente, restaurante) values (
    5,
    "Muy buen restaurante, me ha encantado",
    1,
    1
);
insert into platos (nombre, precio, ruta_imagen, restaurante) values (
    "Macarrones con queso",
    20,
    "C://Users/restaurante1/platos/1/imagen.png",
    1
);
insert into ingredientes (nombre, es_alergeno) values (
    "Tomate",
    false
);
insert into ingredientes (nombre, es_alergeno) values (
    "Queso",
    true
);
insert into ingredientes (nombre, es_alergeno) values (
    "Macarrones",
    true
);
insert into plato_ingrediente values (1, 1);
insert into plato_ingrediente values (1, 2);
insert into plato_ingrediente values (1, 3);