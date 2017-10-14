package net.micrositios.pqrapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	static final String key_entidad = "entidad";
	static final String key_primernombre = "primernombre";
	static final String key_segundonombre = "segundonombre";
	static final String key_primerapellido = "primerapellido";
	static final String key_segundoapellido = "segundoapellido";
	static final String key_email = "email";
	static final String key_tipo_solicitud = "tipo_solicitud";
	static final String key_descripcion = "descripcion";
	static final String key_codigo = "codigo";
	static final String key_webservice = "webservice";
	static final String key_iddato = "_id"; // ID
	static final String key_vulnerable = "vulnerable";
	static final String key_tipo_documento = "tipo_documento";
	static final String key_documento = "documento";
	static final String key_departamento = "departamento";
	static final String key_municipio = "municipio";
	static final String key_direccion = "direccion";
	static final String key_telefono = "telefono";
	static final String key_celular = "celular";
	static final String key_asunto = "asunto";
	static final String key_adjunto = "adjunto";
	static final String key_medio_recepcion = "medio_recepcion";
	static final String key_medio_respuesta = "medio_respuesta";
	static final String key_anonimo = "anonimo";
	static final String key_simcard = "simcard";
	static final String key_imei = "imei";
	static final String key_hecho = "hecho";
	static final String key_nombre_tipo_identificacion = "nombre_tipo_identificacion";
	static final String key_nombre_tipo_solicitud = "nombre_tipo_solicitud";
	static final String key_nombre_vulnerable = "nombre_vulnerable";
	static final String key_nombre_departamento = "nombre_departamento";
	static final String key_nombre_municipio = "nombre_municipio";
	static final String key_codigo_pais = "codigo_pais";
	static final String key_nombre_pais = "nombre_pais";
	static final String key_codigoAsuntoSolicitud = "codigoAsuntoSolicitud";
	static final String key_codigoTipoCanalSolicitud = "codigoTipoCanalSolicitud";
	static final String key_codigoTipoCanalRespuesta = "codigoTipoCanalRespuesta";
	static final String key_codigoEntidad = "codigoEntidad";
	static final String key_siglaEntidad = "siglaEntidad";
	static final String key_numeroTelefonicoDispositivo = "numeroTelefonicoDispositivo";
	static final String key_fecha = "fecha";
	static final String key_fecha_modificacion = "fecha_modificacion";
	static final String key_llave = "llave";
	static final String key_estado = "estado";
	static final String key_respuesta = "respuesta";
	static final String key_respuesta_encuesta = "respuesta_encuesta";

	static final String TAG = "DBAdapter";

	static final String DATABASE_NAME = "PQRappv2";
	static final String DATABASE_TABLE = "solicitudes";
	static final int DATABASE_VERSION = 3;

	static final String DATABASE_CREATE = "CREATE TABLE  solicitudes  (entidad  VARCHAR(100),  primernombre  VARCHAR(100), segundonombre  VARCHAR(100), primerapellido  VARCHAR(100), segundoapellido  VARCHAR(100), email  VARCHAR(100),"
			+ " tipo_solicitud  VARCHAR(100),  descripcion  VARCHAR(100), codigo  VARCHAR(100) UNIQUE, webservice  VARCHAR(100), _id INTEGER PRIMARY KEY AUTOINCREMENT, estado  VARCHAR(100),"
			+ " vulnerable  VARCHAR(100), tipo_documento  VARCHAR(100),  documento  VARCHAR(100),  departamento  VARCHAR(100), municipio  VARCHAR(100),  direccion  VARCHAR(100), telefono  VARCHAR(100), celular  VARCHAR(100),"
			+ " asunto  VARCHAR(100), adjunto  VARCHAR(100), medio_recepcion  VARCHAR(100), medio_respuesta  VARCHAR(100), respuesta  VARCHAR(100), anonimo  VARCHAR(100), simcard  VARCHAR(100), imei  VARCHAR(100), hecho  VARCHAR(100),"
			+ " nombre_tipo_identificacion  VARCHAR(100), nombre_tipo_solicitud  VARCHAR(100), nombre_vulnerable  VARCHAR(100), nombre_departamento  VARCHAR(100), nombre_municipio  VARCHAR(100),"
			+ " codigo_pais  int, nombre_pais  VARCHAR(100), codigoAsuntoSolicitud  int, codigoTipoCanalSolicitud  int, codigoTipoCanalRespuesta  int, codigoEntidad  int,"
			+ " siglaEntidad  VARCHAR(100),  numeroTelefonicoDispositivo  VARCHAR(100), fecha  datetime, fecha_modificacion datetime, llave  VARCHAR(100),respuesta_encuesta VARCHAR(300)) ";

	final Context context;

	DatabaseHelper DBHelper;
	SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(DATABASE_CREATE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS solicitudes");
			onCreate(db);
		}
	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		if (db != null && db.isOpen()) {
			db.close();
			DBHelper.close();
		}
	}

	// ---insert a contact into the database---

	// ---deletes a particular contact---
	public boolean deleteSolicitud(String rowId) {
		return db.delete(DATABASE_TABLE, key_iddato + "=" + rowId, null) > 0;
	}

	// ---retrieves all the solicitudes---
	public Cursor getAllSolicitudes(String orderby) {
		return db.query(DATABASE_TABLE, new String[] { key_entidad,
				key_primernombre, key_segundonombre, key_primerapellido,
				key_segundoapellido, key_email, key_tipo_solicitud,
				key_descripcion, key_codigo, key_webservice, key_iddato,
				key_estado, key_vulnerable, key_tipo_documento, key_documento,
				key_departamento, key_municipio, key_direccion, key_telefono,
				key_celular, key_asunto, key_adjunto, key_medio_recepcion,
				key_medio_respuesta, key_respuesta, key_anonimo, key_simcard,
				key_imei, key_hecho, key_nombre_tipo_identificacion,
				key_nombre_tipo_solicitud, key_nombre_vulnerable,
				key_nombre_departamento, key_nombre_municipio, key_codigo_pais,
				key_nombre_pais, key_codigoAsuntoSolicitud,
				key_codigoTipoCanalSolicitud, key_codigoTipoCanalRespuesta,
				key_codigoEntidad, key_siglaEntidad,
				key_numeroTelefonicoDispositivo, key_fecha,
				key_fecha_modificacion, key_llave }, null, null, null, null,
				orderby);

	}

	// ---retrieves a particular contact---
	public Cursor getSolicitud(String rowCodigo, String rowCodeEntidad)
			throws SQLException {

		String whereClause = key_codigo + " = ? and " + key_codigoEntidad
				+ " = ?";
		String[] whereArgs = { rowCodigo, rowCodeEntidad };

		Cursor mCursor = db.query(DATABASE_TABLE, new String[] { key_entidad,
				key_primernombre, key_segundonombre, key_primerapellido,
				key_segundoapellido, key_email, key_tipo_solicitud,
				key_descripcion, key_codigo, key_webservice, key_iddato,
				key_estado, key_vulnerable, key_tipo_documento, key_documento,
				key_departamento, key_municipio, key_direccion, key_telefono,
				key_celular, key_asunto, key_adjunto, key_medio_recepcion,
				key_medio_respuesta, key_respuesta, key_anonimo, key_simcard,
				key_imei, key_hecho, key_nombre_tipo_identificacion,
				key_nombre_tipo_solicitud, key_nombre_vulnerable,
				key_nombre_departamento, key_nombre_municipio, key_codigo_pais,
				key_nombre_pais, key_codigoAsuntoSolicitud,
				key_codigoTipoCanalSolicitud, key_codigoTipoCanalRespuesta,
				key_codigoEntidad, key_siglaEntidad,
				key_numeroTelefonicoDispositivo, key_fecha,
				key_fecha_modificacion, key_llave, key_respuesta_encuesta, },
				whereClause, whereArgs, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---updates a contact---
	public boolean updateSolicitud(String entidad, String primernombre,
			String segundonombre, String primerapellido,
			String segundoapellido, String email, String tipo_solicitud,
			String descripcion, String codigo, String webservice,
			String estado, String vulnerable, String tipo_documento,
			String documento, String departamento, String municipio,
			String direccion, String telefono, String celular, String asunto,
			String adjunto, String medio_recepcion, String medio_respuesta,
			String respuesta, String anonimo, String simcard, String imei,
			String hecho, String nombre_tipo_identificacion,
			String nombre_tipo_solicitud, String nombre_vulnerable,
			String nombre_departamento, String nombre_municipio,
			String codigo_pais, String nombre_pais,
			String codigoAsuntoSolicitud, String codigoTipoCanalSolicitud,
			String codigoTipoCanalRespuesta, String codigoEntidad,
			String siglaEntidad, String numeroTelefonicoDispositivo,
			String fecha, String fecha_modificacion, String llave) {

		Cursor c = getSolicitud(codigo, codigoEntidad);

		String iddato = c.getString(c.getColumnIndex("_id"));

		c.close();

		ContentValues initialValues = new ContentValues();
		initialValues.put(key_entidad, entidad);
		initialValues.put(key_primernombre, primernombre);
		initialValues.put(key_segundonombre, segundonombre);
		initialValues.put(key_primerapellido, primerapellido);
		initialValues.put(key_segundoapellido, segundoapellido);
		initialValues.put(key_email, email);
		initialValues.put(key_tipo_solicitud, tipo_solicitud);
		initialValues.put(key_descripcion, descripcion);
		initialValues.put(key_codigo, codigo);
		initialValues.put(key_webservice, webservice);
		// initialValues.put(key_iddato, "NULL"); // SE SUPONE QUE LO CREA EN LA
		// TABLA
		initialValues.put(key_estado, estado);
		initialValues.put(key_vulnerable, vulnerable);
		initialValues.put(key_tipo_documento, tipo_documento);
		initialValues.put(key_documento, documento);
		initialValues.put(key_departamento, departamento);
		initialValues.put(key_municipio, municipio);
		initialValues.put(key_direccion, direccion);
		initialValues.put(key_telefono, telefono);
		initialValues.put(key_celular, celular);
		initialValues.put(key_asunto, asunto);
		initialValues.put(key_adjunto, adjunto);
		initialValues.put(key_medio_recepcion, medio_recepcion);
		initialValues.put(key_medio_respuesta, medio_respuesta);
		initialValues.put(key_respuesta, respuesta);
		initialValues.put(key_anonimo, anonimo);
		initialValues.put(key_simcard, simcard);
		initialValues.put(key_imei, imei);
		initialValues.put(key_hecho, hecho);
		initialValues.put(key_nombre_tipo_identificacion,
				nombre_tipo_identificacion);
		initialValues.put(key_nombre_tipo_solicitud, nombre_tipo_solicitud);
		initialValues.put(key_nombre_vulnerable, nombre_vulnerable);
		initialValues.put(key_nombre_departamento, nombre_departamento);
		initialValues.put(key_nombre_municipio, nombre_municipio);
		initialValues.put(key_codigo_pais, codigo_pais);
		initialValues.put(key_nombre_pais, nombre_pais);
		initialValues.put(key_codigoAsuntoSolicitud, codigoAsuntoSolicitud);
		initialValues.put(key_codigoTipoCanalSolicitud,
				codigoTipoCanalSolicitud);
		initialValues.put(key_codigoTipoCanalRespuesta,
				codigoTipoCanalRespuesta);
		initialValues.put(key_codigoEntidad, codigoEntidad);
		initialValues.put(key_siglaEntidad, siglaEntidad);
		initialValues.put(key_numeroTelefonicoDispositivo,
				numeroTelefonicoDispositivo);
		initialValues.put(key_fecha, fecha);
		initialValues.put(key_fecha_modificacion, fecha_modificacion);
		initialValues.put(key_llave, llave);

		Log.d(Propiedades.TAG + " updateSolicitud", initialValues.toString());

		return db.update(DATABASE_TABLE, initialValues, key_iddato + "="
				+ iddato, null) > 0;
	}

	public boolean updateSolicitud(String entidad, String primernombre,
			String segundonombre, String primerapellido,
			String segundoapellido, String email, String tipo_solicitud,
			String descripcion, String codigo, String webservice,
			String estado, String vulnerable, String tipo_documento,
			String documento, String departamento, String municipio,
			String direccion, String telefono, String celular, String asunto,
			String adjunto, String medio_recepcion, String medio_respuesta,
			String respuesta, String anonimo, String simcard, String imei,
			String hecho, String nombre_tipo_identificacion,
			String nombre_tipo_solicitud, String nombre_vulnerable,
			String nombre_departamento, String nombre_municipio,
			String codigo_pais, String nombre_pais,
			String codigoAsuntoSolicitud, String codigoTipoCanalSolicitud,
			String codigoTipoCanalRespuesta, String codigoEntidad,
			String siglaEntidad, String numeroTelefonicoDispositivo,
			String fecha, String fecha_modificacion, String llave,
			String respuest_encuesta) {

		Cursor c = getSolicitud(codigo, codigoEntidad);

		String iddato = c.getString(c.getColumnIndex("_id"));

		c.close();

		ContentValues initialValues = new ContentValues();
		initialValues.put(key_entidad, entidad);
		initialValues.put(key_primernombre, primernombre);
		initialValues.put(key_segundonombre, segundonombre);
		initialValues.put(key_primerapellido, primerapellido);
		initialValues.put(key_segundoapellido, segundoapellido);
		initialValues.put(key_email, email);
		initialValues.put(key_tipo_solicitud, tipo_solicitud);
		initialValues.put(key_descripcion, descripcion);
		initialValues.put(key_codigo, codigo);
		initialValues.put(key_webservice, webservice);
		// initialValues.put(key_iddato, "NULL"); // SE SUPONE QUE LO CREA EN LA
		// TABLA
		initialValues.put(key_estado, estado);
		initialValues.put(key_vulnerable, vulnerable);
		initialValues.put(key_tipo_documento, tipo_documento);
		initialValues.put(key_documento, documento);
		initialValues.put(key_departamento, departamento);
		initialValues.put(key_municipio, municipio);
		initialValues.put(key_direccion, direccion);
		initialValues.put(key_telefono, telefono);
		initialValues.put(key_celular, celular);
		initialValues.put(key_asunto, asunto);
		initialValues.put(key_adjunto, adjunto);
		initialValues.put(key_medio_recepcion, medio_recepcion);
		initialValues.put(key_medio_respuesta, medio_respuesta);
		initialValues.put(key_respuesta, respuesta);
		initialValues.put(key_anonimo, anonimo);
		initialValues.put(key_simcard, simcard);
		initialValues.put(key_imei, imei);
		initialValues.put(key_hecho, hecho);
		initialValues.put(key_nombre_tipo_identificacion,
				nombre_tipo_identificacion);
		initialValues.put(key_nombre_tipo_solicitud, nombre_tipo_solicitud);
		initialValues.put(key_nombre_vulnerable, nombre_vulnerable);
		initialValues.put(key_nombre_departamento, nombre_departamento);
		initialValues.put(key_nombre_municipio, nombre_municipio);
		initialValues.put(key_codigo_pais, codigo_pais);
		initialValues.put(key_nombre_pais, nombre_pais);
		initialValues.put(key_codigoAsuntoSolicitud, codigoAsuntoSolicitud);
		initialValues.put(key_codigoTipoCanalSolicitud,
				codigoTipoCanalSolicitud);
		initialValues.put(key_codigoTipoCanalRespuesta,
				codigoTipoCanalRespuesta);
		initialValues.put(key_codigoEntidad, codigoEntidad);
		initialValues.put(key_siglaEntidad, siglaEntidad);
		initialValues.put(key_numeroTelefonicoDispositivo,
				numeroTelefonicoDispositivo);
		initialValues.put(key_fecha, fecha);
		initialValues.put(key_fecha_modificacion, fecha_modificacion);
		initialValues.put(key_llave, llave);
		initialValues.put(key_respuesta_encuesta, respuest_encuesta);

		Log.d(Propiedades.TAG + " updateSolicitud", initialValues.toString());

		return db.update(DATABASE_TABLE, initialValues, key_iddato + "="
				+ iddato, null) > 0;
	}

	public long insertSolicitud(String entidad, String primernombre,
			String segundonombre, String primerapellido,
			String segundoapellido, String email, String tipo_solicitud,
			String descripcion, String codigo, String webservice,
			String iddato, String estado, String vulnerable,
			String tipo_documento, String documento, String departamento,
			String municipio, String direccion, String telefono,
			String celular, String asunto, String adjunto,
			String medio_recepcion, String medio_respuesta, String respuesta,
			String anonimo, String simcard, String imei, String hecho,
			String nombre_tipo_identificacion, String nombre_tipo_solicitud,
			String nombre_vulnerable, String nombre_departamento,
			String nombre_municipio, String codigo_pais, String nombre_pais,
			String codigoAsuntoSolicitud, String codigoTipoCanalSolicitud,
			String codigoTipoCanalRespuesta, String codigoEntidad,
			String siglaEntidad, String numeroTelefonicoDispositivo,
			String fecha, String fecha_modificacion, String llave) {

		ContentValues initialValues = new ContentValues();
		initialValues.put(key_entidad, entidad);
		initialValues.put(key_primernombre, primernombre);
		initialValues.put(key_segundonombre, segundonombre);
		initialValues.put(key_primerapellido, primerapellido);
		initialValues.put(key_segundoapellido, segundoapellido);
		initialValues.put(key_email, email);
		initialValues.put(key_tipo_solicitud, tipo_solicitud);
		initialValues.put(key_descripcion, descripcion);
		initialValues.put(key_codigo, codigo);
		initialValues.put(key_webservice, webservice);
		// initialValues.put(key_iddato, "NULL"); // SE SUPONE QUE LO CREA EN LA
		// TABLA
		initialValues.put(key_estado, estado);
		initialValues.put(key_vulnerable, vulnerable);
		initialValues.put(key_tipo_documento, tipo_documento);
		initialValues.put(key_documento, documento);
		initialValues.put(key_departamento, departamento);
		initialValues.put(key_municipio, municipio);
		initialValues.put(key_direccion, direccion);
		initialValues.put(key_telefono, telefono);
		initialValues.put(key_celular, celular);
		initialValues.put(key_asunto, asunto);
		initialValues.put(key_adjunto, adjunto);
		initialValues.put(key_medio_recepcion, medio_recepcion);
		initialValues.put(key_medio_respuesta, medio_respuesta);
		initialValues.put(key_respuesta, respuesta);
		initialValues.put(key_anonimo, anonimo);
		initialValues.put(key_simcard, simcard);
		initialValues.put(key_imei, imei);
		initialValues.put(key_hecho, hecho);
		initialValues.put(key_nombre_tipo_identificacion,
				nombre_tipo_identificacion);
		initialValues.put(key_nombre_tipo_solicitud, nombre_tipo_solicitud);
		initialValues.put(key_nombre_vulnerable, nombre_vulnerable);
		initialValues.put(key_nombre_departamento, nombre_departamento);
		initialValues.put(key_nombre_municipio, nombre_municipio);
		initialValues.put(key_codigo_pais, codigo_pais);
		initialValues.put(key_nombre_pais, nombre_pais);
		initialValues.put(key_codigoAsuntoSolicitud, codigoAsuntoSolicitud);
		initialValues.put(key_codigoTipoCanalSolicitud,
				codigoTipoCanalSolicitud);
		initialValues.put(key_codigoTipoCanalRespuesta,
				codigoTipoCanalRespuesta);
		initialValues.put(key_codigoEntidad, codigoEntidad);
		initialValues.put(key_siglaEntidad, siglaEntidad);
		initialValues.put(key_numeroTelefonicoDispositivo,
				numeroTelefonicoDispositivo);
		initialValues.put(key_fecha, fecha);
		initialValues.put(key_fecha_modificacion, fecha_modificacion);
		initialValues.put(key_llave, llave);
		initialValues.put(key_respuesta_encuesta, "");

		Log.d(Propiedades.TAG + " insertSolicitud", initialValues.toString());
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	public String getrespuesta(String codigo) {
		String res = "";
		try {
			Log.i("PQR", "codigo" + codigo + " entidad"
					+ Propiedades.codigo_entidad);

			Cursor c = this.getSolicitud(codigo, Propiedades.codigo_entidad);
			res = c.getString(c.getColumnIndex("respuesta_encuesta"));
			c.close();

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("pqr", "error obteniendo respuesta" + e.toString());

		}

		return res;
	}

	public boolean updateSolicitud(String codigo, String respuest_encuesta) {

		try {

			Cursor c = getSolicitud(codigo, Propiedades.codigo_entidad);

			String iddato = c.getString(c.getColumnIndex("_id"));

			c.close();

			ContentValues initialValues = new ContentValues();

			// initialValues.put(key_iddato, "NULL"); // SE SUPONE QUE LO CREA
			// EN LA
			// TABLA
			initialValues.put(key_respuesta_encuesta, respuest_encuesta);

			Log.d(Propiedades.TAG + " updateSolicitud",
					initialValues.toString());

			int v = db.update(DATABASE_TABLE, initialValues, key_iddato + "="
					+ iddato, null);

			return v > 0;

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("pqr", "error actualizacion" + e.toString());

		}
		return false;
	}

	public boolean updateSolicitud(String descripcion, String codigo,
			String estado, String nombre_tipo_solicitud,
			String codigoTipoCanalSolicitud, String fecha,
			String fecha_modificacion, String asunto, String respuesta,
			String adjunto) {

		Cursor c = getSolicitud(codigo, Propiedades.codigo_entidad);

		String iddato = c.getString(c.getColumnIndex("_id"));

		c.close();

		ContentValues initialValues = new ContentValues();
		initialValues.put(key_descripcion, descripcion);
		initialValues.put(key_codigo, codigo);
		// initialValues.put(key_iddato, "NULL"); // SE SUPONE QUE LO CREA EN LA
		// TABLA
		initialValues.put(key_estado, estado);
		initialValues.put(key_nombre_tipo_solicitud, nombre_tipo_solicitud);
		initialValues.put(key_codigoTipoCanalSolicitud,
				codigoTipoCanalSolicitud);
		initialValues.put(key_fecha, fecha);
		initialValues.put(key_fecha_modificacion, fecha_modificacion);
		initialValues.put(key_adjunto, adjunto);
		initialValues.put(key_asunto, asunto);
		initialValues.put(key_respuesta, respuesta);
		Log.d(Propiedades.TAG + " updateSolicitud", initialValues.toString());

		Log.d("MICROSITIOS_ERROR", descripcion + " " + codigo + " " + estado
				+ " " + nombre_tipo_solicitud + " " + codigoTipoCanalSolicitud
				+ " " + fecha + " " + fecha_modificacion + " " + iddato + " "
				+ adjunto);

		int v = db.update(DATABASE_TABLE, initialValues, key_iddato + "="
				+ iddato, null);

		Log.d("MICROSITIOS_ERROR", descripcion + " " + codigo + " " + estado
				+ " " + nombre_tipo_solicitud + " " + codigoTipoCanalSolicitud
				+ " " + fecha + " " + fecha_modificacion + " " + iddato + "-"
				+ v);

		return v > 0;

	}
}
