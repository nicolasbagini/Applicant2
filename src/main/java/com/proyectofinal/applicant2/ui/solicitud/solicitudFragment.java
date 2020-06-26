package com.proyectofinal.applicant2.ui.solicitud;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyectofinal.applicant2.MainActivity;
import com.proyectofinal.applicant2.R;
import com.proyectofinal.applicant2.dbApplicant.dbSolicitud;
import com.proyectofinal.applicant2.solicitud;

public class solicitudFragment extends Fragment {

    Button ButtonSolicitar;
    EditText EditTextEmpleador;
    EditText EditTextOficio;
    EditText EditTextModalidadOficio;

    private solicitudViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = ViewModelProviders.of(this).get(solicitudViewModel.class);
        View root = inflater.inflate(R.layout.fragment_solicitud, container, false);

        ButtonSolicitar = root.findViewById(R.id.ButtonSolicitar);
        EditTextEmpleador = root.findViewById(R.id.EditTextEmpleador);
        EditTextOficio = root.findViewById(R.id.EditTextOficio);
        EditTextModalidadOficio = root.findViewById(R.id.EditTextModalidadOficio);

        ButtonSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizar();
            }
        });

        return root;
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
                            Intent intent = new Intent(getContext(), MainActivity.class);

                            intent.putExtra("id", documentReference.getId());

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
            new AlertDialog.Builder(getContext())
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
