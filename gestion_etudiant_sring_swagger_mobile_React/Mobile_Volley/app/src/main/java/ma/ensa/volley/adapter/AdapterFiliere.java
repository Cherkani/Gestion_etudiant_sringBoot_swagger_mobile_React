package ma.ensa.volley.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ma.ensa.volley.R;
import ma.ensa.volley.beans.Filiere;

public class AdapterFiliere extends ArrayAdapter<Filiere> {
    private Context context;
    private List<Filiere> filieres;

    public AdapterFiliere(Context context, List<Filiere> filieres) {
        super(context, 0, filieres);
        this.context = context;
        this.filieres = filieres;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.filiere_item, parent, false);
        }


        Filiere filiere1 = filieres.get(position);

        TextView nameTextView = convertView.findViewById(R.id.nameText);
        TextView codeTextView = convertView.findViewById(R.id.codeText);

        nameTextView.setText(filiere1.getLibelle());
        codeTextView.setText(filiere1.getCode());

        return convertView;
    }

}
