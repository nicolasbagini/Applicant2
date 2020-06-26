package com.proyectofinal.applicant2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.proyectofinal.applicant2.dbApplicant.dbUsuario;

public class login_inicio extends AppCompatActivity {

    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    public static final String USUARIO = "";

    private SharedPreferences preferences;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_inicio);

        //Initializing Views
        signInButton = findViewById(R.id.buttonIngresar);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.

            // si es la primera vez(verificar en firebase)
            this.inicio();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(login_inicio.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.

        // si ya hice el login en el telefono
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            startActivity(new Intent(login_inicio.this, MainActivity.class));
        }

        super.onStart();
    }


    private void inicio(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(login_inicio.this);
        String googleMail = acct.getEmail();

        db.collection("dbUsuario").whereEqualTo("correo", googleMail).limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("dbUsuario", document.getId() + " => " + document.getData());
                                dbUsuario usuario = document.toObject(dbUsuario.class);

                                Intent intent = new Intent(login_inicio.this, MainActivity.class);

                                intent.putExtra("modo", usuario.getModo()); // Postulante o Empleador
                                intent.putExtra("correo", usuario.getCorreo()); // correo
                                intent.putExtra("oficio",usuario.getOficio()); // oficio

                                // MY_PREFS_NAME - a static String variable like:
                                SharedPreferences preferencias = getSharedPreferences(USUARIO, Context.MODE_PRIVATE);
                                String modo = usuario.getModo();
                                String correo = usuario.getCorreo();
                                String oficio = usuario.getOficio();


                                SharedPreferences.Editor editor = preferencias.edit();
                                editor.putString("modo",modo);
                                editor.putString("correo",correo);
                                editor.putString("oficio",oficio);
                                editor.commit();


                                startActivity(intent);
                            }
                        } else {
                            Log.d("dbUsuario", "Error getting documents: ", task.getException());
                        }
                    }
                });

        startActivity(new Intent(login_inicio.this, login_modo.class));
    }

}

