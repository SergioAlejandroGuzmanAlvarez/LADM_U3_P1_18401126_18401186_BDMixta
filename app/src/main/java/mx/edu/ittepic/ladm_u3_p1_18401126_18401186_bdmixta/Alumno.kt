package mx.edu.ittepic.ladm_u3_p1_18401126_18401186_bdmixta

import android.content.ContentValues
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class Alumno (val p: AppCompatActivity) {
    private var ID = 0
    private var nombre =""
    private var escuela=""
    private var telefono=0
    private var carrera1 = ""
    private var carrera2=""
    private var correo=""
    private var fecha =""
    private var hora=""

    private var baseDatos= BaseDatos(p,"TecTepic",null,1)

    fun insertar(nom: EditText, escuela: EditText, telefono: EditText, carrera1:EditText,
                 carrera2:EditText, correo:EditText, fecha:EditText, hora:EditText):Boolean{
        var data = ContentValues()
        data.put("NOMBRE",nom.text.toString())
        data.put("ESCUELA_PROCEDENCIA",escuela.text.toString())
        data.put("TELEFONO",telefono.text.toString().toInt())
        data.put("CARRERA1",carrera1.text.toString())
        data.put("CARRERA2",carrera2.text.toString())
        data.put("CORREO",correo.text.toString())
        data.put("FECHA",fecha.text.toString())
        data.put("HORA",hora.text.toString())

        val resultado = baseDatos.writableDatabase
            .insert("ALUMNO","ID",data)
        if (resultado == -1L) return false
        return true
    }

    fun mostrarTodos():ArrayList<String>{
        var resultado = ArrayList<String>()
        var cursor = baseDatos.readableDatabase
            .rawQuery("SELECT * FROM ALUMNO",null)
        if(!cursor.moveToFirst()){ //Si no se mueve al primero
            resultado.add("NO HAY DATOS A MOSTRAR")
            return resultado
        }
        do {
            var cad = cursor.getString(1)+"\n"+
                    cursor.getString(2)+"\n"+
                    cursor.getInt(3)+"\n"+
                    cursor.getString(4)+"\n"+
                    cursor.getString(5)+"\n"+
                    cursor.getString(6)+"\n"+
                    cursor.getString(7)+"\n"+
                    cursor.getString(8)
            resultado.add(cad)
        }while(cursor.moveToNext())
        return resultado
    }
}