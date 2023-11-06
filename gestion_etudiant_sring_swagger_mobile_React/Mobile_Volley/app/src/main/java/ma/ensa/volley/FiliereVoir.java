package ma.ensa.volley;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.volley.adapter.AdapterFiliere;
import ma.ensa.volley.beans.Filiere;

public class FiliereVoir extends AppCompatActivity {
   private ListView idfiliereListView;
private AdapterFiliere filiereAdapter;
private RequestQueue requestQueue;
private Button backButtonfiliereAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filiere_lists);

        backButtonfiliereAdd = findViewById(R.id.backButtonfiliereAdd);
        backButtonfiliereAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FiliereVoir.this, FiliereAdd.class);
                startActivity(intent);
            }
        });


         idfiliereListView = findViewById(R.id.idfiliereListView);

        fetchStudentsFromServer();
    }
    private void fetchStudentsFromServer() {
        String fetchUrl = "http://10.0.2.2:8088/api/v1/filieres";
        requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, fetchUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<Filiere> filieres = parseFiliereData(response);

                 filiereAdapter = new AdapterFiliere(FiliereVoir.this, filieres);
                idfiliereListView.setAdapter(filiereAdapter);

                idfiliereListView.setOnItemClickListener((parent, view, position, id) -> {
                    showActionDialog(filieres.get(position));
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error here
            }
        });

        requestQueue.add(request);
    }
    private List<Filiere> parseFiliereData(String response) {
        List<Filiere> filieres = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String code = jsonObject.getString("code");
                String name = jsonObject.getString("name");


                Filiere filiere = new Filiere(id, code, name);
                filieres.add(filiere);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return filieres;
    }

    private void showActionDialog(final Filiere filiere) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustomStyle);
        builder.setTitle("Actions")
                .setItems(new CharSequence[]{"Update", "Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            showUpdateDialog(filiere);
                        } else {
                            showDeleteConfirmationDialog(filiere.getId());
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showDeleteConfirmationDialog(final int filiereId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm the delete")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteFiliere(filiereId);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteFiliere(int studentId) {
        String deleteUrl = "http://10.0.2.2:8088/api/v1/filieres/deleteFiliere/"+studentId;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.DELETE, deleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(FiliereVoir.this, "filiere deleted successfully", Toast.LENGTH_LONG).show();

                fetchStudentsFromServer();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FiliereVoir.this, "Error deleting filiere", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);
    }

    private void showUpdateDialog(final Filiere filiere) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Filiere Information");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText codeInput = new EditText(this);
        codeInput.setHint("Code");
        codeInput.setText(filiere.getCode());
        layout.addView(codeInput);

        final EditText nameInput = new EditText(this);
        nameInput.setHint("Name");
        nameInput.setText(filiere.getLibelle());
        layout.addView(nameInput);

    /*
       final Spinner villeSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> adapterV = ArrayAdapter.createFromResource(this, R.array.villes, android.R.layout.simple_spinner_item);
        adapterV.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        villeSpinner.setAdapter(adapterV);
        villeSpinner.setSelection(adapterV.getPosition(student.getVille()));
        layout.addView(villeSpinner);

     */
/*
        final RadioGroup sexeRadioGroup = new RadioGroup(this);

        RadioButton hommeRadio = new RadioButton(this);
        hommeRadio.setText("homme");
        hommeRadio.setId(View.generateViewId());
        sexeRadioGroup.addView(hommeRadio);

        RadioButton femmeRadio = new RadioButton(this);
        femmeRadio.setText("femme");
        femmeRadio.setId(View.generateViewId());
        sexeRadioGroup.addView(femmeRadio);

        if (student.getSexe().equals("homme")) {
            hommeRadio.setChecked(true);
        } else {
            femmeRadio.setChecked(true);
        }

        layout.addView(sexeRadioGroup);

        */
        builder.setView(layout);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String newName = nameInput.getText().toString();
                String newCode = codeInput.getText().toString();
             //   String newVille = villeSpinner.getSelectedItem().toString();
              //  String newSexe = (hommeRadio.isChecked()) ? "homme" : "femme";

                filiere.setLibelle(newName);
                filiere.setCode(newCode);
             //   student.setVille(newVille);
             //   student.setSexe(newSexe);

                sendUpdateRequest(filiere);

                filiereAdapter.notifyDataSetChanged();
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
    private void sendUpdateRequest(final Filiere filiere) {
        String updateUrl = "http://10.0.2.2:8088/api/v1/filieres/updateFiliere/" + filiere.getId();
        JSONObject jsonbody = new JSONObject();
        try {
            jsonbody.put("code", filiere.getCode());
            jsonbody.put("name", filiere.getLibelle());  } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, updateUrl, jsonbody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(FiliereVoir.this, "Filiere updated successfully", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FiliereVoir.this, "Error updating Filiere", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);
    }



}