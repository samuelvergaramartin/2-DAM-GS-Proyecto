package com.sam170703dev.megustapp.api;

import com.sam170703dev.megustapp.entidades.Cliente;
import com.sam170703dev.megustapp.entidades.Ingrediente;
import com.sam170703dev.megustapp.entidades.Plato;
import com.sam170703dev.megustapp.entidades.Restaurante;
import com.sam170703dev.megustapp.entidades.Usuario;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionCrearCliente;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionCrearIngrediente;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionCrearPlato;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionCrearRestaurante;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionCrearUsuario;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionEliminarImagenPlato;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionTokenCliente;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionTokenRestaurante;
import com.sam170703dev.megustapp.respuestas_http.RespuestaToken;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
public interface API {
    @GET("/usuarios")
    Call<List<Usuario>> getUsuarios(@Header("Authorization") String token);

    @GET("/usuarios/{id}")
    Call<Usuario> getUsuarioById(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @GET("/clientes")
    Call<List<Cliente>> getClientes(@Header("Authorization") String token);

    @GET("/clientes/{id}")
    Call<Cliente> getClienteById(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @GET("/restaurantes")
    Call<List<Restaurante>> getRestaurantes(@Header("Authorization") String token);

    @GET("/restaurantes/{id}")
    Call<Restaurante> getRestauranteById(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @POST("/clientes")
    Call<Cliente> crearCliente(
            @Header("Authorization") String token,
            @Body PeticionCrearCliente cliente
    );

    @POST("/restaurantes")
    Call<Restaurante> crearRestaurante(
            @Header("Authorization") String token,
            @Body PeticionCrearRestaurante restaurante
    );

    @POST("/usuarios")
    Call<Usuario> crearUsuario(
            @Header("Authorization") String token,
            @Body PeticionCrearUsuario usuario
    );
    @POST("/oauth/token/cliente")
    Call<RespuestaToken> getClientToken(@Body PeticionTokenCliente body);

    @POST("/oauth/token/restaurante")
    Call<RespuestaToken> getRestaurantToken(@Body PeticionTokenRestaurante body);

    @Multipart
    @POST("/api/imagenes/clientes")
    Call<ResponseBody> subirImagenCliente(
            @Part MultipartBody.Part imagen
            //@Part("description") RequestBody descripcion
    );

    @Multipart
    @POST("/api/imagenes/platos")
    Call<ResponseBody> subirImagenPlato(
            @Part MultipartBody.Part imagen,
            @Part("description") RequestBody descripcion
    );

    @DELETE("/api/imagenes/platos/{id}")
    Call<ResponseBody> eliminarImagenPlato(
            @Path("id") String id
    );

    @PUT("/clientes/{id}")
    Call<ResponseBody> actualizarCliente(
            @Path("id") int id,
            @Header("Authorization") String token,
            @Body Cliente cliente
    );

    @PUT("/usuarios/{id}")
    Call<ResponseBody> actualizarUsuario(
            @Path("id") int id,
            @Header("Authorization") String token,
            @Body Usuario usuario
    );

    @POST("/platos")
    Call<Plato> crearPlato(
            @Header("Authorization") String token,
            @Body PeticionCrearPlato plato
    );

    @POST("/ingredientes")
    Call<Ingrediente> crearIngrediente(
            @Header("Authorization") String token,
            @Body PeticionCrearIngrediente ingrediente
    );

    @GET("/ingredientes")
    Call<List<Ingrediente>> obtenerIngredientes(
            @Header("Authorization") String token
    );
}
