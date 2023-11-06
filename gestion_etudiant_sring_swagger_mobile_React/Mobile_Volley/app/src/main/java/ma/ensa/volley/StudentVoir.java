package ma.ensa.volley;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.volley.adapter.AdapterStudents;
import ma.ensa.volley.beans.Filiere;
import ma.ensa.volley.beans.Students;

public class StudentVoir extends AppCompatActivity {

    /////////:spinner

    private Spinner spinner;
    private List<Filiere> filieresList = new ArrayList<>();
    private RequestQueue requestQueue;

    private String filieresUrl = "http://10.0.2.2:8088/api/v1/filieres";
    ////////////////

    private ListView idStudentsListView;
    private AdapterStudents studentsAdapter;
    private Button backButtonStudentsAdd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_lists);
        backButtonStudentsAdd = findViewById(R.id.backButtonStudentsAdd);

        spinner = findViewById(R.id.spinnerfiliereTextStudent);
        requestQueue = Volley.newRequestQueue(this);
        loadFilieres();

        backButtonStudentsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentVoir.this, StudentAdd.class);
                startActivity(intent);
            }
        });

        idStudentsListView = findViewById(R.id.idStudentsListView);
        fetchStudentsFromServer();
    }

    private void fetchStudentsFromServer() {
        String fetchUrl = "http://10.0.2.2:8088/api/v1/students";
        requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, fetchUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("oki", response);
                List<Students> students = parseStudentsData(response);
                studentsAdapter = new AdapterStudents(StudentVoir.this, students);
                idStudentsListView.setAdapter(studentsAdapter);

                idStudentsListView.setOnItemClickListener((parent, view, position, id) -> {
                    showActionDialog(students.get(position));
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Gérer l'erreur ici
            }
        });

        requestQueue.add(request);
    }

    private List<Students> parseStudentsData(String response) {
        List<Students> students = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String firstname = jsonObject.getString("firstname");
                String lastname = jsonObject.getString("lastname");
                String login = jsonObject.getString("login");
                String telephone = jsonObject.getString("telephone");
                String password = jsonObject.getString("password");

                JSONObject filiereJson = jsonObject.getJSONObject("filiere");
                int filiereId = filiereJson.getInt("id");
                String filiereCode = filiereJson.getString("code");
                String filiereName = filiereJson.getString("name");

                Filiere filiere = new Filiere(filiereId, filiereCode, filiereName);

                Students student = new Students(id, firstname, password, lastname, telephone, login, filiere);
                students.add(student);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return students;
    }

    private void showActionDialog(final Students student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actions")
                .setItems(new CharSequence[]{"Update", "Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            showUpdateDialog(student);
                        } else {
                            showDeleteConfirmationDialog(student.getId());
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteConfirmationDialog(final int studentId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm the delete")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteStudent(studentId);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteStudent(int studentId) {
        String deleteUrl = "http://10.0.2.2:8088/api/v1/students/" + studentId;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.DELETE, deleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(StudentVoir.this, "Student deleted successfully", Toast.LENGTH_LONG).show();
                fetchStudentsFromServer();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StudentVoir.this, "Error deleting student", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);
    }

    private void showUpdateDialog(final Students student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Student Information");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText firstnameInput = new EditText(this);
        firstnameInput.setHint("firstname");
        firstnameInput.setText(student.getFirstname());
        layout.addView(firstnameInput);

        final EditText lastnameInput = new EditText(this);
        lastnameInput.setHint("lastname");
        lastnameInput.setText(student.getLastname());
        layout.addView(lastnameInput);

        final EditText telephoneInput = new EditText(this);
        telephoneInput.setHint("telephone");
        telephoneInput.setText(student.getTelephone());
        layout.addView(telephoneInput);

        final EditText loginInput = new EditText(this);
        loginInput.setHint("login");
        loginInput.setText(student.getLogin());
        layout.addView(loginInput);

        final EditText passwordInput = new EditText(this);
        passwordInput.setHint("password");
        passwordInput.setText(student.getPassword());
        layout.addView(passwordInput);
///////////ajout spinner dans le modal
        // Ajouter un Spinner pour la filière
        Spinner filiereSpinner = new Spinner(this);
        ArrayAdapter<Filiere> filiereAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filieresList);
        filiereAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filiereSpinner.setAdapter(filiereAdapter);

        // Sélectionnez la filière actuelle de l'étudiant dans le Spinner
        int currentFilierePosition = filiereAdapter.getPosition(student.getFiliere());
        filiereSpinner.setSelection(currentFilierePosition);

        layout.addView(filiereSpinner);
///////////////////////////
        builder.setView(layout);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newFirstname = firstnameInput.getText().toString();
                String newLastname = lastnameInput.getText().toString();
                String newTelephone = telephoneInput.getText().toString();
                String newLogin = loginInput.getText().toString();
                String newPassword = passwordInput.getText().toString();

                // Obtenez la nouvelle filière sélectionnée depuis le Spinner
                Filiere newFiliere = (Filiere) filiereSpinner.getSelectedItem();

                student.setFirstname(newFirstname);
                student.setLastname(newLastname);
                student.setTelephone(newTelephone);
                student.setLogin(newLogin);
                student.setPassword(newPassword);
                student.setFiliere(newFiliere);
                Log.d("response", String.valueOf(student));
                sendUpdateRequest(student);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void sendUpdateRequest(final Students student) {
        String updateUrl = "http://10.0.2.2:8088/api/v1/students/" + student.getId();
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("firstname", student.getFirstname());
            jsonBody.put("lastname", student.getLastname());
            jsonBody.put("login", student.getLogin());
            jsonBody.put("telephone", student.getTelephone());
            jsonBody.put("password", student.getPassword());

            // Créez un objet JSON pour la filière
            JSONObject filiereJson = new JSONObject();
            filiereJson.put("id", student.getFiliere().getId());
            filiereJson.put("code", student.getFiliere().getCode());
            filiereJson.put("name", student.getFiliere().getLibelle());

            jsonBody.put("filiere", filiereJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, updateUrl, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(StudentVoir.this, "Student updated successfully", Toast.LENGTH_LONG).show();
                        fetchStudentsFromServer();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StudentVoir.this, "Error updating student", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);
    }

///////////////spinner
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
    ///////////////////////////:
}
