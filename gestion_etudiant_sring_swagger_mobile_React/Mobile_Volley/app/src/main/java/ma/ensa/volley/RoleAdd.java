package ma.ensa.volley;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RoleAdd extends AppCompatActivity {
    Button bnRetour, bnAddRole, bnVoirRole;
    EditText name;
    RequestQueue requestQueue;
    String postUrl = "http://10.0.2.2:8088/api/v1/roles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.role_add);
        bnRetour = findViewById(R.id.bnRetourrole);
        name = findViewById(R.id.nameEditTextrole);

        bnAddRole = findViewById(R.id.bnAddRole);
        bnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoleAdd.this, MainActivity.class);
                startActivity(intent);
            }
        });

        bnVoirRole = findViewById(R.id.bnVoirRoles);
        bnVoirRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoleAdd.this, RoleVoir.class);
                startActivity(intent);
            }
        });

        bnAddRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonbody = new JSONObject();
                try {
                    jsonbody.put("name", name.getText().toString());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                requestQueue = Volley.newRequestQueue(RoleAdd.this);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl,
                        jsonbody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("resultat", response + "");
                        Toast.makeText(RoleAdd.this, "Response:" + response.toString(), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Erreur", error.toString());
                        Toast.makeText(RoleAdd.this, "Response:" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
                requestQueue.add(request);
            }
        });
    }
}
