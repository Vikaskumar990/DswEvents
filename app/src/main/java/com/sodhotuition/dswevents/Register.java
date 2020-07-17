package com.sodhotuition.dswevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sodhotuition.dswevents.Model.Organization;

public class Register extends AppCompatActivity {
    Button btnregister;
    EditText name,email,password,phone,members,description;
    FirebaseDatabase database;
    DatabaseReference ref;
    Organization org;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //org = new organisation();
         database = FirebaseDatabase.getInstance();
         ref = database.getReference("Organization");
        //ref = FirebaseDatabase.getInstance().getReference().child("Organization");

        name =  findViewById(R.id.name);
        email =  findViewById(R.id.email);
        password =  findViewById(R.id.password);
        phone =  findViewById(R.id.phone);
        members =  findViewById(R.id.members);
        description = findViewById(R.id.description);
        btnregister =   findViewById(R.id.btnregister);

         btnregister.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {


                 final Organization org = new Organization(name.getText().toString(),
                         email.getText().toString(),
                         password.getText().toString(),
                         phone.getText().toString(),
                         members.getText().toString(),
                         description.getText().toString());

                 ref.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError databaseError) {

                     }
                 });


                 /*org.setName(name.getText().toString().trim());
                 org.setEmail(email.getText().toString().trim());
                 org.setPassword(password.getText().toString().trim());
                 org.setPhone(phone.getText().toString().trim());
                 org.setDescription(description.getText().toString().trim());
                 org.setMembers(members.getText().toString().trim());*/

                 ref .push().setValue(org);
                 //reference.child("Organization01").setValue(org);
                 Toast.makeText(Register.this,"your registration completed successfully.....!",Toast.LENGTH_SHORT).show();
             }
         });
    }

    public void btn_login(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
/*private void getValues()
{
    org.setName(name.getText().toString());
    org.setEmail(email.getText().toString());
    org.setPassword(password.getText().toString());
    org.setPhone(phone.getText().toString());
    org.setDescription(description.getText().toString());
    org.setMembers(members.getText().toString());
}*/


   /* public void btn_register(View view) {
        reference.addValueEventListener(new ValueEventListener()             @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getValues();
                reference.child("Organization01").setValue(org);
                Toast.makeText(Register.this,"your registration completed successfully.....!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
}
