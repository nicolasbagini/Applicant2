package com.proyectofinal.applicant2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyectofinal.applicant2.dbApplicant.dbSolicitud;

public class solicitud extends AppCompatActivity {

    Button ButtonSolicitar;
    EditText EditTextEmpleador;
    EditText EditTextOficio;
    EditText EditTextModalidadOficio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud);

        ButtonSolicitar = findViewById(R.id.ButtonSolicitar);
        EditTextEmpleador = findViewById(R.id.EditTextEmpleador);
        EditTextOficio = findViewById(R.id.EditTextOficio);
        EditTextModalidadOficio = findViewById(R.id.EditTextModalidadOficio);

        ButtonSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizar();
            }
        });

    }

    private void actualizar() {
        if (this.verificarCampos()) {
            // Access a Cloud Firestore instance from your Activity
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            String empleador = obtenerEmpleador();
            String oficio = obtenerOficio();
            String modalidadOficio = obtenerModalidadOficio();
            String estadoSolicitud = obtenerEstadoSolicitud();


            dbSolicitud solicitud = new dbSolicitud(empleador, oficio, modalidadOficio, estadoSolicitud);


                // Add a new document with a generated ID
            db.collection("dbSolicitud")
                    .add(solicitud)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("dbSolicitud", "DocumentSnapshot added with ID: " + documentReference.getId());
                            //Toast.makeText(login_postulante.this, "Postulante Registrado", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(solicitud.this, MainActivity.class);

                            intent.putExtra("id", documentReference.getId()); // yo quiero el modo

                            startActivity(intent);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("dbSolicitud", "Error adding document", e);


                        }
                    });
        } else {
            new AlertDialog.Builder(solicitud.this)
                    .setTitle("Registrar solicitud")
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
    public String obtenerEmpleador(){
        String empleador = EditTextEmpleador.getText().toString();
        return empleador;
    }
    public String obtenerOficio(){
        String oficio = EditTextOficio.getText().toString();
        return oficio;
    }
    public String obtenerModalidadOficio(){
        String modalidadOficio = EditTextModalidadOficio.getText().toString();
        return modalidadOficio;
    }
    public String obtenerEstadoSolicitud(){
        String estadoSolicitud = "activa";
        return estadoSolicitud;
    }
}
