package ma.ensa.volley;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.volley.beans.Filiere;

public class StudentAdd extends AppCompatActivity {


    ///////////partie spinner
    private Spinner spinner;
    private List<Filiere> filieresList = new ArrayList<>();
    private RequestQueue requestQueue;
    private String filieresUrl = "http://10.0.2.2:8088/api/v1/filieres";

    /////////
    Button bnRetourStudent, bnAddStudent, bnVoirStudents;
    EditText firstname,lastname,login,telephone,password;

    String postUrl = "http://10.0.2.2:8088/api/v1/students";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_add);

        ////partie spinner
        spinner = findViewById(R.id.spinnerfiliereTextStudent);

        requestQueue = Volley.newRequestQueue(this);
        loadFilieres();
        ////////////


        bnRetourStudent = findViewById(R.id.bnRetourStudent);
        firstname = findViewById(R.id.firstnameeditTextStudent);
        lastname = findViewById(R.id.lastnameeditTextStudent);
        login = findViewById(R.id.logineditTextStudent);
        telephone = findViewById(R.id.telephoneeditTextStudent);
        spinner = findViewById(R.id.spinnerfiliereTextStudent);
        password = findViewById(R.id.passwordeditTextStudent);

        bnAddStudent = findViewById(R.id.bnAddStudent);
        bnRetourStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentAdd.this, MainActivity.class);
                startActivity(intent);
            }
        });

        bnVoirStudents = findViewById(R.id.bnVoirStudents);
        bnVoirStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(StudentAdd.this, StudentVoir.class);
               startActivity(intent);
            }
        });

        bnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonbody = new JSONObject();
                    jsonbody.put("firstname", firstname.getText().toString());
                    jsonbody.put("lastname", lastname.getText().toString());
                    jsonbody.put("login", login.getText().toString());
                    jsonbody.put("telephone", telephone.getText().toString());
                    jsonbody.put("password", password.getText().toString());

                    // Obtenez l'objet Filiere sélectionné depuis le Spinner
                    Filiere selectedFiliere = (Filiere) spinner.getSelectedItem();

                    // Créez un objet JSON pour le champ "filiere"
                    JSONObject filiereJson = new JSONObject();
                    filiereJson.put("id", selectedFiliere.getId());
                    filiereJson.put("code", selectedFiliere.getCode());
                    filiereJson.put("name", selectedFiliere.getLibelle());

                    jsonbody.put("filiere", filiereJson);

                    requestQueue = Volley.newRequestQueue(StudentAdd.this);
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl,
                            jsonbody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("resultat", response + "");
                            Toast.makeText(StudentAdd.this, "Response:" + response.toString(), Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Erreur", error.toString());
                            Toast.makeText(StudentAdd.this, "Response:" + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                    requestQueue.add(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }






    //////////////spinner
    private void loadFilieres() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, filieresUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject filiereJson = response.getJSONObject(i);
                                int id = filiereJson.getInt("id");
                                String code = filiereJson.getString("code");
                                String name = filiereJson.getString("name");
                                Filiere filiere = new Filiere(id, code, name);
                                filieresList.add(filiere);
                            }
                            populateSpinner();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(request);
    }

    private void populateSpinner() {
        ArrayAdapter<Filiere> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filieresList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }



}
