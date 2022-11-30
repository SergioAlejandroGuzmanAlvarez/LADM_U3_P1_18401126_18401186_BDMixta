package mx.edu.ittepic.ladm_u3_p1_18401126_18401186_bdmixta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_insertar.setOnClickListener {
            val resultadoInserccion = Alumno(this).insertar(nombre,escuela,telefono,carrera1,carrera2,correo,fecha,hora)
            if(resultadoInserccion){
                Toast.makeText(this,"SE INSERTO CON EXITO", Toast.LENGTH_LONG).show()
                nombre.setText("")
                escuela.setText("")
                telefono.setText("")
                carrera1.setText("")
                carrera2.setText("")
                correo.setText("")
                fecha.setText("")
                hora.setText("")
                mostrar()
            }else{
                AlertDialog.Builder(this).setMessage("NO SE PUDO INSERTAR").show()
            }
        }
    }
    fun mostrar(){
        var datos = Alumno(this).mostrarTodos()
        listaAlumnos.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datos)
    }
    override fun onRestart() {
        super.onRestart()
        mostrar()
    }

}