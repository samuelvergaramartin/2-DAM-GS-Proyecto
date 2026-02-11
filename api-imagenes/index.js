import express from 'express';
import multer from 'multer';
import * as path from "node:path";

const servidor = express();
const puerto = 3000;
servidor.use(express.json());
servidor.use('/api/imagenes', express.static('uploads'));

// Configuración de almacenamiento
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, 'uploads/'); // Directorio donde se guardarán las imágenes
    },
    filename: (req, file, cb) => {
        cb(null, file.originalname); // Usa el nombre original del archivo
    }
});

const upload = multer({ storage: storage });

servidor.get("/api/usuarios", (req, res)=> {
    res.send([{
        id: 1,
        email: "samuelvergaramartin@protonmail.com",
        clave: "123"
    }]).status(200);
})

servidor.get("/api/usuarios/:id", (req, res)=> {
    const id = req.params.id;
    res.send({
        id: 1,
        email: "samuelvergaramartin@protonmail.com",
        clave: "123"
    }).status(200);
})

servidor.get("/api/imagenes", (req, res)=> {
   res.send({
       message: "OK"
   }).status(200);
});

servidor.post('/upload', upload.single('imagen'), (req, res) => {
    if (!req.file) {
        return res.status(400).json({ mensaje: 'No se recibió ningún archivo' });
    }
    res.json({ mensaje: 'Imagen subida con éxito', archivo: req.file });
});

servidor.listen(puerto, ()=> {
    console.log("Servidor escuchando por el puerto " + puerto);
})