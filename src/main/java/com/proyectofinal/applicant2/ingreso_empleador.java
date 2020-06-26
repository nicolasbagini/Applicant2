package com.proyectofinal.applicant2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyectofinal.applicant2.dbApplicant.dbUsuario;

import java.util.HashMap;
import java.util.Map;

public class ingreso_empleador extends AppCompatActivity {

    Button ButtonGuardar;

    EditText EditTextRazonSocial;
    EditText EditTextCuit;
    EditText EditTextFechaInicio;

    EditText EditTextTelefono;
    EditText EditTextDireccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_empleador);

        Bundle bundle = getIntent().getExtras();

        final String modo = bundle.getString("modo");
        final String correo = bundle.getString("correo");


        ButtonGuardar = findViewById(R.id.ButtonGuardar);

        EditTextRazonSocial = findViewById(R.id.EditTextRazonSocial);
        EditTextCuit = findViewById(R.id.EditTextCuit);
        EditTextFechaInicio = findViewById(R.id.EditTextFechaInicio);

        EditTextTelefono = findViewById(R.id.EditTextTelefono);
        EditTextDireccion = findViewById(R.id.EditTextDireccion);

        ButtonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });
    }

    private void guardar() {
        if (this.verificarCampos()) {
            // Access a Cloud Firestore instance from your Activity
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> usuario = new HashMap<>();
            usuario.put("modo", obtenerModo());
            usuario.put("correo", obtenerCorreo());
            usuario.put("telefono", obtenerTelefono());
            usuario.put("direccion", obtenerDireccion());
            usuario.put("razonSocil", obtenerRazonSocial());
            usuario.put("cuit", obtenerCuit());
            usuario.put("fechaInicio", obtenerFechaInicio());

            // Add a new document with a generated ID
            db.collection("dbUsuario")
                    .add(usuario)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("dbUsuario", "DocumentSnapshot added with ID: " + documentReference.getId());
                            //Toast.makeText(login_postulante.this, "Postulante Registrado", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ingreso_empleador.this, MainActivity.class);

                            intent.putExtra("id", documentReference.getId());

                            startActivity(intent);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("dbUsuario", "Error adding document", e);
                        }
                    });
        } else {
            new AlertDialog.Builder(ingreso_empleador.this)
                    .setTitle("Registrar postulante")
                    .setMessage("Verificar los campos a completar.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }


    }
    public boolean verificarCampos(){
        boolean ok=false;
        ok=true;
        return ok;
    }
    public String obtenerModo(){
        String modo = "empleador";
        return modo;
    }
    public String obtenerCorreo(){
        String correo = "nicolasbagini@gmail.com";
        return correo;
    }
    public String obtenerTelefono(){
        String telefono = EditTextTelefono.getText().toString();
        return telefono;
    }
    public String obtenerDireccion(){
        String direccion = EditTextDireccion.getText().toString();
        return direccion;
    }
    public String obtenerRazonSocial(){
        String razonSocial = EditTextRazonSocial.getText().toString();
        return razonSocial;
    }
    public String obtenerCuit(){
        String cuit =  EditTextCuit.getText().toString();
        return cuit;
    }
    public String obtenerFechaInicio(){
        String fechaInicio = EditTextFechaInicio.getText().toString();
        return fechaInicio;
    }
}
