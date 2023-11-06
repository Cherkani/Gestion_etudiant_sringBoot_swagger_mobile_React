package ma.ensa.volley.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ma.ensa.volley.R;
import ma.ensa.volley.beans.Role;

public class AdapterRole extends ArrayAdapter<Role> {
    private Context context;
    private List<Role> roles;

    public AdapterRole(Context context, List<Role> roles) {
        super(context, 0, roles);
        this.context = context;
        this.roles = roles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.role_item, parent, false);
        }


        Role role1 = roles.get(position);

        TextView nameTextView = convertView.findViewById(R.id.nameTextrole);

        nameTextView.setText(role1.getName());

        return convertView;
    }

}
