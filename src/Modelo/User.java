package Modelo;

/**
 * Usuarios del Sistema
 *
 * @author Admin
 */
public class User {

    private int id_usuario;
    private String login;
    private String clave;
    private String email;
    private String activo;
    private String rolNombre;
    
    private int rolId;
    private String nombre;
    private String apellido;
    private String tipoDocumento;
    private String numDocumento;
    private String direccion;
    private String telefono;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public User() {
    }

    public int getRolId() {
        return rolId;
    }

    public void setRolId(int rolId) {
        this.rolId = rolId;
    }

    public String getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public String toString() {
        return "User{" + "id_usuario=" + id_usuario + ", rolId=" + rolId + ", nombre=" + nombre + ", apellido=" + apellido + ", rolNombre=" + rolNombre + ", tipoDocumento=" + tipoDocumento + ", numDocumento=" + numDocumento + ", direccion=" + direccion + ", telefono=" + telefono + ", email=" + email + ", clave=" + clave + ", activo=" + activo + '}';
    }

    public String genLogin(String nombre, String apellido) {
        String resultado;
        resultado = nombre.charAt(0) + apellido.substring(1);
        return resultado;

    }

}
