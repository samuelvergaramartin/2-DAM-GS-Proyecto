-- Creación de la base de datos
CREATE DATABASE restaurante_reviews;

-- Tabla Usuario (generalización)
CREATE TABLE Usuario (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    contraseña VARCHAR(100) NOT NULL
);

-- Tabla Cliente (especialización de Usuario)
CREATE TABLE Cliente (
    id INTEGER PRIMARY KEY REFERENCES Usuario(id),
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    ciudad VARCHAR(100),
    numero_reseñas INTEGER DEFAULT 0,
    foto_perfil TEXT
);

-- Tabla Restaurante
CREATE TABLE Restaurante (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    calle VARCHAR(100),
    ciudad VARCHAR(100),
    telefono VARCHAR(20)
);

-- Tabla Plato
CREATE TABLE Plato (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio NUMERIC(6,2) NOT NULL,
    ingredientes TEXT,
    restaurante_id INTEGER NOT NULL REFERENCES Restaurante(id) ON DELETE CASCADE
);

-- Tabla Reseña
CREATE TABLE Reseña (
    id SERIAL PRIMARY KEY,
    nota INTEGER CHECK (nota BETWEEN 1 AND 5),
    comentario TEXT,
    cliente_id INTEGER NOT NULL REFERENCES Cliente(id) ON DELETE CASCADE,
    restaurante_id INTEGER NOT NULL REFERENCES Restaurante(id) ON DELETE CASCADE
);