package com.proyectofinal.applicant2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.proto.MaybeDocumentOrBuilder;
import com.proyectofinal.applicant2.dbApplicant.dbNacionalidad;
import com.proyectofinal.applicant2.dbApplicant.dbOficio;
import com.proyectofinal.applicant2.dbApplicant.dbSexo;
import com.proyectofinal.applicant2.dbApplicant.dbSolicitud;
import com.proyectofinal.applicant2.dbApplicant.dbTipoDocumento;
import com.proyectofinal.applicant2.dbApplicant.dbUsuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class ingreso_postulante extends AppCompatActivity {

    private static final String alfanumerico = "^[a-zA-Z_0-9]*$";

    Button ButtonGuardar;

    EditText EditTextNombre;
    EditText EditTextApellido;
    Spinner spinnerTipoDocumento;
    EditText EditTextDocumento;
    EditText EditTextFechaNacimiento;
    Spinner spinnerSexo;
    Spinner spinnerNacionalidad;
    Spinner spinnerOficio;

    EditText EditTextTelefono;
    EditText EditTextDireccion;


    EditText EditTextNaciones;
    private ListView ListViewNaciones;

    public static String MODO = "";
    public static String CORREO = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_postulante);

        Bundle bundle = getIntent().getExtras();


        final String modo = bundle.getString("modo");
        final String correo = bundle.getString("correo");


        ButtonGuardar = findViewById(R.id.ButtonGuardar);

        EditTextNombre = findViewById(R.id.EditTextNombre);
        EditTextApellido = findViewById(R.id.EditTextApellido);
        spinnerTipoDocumento = findViewById(R.id.spinnerTipoDocumento);
        EditTextDocumento = findViewById(R.id.EditTextDocumento);
        EditTextFechaNacimiento = findViewById(R.id.EditTextFechaNacimiento);
        spinnerSexo = findViewById(R.id.spinnerSexo);
        spinnerNacionalidad = findViewById(R.id.spinnerNacionalidad);
        spinnerOficio = findViewById(R.id.spinnerOficio);

        EditTextTelefono = findViewById(R.id.EditTextTelefono);
        EditTextDireccion = findViewById(R.id.EditTextDireccion);


        ButtonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });

        MODO=modo;
        CORREO=correo;

        /*EditTextNaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login_postulante.this, listado_naciones.class));
            }
        });*/
        this.configuracion();
    }

    private void configuracion() {
        /*spinnerOficio = (Spinner) findViewById(R.id.spinnerOficio);
        spinnerNacionalidad = (Spinner) findViewById(R.id.spinnerNacionalidad);
        spinnerTipoDocumento = (Spinner) findViewById(R.id.spinnerTipoDocumento);
        spinnerSexo = (Spinner) findViewById(R.id.spinnerSexo);*/


        this.cargarTipoDocumento();
        this.cargarNacionalidad();
        this.cargarSexo();
        this.cargarOficio();

    }

    private void cargarOficio() {
        final ArrayList<String> listaOficio = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("dbOficio")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayAdapter adaptador = new ArrayAdapter(ingreso_postulante.this,android.R.layout.simple_spinner_dropdown_item,listaOficio);

                                dbOficio oficio = document.toObject(dbOficio.class);

                                listaOficio.add(oficio.getDescripcion());

                                Log.d("add-oficio", "getId "+document.getId());

                                spinnerOficio.setAdapter(adaptador);
                            }

                        } else {
                            Log.d("dbOficio", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void cargarSexo() {
        final ArrayList<String> listaSexo = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("dbSexo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayAdapter adaptador = new ArrayAdapter(ingreso_postulante.this,android.R.layout.simple_spinner_dropdown_item,listaSexo);

                                dbSexo sexo = document.toObject(dbSexo.class);

                                listaSexo.add(sexo.getTipo());

                                Log.d("add-sexo", "getId "+document.getId());

                                spinnerSexo.setAdapter(adaptador);
                            }

                        } else {
                            Log.d("db-sexo", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void cargarNacionalidad() {
        final ArrayList<String> listaNacionalidad = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("dbNacionalidad")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayAdapter adaptador = new ArrayAdapter(ingreso_postulante.this,android.R.layout.simple_spinner_dropdown_item,listaNacionalidad);

                                dbNacionalidad nacionalidad = document.toObject(dbNacionalidad.class);

                                listaNacionalidad.add(nacionalidad.getNombre());

                                Log.d("add-nacionalidad", "getId "+document.getId());

                                spinnerNacionalidad.setAdapter(adaptador);
                            }

                        } else {
                            Log.d("dbNacionalidad", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void cargarTipoDocumento() {
        final ArrayList<String> listaTipoDocumento = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("dbTipoDocumento")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayAdapter adaptador = new ArrayAdapter(ingreso_postulante.this,android.R.layout.simple_spinner_dropdown_item,listaTipoDocumento);

                                dbTipoDocumento tipoDocumento = document.toObject(dbTipoDocumento.class);

                                listaTipoDocumento.add(tipoDocumento.getDescripcion());

                                Log.d("add-tipoDocumento", "getId "+document.getId());

                                spinnerTipoDocumento.setAdapter(adaptador);
                            }

                        } else {
                            Log.d("dbTipoDocumento", "Error getting documents: ", task.getException());
                        }
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
            usuario.put("nombre", obtenerNombre());
            usuario.put("apellido", obtenerApellido());
            usuario.put("tipoDocumento", obtenerTipoDocumento());
            usuario.put("documento", obtenerDocumento());
            usuario.put("fechaNacimiento", obtenerFechaNacimiento());
            usuario.put("sexo", obtenerSexo());
            usuario.put("nacionalidad", obtenerNacionalidad());
            usuario.put("oficio", obtenerOficio());

            // Add a new document with a generated ID
            db.collection("dbUsuario")
                    .add(usuario)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("dbUsuario", "DocumentSnapshot added with ID: " + documentReference.getId());
                            //Toast.makeText(login_postulante.this, "Postulante Registrado", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ingreso_postulante.this, MainActivity.class);

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
            new AlertDialog.Builder(ingreso_postulante.this)
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
        String modo = MODO;
        return modo;
    }
    public String obtenerCorreo(){
        String correo = CORREO;
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
    public String obtenerNombre(){
        String nombre = EditTextNombre.getText().toString();
        return nombre;
    }
    public String obtenerApellido(){
        String apellido = EditTextApellido.getText().toString();
        return apellido;
    }
    public String obtenerTipoDocumento(){
        String tipoDocumento = spinnerTipoDocumento.getSelectedItem().toString();
        return tipoDocumento;
    }
    public String obtenerDocumento(){
        String documento =  EditTextDocumento.getText().toString();
        return documento;
    }
    public String obtenerFechaNacimiento(){
        String fechaNacimiento = EditTextFechaNacimiento.getText().toString();
        return fechaNacimiento;
    }
    public String obtenerSexo(){
        String sexo = spinnerSexo.getSelectedItem().toString();
        return sexo;
    }
    public String obtenerNacionalidad(){
        String nacionalidad = spinnerNacionalidad.getSelectedItem().toString();
        return nacionalidad;
    }
    public String obtenerOficio(){
        String oficio = spinnerOficio.getSelectedItem().toString();
        return oficio;
    }
}

