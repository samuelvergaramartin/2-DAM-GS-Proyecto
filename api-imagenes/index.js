import express from 'express';
import multer from 'multer';
import { existsSync, unlinkSync } from 'node:fs';

const servidor = express();
const puerto = 3000;
servidor.use(express.json());
servidor.use('/api/imagenes', express.static('uploads'));

// Configuración de almacenamiento
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        switch (req.url) {
            case '/api/imagenes/clientes': {
                cb(null, 'uploads/clients')
                break;
            }
            case '/api/imagenes/restaurantes': {
                cb(null, 'uploads/restaurants');
                break;
            }
            case '/api/imagenes/platos': {
                cb(null, 'uploads/dishes');
                break;
            }
        }
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

servidor.post('/api/imagenes/clientes', upload.single('imagen'), (req, res) => {
    if (!req.file) {
        return res.status(400).json({ mensaje: 'No se recibió ningún archivo' });
    }
    res.json({ mensaje: 'Imagen subida con éxito', archivo: req.file });
});

servidor.post('/api/imagenes/platos', upload.single('imagen'), (req, res) => {
    if (!req.file) {
        return res.status(400).json({ mensaje: 'No se recibió ningún archivo' });
    }
    res.json({ mensaje: 'Imagen subida con éxito', archivo: req.file });
});

servidor.post("/test", (req, res)=> {
    console.log("Petición recibida.")
    console.log("Cuerpo de la peticion: ", req.body);
    res.json({mensaje: "OK, prueba realizada"});
})

servidor.delete("/api/imagenes/platos/:id", (req, res)=> {
    if(!req.params && !req.params.id) {
        return res.status(400).json({ mensaje: 'Se esperaba el nombre de la imagen a borrar.' });
    }
    const path = `uploads/dishes/${req.params.id}`;
    const existe = existsSync(path);
    if(existe) {
        unlinkSync(path);
        res.status(200).json({mensaje: 'Imagen eliminada correctamente'});
    }
    else res.status(404).json({mensaje: "No existe ninguna imagen con ese nombre."});
})

servidor.listen(puerto, ()=> {
    console.log("Servidor escuchando por el puerto " + puerto);
})