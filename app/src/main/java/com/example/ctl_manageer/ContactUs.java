package com.example.ctl_manageer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ContactUs extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText message;
    private Button btnSend;

    private String to;
    private String from;
    private String mess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Light);
        }

        name = findViewById(R.id.edtTextName);
        email = findViewById(R.id.edtTextEmail);
        message = findViewById(R.id.edtTextMessage);
        btnSend = findViewById(R.id.btnSend);

        email.setText("ST10118053@vcconnect.edu.za");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
    }

    private void sendMail() {
        to = "ST10118053@vcconnect.edu.za";
        from = name.getText().toString();
        String [] recipients = {to};
        mess = message.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT,from);
        intent.putExtra(Intent.EXTRA_TEXT,mess);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose an email client"));
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }


}