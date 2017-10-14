package net.micrositios.pqrapp.formulario;

public class Campo {
	int id = 0;

	String tipo_de_campo = "";
	String nombre = "";
	int tamano = 0;
	String tipo = "";
	boolean obligatorio = false;
	String nombre_parametro = "";
	String id_parametro = "";
	String id_solicitud = "";
	String consulta = "";
	int id_padre = 0;
	String msg_error = "";
	int tamano_minimo = 0;
	int numero_hijos = 0;
	boolean guardar = false;
	String seccion = "";

	public Campo(int id, String tipo_de_campo, String nombre, int tamano,
			String tipo, boolean obligatorio, String nombre_parametro,
			String id_parametro, String id_solicitud, String consulta,
			int id_padre, String msg_error, int tamano_minimo,
			int numero_hijos, boolean guardar, String seccion) {
		super();
		this.id = id;
		this.tipo_de_campo = tipo_de_campo;
		this.nombre = nombre;
		this.tamano = tamano;
		this.tipo = tipo;
		this.obligatorio = obligatorio;
		this.nombre_parametro = nombre_parametro;
		this.id_parametro = id_parametro;
		this.id_solicitud = id_solicitud;
		this.consulta = consulta;
		this.id_padre = id_padre;
		this.msg_error = msg_error;
		this.tamano_minimo = tamano_minimo;
		this.numero_hijos = numero_hijos;
		this.guardar = guardar;
		this.seccion = seccion;
	}

	public String getSeccion() {
		return seccion;
	}

	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}

	public boolean isGuardar() {
		return guardar;
	}

	public void setGuardar(boolean guardar) {
		this.guardar = guardar;
	}

	public String getId_parametro() {
		return id_parametro;
	}

	public void setId_parametro(String id_parametro) {
		this.id_parametro = id_parametro;
	}

	public int getNumero_hijos() {
		return numero_hijos;
	}

	public void setNumero_hijos(int numero_hijos) {
		this.numero_hijos = numero_hijos;
	}

	public int getTamano_minimo() {
		return tamano_minimo;
	}

	public void setTamano_minimo(int tamano_minimo) {
		this.tamano_minimo = tamano_minimo;
	}

	public String getMsg_error() {
		return msg_error;
	}

	public void setMsg_error(String msg_error) {
		this.msg_error = msg_error;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTipo_de_campo() {
		return tipo_de_campo;
	}

	public void setTipo_de_campo(String tipo_de_campo) {
		this.tipo_de_campo = tipo_de_campo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getTamano() {
		return tamano;
	}

	public void setTamano(int tamano) {
		this.tamano = tamano;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean isObligatorio() {
		return obligatorio;
	}

	public void setObligatorio(boolean obligatorio) {
		this.obligatorio = obligatorio;
	}

	public String getNombre_parametro() {
		return nombre_parametro;
	}

	public void setNombre_parametro(String nombre_parametro) {
		this.nombre_parametro = nombre_parametro;
	}

	public String getId_solicitud() {
		return id_solicitud;
	}

	public void setId_solicitud(String id_solicitud) {
		this.id_solicitud = id_solicitud;
	}

	public String getConsulta() {
		return consulta;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}

	public int getId_padre() {
		return id_padre;
	}

	public void setId_padre(int id_padre) {
		this.id_padre = id_padre;
	}

}
