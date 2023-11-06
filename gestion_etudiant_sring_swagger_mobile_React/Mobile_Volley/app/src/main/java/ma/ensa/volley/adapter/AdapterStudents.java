package ma.ensa.volley.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ma.ensa.volley.R;
import ma.ensa.volley.beans.Students;

public class AdapterStudents extends ArrayAdapter<Students> {
    private Context context;
    private List<Students> studentsList;

    public AdapterStudents(Context context, List<Students> studentsList) {
        super(context, 0, studentsList);
        this.context = context;
        this.studentsList = studentsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.student_item, parent, false);
        }

        Students student1 = studentsList.get(position);

        TextView firstnameTextView = convertView.findViewById(R.id.firstnameTextStudent);
        TextView lastnameTextView = convertView.findViewById(R.id.lastnameTextStudent);
        TextView loginTextView = convertView.findViewById(R.id.loginTextStudent);
  //      TextView passwordTextView = convertView.findViewById(R.id.passwordTextStudent);
        TextView telephoneTextView = convertView.findViewById(R.id.telephoneTextStudent);
        TextView filiereTextView = convertView.findViewById(R.id.filiereTextStudent);


        firstnameTextView.setText(student1.getFirstname());
        lastnameTextView.setText(student1.getLastname());
        loginTextView.setText(student1.getLogin());
//        passwordTextView.setText(student1.getPassword());
        telephoneTextView.setText(student1.getTelephone());
        filiereTextView.setText(student1.getFiliere().getLibelle());

        return convertView;
    }
}
