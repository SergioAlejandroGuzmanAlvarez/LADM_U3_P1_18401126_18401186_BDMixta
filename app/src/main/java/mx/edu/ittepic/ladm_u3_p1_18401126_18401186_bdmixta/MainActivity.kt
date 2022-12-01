package mx.edu.ittepic.ladm_u3_p1_18401126_18401186_bdmixta

import android.content.ContentValues
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var baseDatos = BaseDatos(this,"ALUMNOSTEC", null, 1)
    var listaID = ArrayList<String>()
    var baseRemota = FirebaseFirestore.getInstance()
    var idContador=0
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.opcionMenu1->{
                FirebaseFirestore.getInstance()
                    .collection("ALUMNOS")
                    .document(idCampo.text.toString())
                    .update("NOMBRE",nombre.text.toString(),
                        "ESCUELA_PROCEDENCIA",escuela.text.toString(),
                        "TELEFONO",telefono.text.toString(),
                        "CARRERA1",carrera1.text.toString(),
                        "CARRERA2",carrera2.text.toString(),
                        "CORREO", correo.text.toString(),
                        "FECHA", fecha.text.toString(),
                        "HORA", hora.text.toString()
                    )
                    .addOnSuccessListener {
                        toas("SE ACTUALIZO CON ÉXITO.")
                        limpiarCampos()
                    }
                    .addOnFailureListener {
                        alert(it.message!!)
                    }
            }
            R.id.opcionMenu2->{
                FirebaseFirestore.getInstance()
                    .collection("ALUMNOS")
                    .document(idCampo.text.toString())
                    .delete()
                    .addOnSuccessListener {
                        toas("SE BORRO EN LA BASE DATOS!!")
                        eliminar(idCampo.text.toString())
                    }
                    .addOnFailureListener {
                        alert(it.message!!)
                    }
            }
            R.id.opcionMenu3->{
                finish()
            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_insertar.setOnClickListener {
            insertarAlumno()
        }
        btn_consultar.setOnClickListener {
            consultaFirebase()
        }
        recargarLista()
    }

    fun insertarAlumno() : Boolean {
        try {
            var transaccion = baseDatos.writableDatabase
            var data = ContentValues()
            data.put("ID",idContador++)
            data.put("NOMBRE",nombre.text.toString())
            data.put("ESCUELA_PROCEDENCIA",escuela.text.toString())
            data.put("TELEFONO",telefono.text.toString())
            data.put("CARRERA1",carrera1.text.toString())
            data.put("CARRERA2",carrera2.text.toString())
            data.put("CORREO",correo.text.toString())
            data.put("FECHA",fecha.text.toString())
            data.put("HORA",hora.text.toString())
            var respuesta = transaccion.insert("ALUMNO", null, data)
            if(respuesta == -1L){
                return false
                Toast.makeText(this,"ERROR! No se pudo insertar el dato.",Toast.LENGTH_LONG)
            }else{
                Toast.makeText(this,"Se inserto con éxito.",Toast.LENGTH_LONG)
                limpiarCampos()
                recargarLista()
            }
            transaccion.close()
        } catch (err: SQLiteException){
            AlertDialog.Builder(this)
                .setTitle("ATENCION!")
                .setMessage(err.message)
                .show()
        }
        return true
    }
    fun mensaje(m:String){
        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage(m)
            .setPositiveButton("OK"){d,i->}
            .show()
    }
    fun recargarLista(){
        try {
            var transaccion = baseDatos.readableDatabase
            var alumno = ArrayList<String>()
            var cursor = transaccion.query("ALUMNO", arrayOf("*"), null, null, null,null,null)
            if(cursor.moveToFirst()){
                listaID.clear()
                do{
                    var data = "[" + cursor.getInt(0)+"] - "+cursor.getString(1)
                    alumno.add(data)
                    listaID.add(cursor.getInt(0).toString())
                } while (cursor.moveToNext())

            }else {
                alumno.add("NO HAY DATOS CAPTURADOS")
            }

            listaAlumnos.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alumno)

            listaAlumnos.setOnItemClickListener { adapterView, view, posicionItemSeleccionado, id ->
                var idABorrar = listaID.get(posicionItemSeleccionado)
                AlertDialog.Builder(this)
                    .setMessage("¿Estas seguro que deseas borrar ID:"+idABorrar+"?")
                    .setTitle("ATENCION")
                    .setPositiveButton("SI"){d,i->
                        eliminar(idABorrar)
                    }
                    .setNegativeButton("NO"){d,i->
                        d.dismiss()
                    }
                    .show()
            }

            transaccion.close()
        } catch (err: SQLiteException) {
            mensaje(err.message!!)
        }
    }
    fun eliminar(idABorrar:String){
        try {
            var transaccion = baseDatos.writableDatabase
            var resultado = transaccion.delete("ALUMNO", "ID=?", arrayOf(idABorrar))
            if(resultado==0){
                mensaje("NO SE ENCONTRO EL ID" +idABorrar+ "\nNO SE PUDO ELIMINAR")
            } else {
                mensaje("EXITO ID ${idABorrar} SE ELIMINO CORRECTAMENTE")
            }
            transaccion.close()
            recargarLista()
        } catch(err:SQLiteException){
            mensaje(err.message!!)
        }
    }
    fun limpiarCampos(){
        nombre.setText("")
        escuela.setText("")
        telefono.setText("")
        carrera1.setText("")
        carrera2.setText("")
        correo.setText("")
        fecha.setText("")
        hora.setText("")
        idCampo.setText("")
    }
    fun consultaFirebase(){
        try{
            var transaccion = baseDatos.readableDatabase
            var idBuscar = idCampo.text.toString()
            var cursor = transaccion.query("ALUMNO", arrayOf("NOMBRE","ESCUELA_PROCEDENCIA","TELEFONO","CARRERA1","CARRERA2","CORREO","FECHA","HORA"),"ID=?", arrayOf(idBuscar),null,null,null)
            if(cursor.moveToFirst()){
                resultadoConsulta.setText(
                    "NOMBRE: ${cursor.getString(1)}," + "TELEFONO ${cursor.getString(3)}"
                )
                var datosInsertar = hashMapOf(
                    "NOMBRE" to cursor.getString(1),
                    "ESCUELA_PROCEDENCIA" to cursor.getString(2),
                    "CARRERA1" to cursor.getString(3),
                    "CARRERA2" to cursor.getString(4),
                    "CORREO" to cursor.getString(5),
                    "FECHA" to Date()
                )
                baseRemota.collection("ALUMNOS")
                    .add(datosInsertar)
                    mensaje("Se inserto en la Firestore de Firebase")
            }else{
                mensaje("ERORR. No hay resultados que capturar.")
            }
            transaccion.close()
            limpiarCampos()
        }catch(err:SQLiteException){
            mensaje(err.message!!)
        }
    }
    fun toas(m:String){
        Toast.makeText(this,m,Toast.LENGTH_LONG).show()
    }

    fun alert(m:String){
        AlertDialog.Builder(this).setTitle("ATENCION")
            .setMessage(m)
            .setPositiveButton("OK"){d,i->}
            .show()
    }
}