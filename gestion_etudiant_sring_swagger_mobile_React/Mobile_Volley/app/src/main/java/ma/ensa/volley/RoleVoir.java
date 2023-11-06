package ma.ensa.volley;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import ma.ensa.volley.adapter.AdapterRole;
import ma.ensa.volley.beans.Filiere;
import ma.ensa.volley.beans.Role;

public class RoleVoir extends AppCompatActivity {
    private ListView idroleListView;
    private AdapterRole roleAdapter;
    private RequestQueue requestQueue;
    Button backButtonRoleAdd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.role_lists);
        backButtonRoleAdd = findViewById(R.id.backButtonRoleAdd);
        backButtonRoleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoleVoir.this, RoleAdd.class);
                startActivity(intent);
            }
        });





       idroleListView = findViewById(R.id.idroleListView);

        fetchRolesFromServer();



    }

    private void fetchRolesFromServer() {
        String fetchUrl = "http://10.0.2.2:8088/api/v1/roles";
        requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, fetchUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("oki", response);
               List<Role> roles = parseRoleData(response);

                roleAdapter = new AdapterRole(RoleVoir.this, roles);
                idroleListView.setAdapter(roleAdapter);

                idroleListView.setOnItemClickListener((parent, view, position, id) -> {
                    showActionDialog(roles.get(position));
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
    private List<Role> parseRoleData(String response) {
        List<Role> roles = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");

                String name = jsonObject.getString("name");


                Role role = new Role(id, name);
                roles.add(role);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return roles;
    }

    private void showActionDialog(final Role role) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustomStyle);
        builder.setTitle("Actions")
                .setItems(new CharSequence[]{"Update", "Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            showUpdateDialog(role);
                        } else {
                            showDeleteConfirmationDialog(role.getId());
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showDeleteConfirmationDialog(final int roleId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm the delete")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteRole(roleId);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteRole(int roleId) {
        String deleteUrl = "http://10.0.2.2:8088/api/v1/roles/deleteRole/"+roleId;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.DELETE, deleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(RoleVoir.this, "ROLE deleted successfully", Toast.LENGTH_LONG).show();

                fetchRolesFromServer();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RoleVoir.this, "Error deleting ROLE", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);
    }

    private void showUpdateDialog(final Role role) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Role Information");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);



        final EditText nameInput = new EditText(this);
        nameInput.setHint("Name");
        nameInput.setText(role.getName());
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

                //   String newVille = villeSpinner.getSelectedItem().toString();
                //  String newSexe = (hommeRadio.isChecked()) ? "homme" : "femme";

                role.setName(newName);

                //   student.setVille(newVille);
                //   student.setSexe(newSexe);

                sendUpdateRequest(role);

                roleAdapter.notifyDataSetChanged();
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
    private void sendUpdateRequest(final Role role) {
        String updateUrl = "http://10.0.2.2:8088/api/v1/roles/updateRole/" + role.getId();
        JSONObject jsonbody = new JSONObject();
        try {

            jsonbody.put("name", role.getName());  } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, updateUrl, jsonbody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(RoleVoir.this, "role updated successfully", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RoleVoir.this, "Error updating role", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);
    }



}