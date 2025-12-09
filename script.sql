drop database if exists MegustApp;
create database MegustApp;
use MegustApp;

create table clientes (
    id int auto_increment primary key,
    nombre varchar(50) not null,
    ciudad varchar(50) not null,
    telefono varchar(15) not null,
    numero_valoraciones int not null,
    email varchar(50) not null,
    clave varchar(100) not null,
    foto_perfil longblob not null,
    valoracion int
);

create table restaurantes(
    id int auto_increment primary key,
    nombre varchar(50) not null,
    telefono varchar(15) not null,
    ciudad varchar(50) not null,
    calle varchar(100) not null,
    email varchar(50) not null,
    clave varchar(100) not null,
    foto_perfil longblob not null,
    plato int
);

create table valoraciones(
    id int auto_increment primary key,
    nota int not null,
    comentario text null
);

create table platos(
    id int auto_increment primary key,
    nombre varchar(50) not null,
    precio float(3,2) not null,
    imagen longblob not null
);

create table ingredientes(
    id int auto_increment primary key,
    nombre varchar(100) not null
);

alter table clientes add constraint fk_valoracion foreign key (valoracion) references valoraciones(id);
alter table restaurantes add constraint fk_plato foreign key (plato) references platos(id);
