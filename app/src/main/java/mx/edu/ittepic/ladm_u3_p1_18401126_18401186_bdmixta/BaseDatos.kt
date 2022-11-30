package mx.edu.ittepic.ladm_u3_p1_18401126_18401186_bdmixta

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db!!.execSQL("CREATE TABLE ALUMNO (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE VARCHAR(200), ESCUELA_PROCEDENCIA VARCHAR(200), TELEFONO INT,CARRERA1 VARCHAR(200),CARRERA2 VARCHAR(200),CORREO VARCHAR(300),FECHA DATE,HORA VARCHAR(50))")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}