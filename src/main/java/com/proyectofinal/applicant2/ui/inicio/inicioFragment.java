package com.proyectofinal.applicant2.ui.inicio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.proyectofinal.applicant2.R;
import com.proyectofinal.applicant2.dbApplicant.dbOficio;
import com.proyectofinal.applicant2.dbApplicant.dbSolicitud;
import com.proyectofinal.applicant2.solicitud;

import java.util.ArrayList;

public class inicioFragment extends Fragment {

    ListView ListViewSolicitudes;
    Button ButtonBuscar;
    Spinner spinnerBuscarOficio;

    private inicioViewModel homeViewModel;


    public static final String USUARIO = "miUsuario";


    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(inicioViewModel.class);

        View root = inflater.inflate(R.layout.fragment_inicio, container, false);

        Context context = getActivity();

        ListViewSolicitudes = (ListView) root.findViewById(R.id.ListViewSolicitudes);
        ButtonBuscar = root.findViewById(R.id.ButtonBuscar);
        spinnerBuscarOficio = root.findViewById(R.id.spinnerBuscarOficio);


        ButtonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscar();
            }
        });

        /*ListViewSolicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), solicitud.class));
            }
        });*/

        this.configuracion();

        return root;
    }


    private void configuracion(){
        this.cargarOficio();

        final ArrayList<String> listaSolicitud = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //SharedPreferences prefs = context.getSharedPreferences(MI_OFICIO, context.MODE_PRIVATE);

       /* SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        //int defaultValue = getResources().getInteger(R.integer.saved_high_score_default_key);

        String miOficio = prefs.getString("miOficio", "wachu");*/


        SharedPreferences preferencias = getActivity().getSharedPreferences(USUARIO, Context.MODE_PRIVATE);
        String miUsuario = preferencias.getString("oficio", "tecnico");

        Toast.makeText(getContext(), miUsuario, Toast.LENGTH_LONG).show();


        db.collection("dbSolicitud").whereEqualTo("id_oficio", miUsuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayAdapter adaptador = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,listaSolicitud);

                                dbSolicitud solicitud = document.toObject(dbSolicitud.class);

                                listaSolicitud.add(solicitud.getId_empleador()+": "+solicitud.getId_oficio()+" - "+solicitud.getId_modalidadOficio());

                                Log.d("add-solicitud", "getId "+document.getId());

                                ListViewSolicitudes.setAdapter(adaptador);
                            }

                        } else {
                            Log.d("dbSolicitud", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void buscar(){
        final ArrayList<String> listaSolicitud = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String busqueda = spinnerBuscarOficio.getSelectedItem().toString();
        listaSolicitud.clear();

        /*db.collection("dbSolicitud").whereEqualTo("id_oficio", busqueda)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayAdapter adaptador = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,listaSolicitud);

                                dbSolicitud solicitud = document.toObject(dbSolicitud.class);

                                listaSolicitud.add(solicitud.getId_empleador()+": "+solicitud.getId_oficio()+" - "+solicitud.getId_modalidadOficio());

                                Log.d("add-solicitud", "getId "+document.getId());

                                ListViewSolicitudes.setAdapter(adaptador);
                            }

                        } else {
                            Log.d("dbSolicitud", "Error getting documents: ", task.getException());
                        }
                    }
                });*/
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
                                ArrayAdapter adaptador = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,listaOficio);

                                dbOficio oficio = document.toObject(dbOficio.class);

                                listaOficio.add(oficio.getDescripcion());

                                Log.d("add-oficio", "getId "+document.getId());

                                spinnerBuscarOficio.setAdapter(adaptador);
                            }

                        } else {
                            Log.d("dbOficio", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
