package ec.edu.upse.taximetro_app.utiles;

public class ItemDeUsuario {
private int id_login;
private String Usuario;
private String Contrase�a;
public ItemDeUsuario(int id_login, String usuario, String contrase�a) {
	super();
	this.id_login = id_login;
	Usuario = usuario;
	Contrase�a = contrase�a;
}
public int getId_login() {
	return id_login;
}
public void setId_login(int id_login) {
	this.id_login = id_login;
}
public String getUsuario() {
	return Usuario;
}
public void setUsuario(String usuario) {
	Usuario = usuario;
}
public String getContrase�a() {
	return Contrase�a;
}
public void setContrase�a(String contrase�a) {
	Contrase�a = contrase�a;
}

}
