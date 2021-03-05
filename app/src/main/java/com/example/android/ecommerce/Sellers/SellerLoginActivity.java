package com.example.android.ecommerce.Sellers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {

    private Button sellerLoginBtn;
    private EditText emailInput,passwordInput;

    private FirebaseAuth mAuth;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        mAuth = FirebaseAuth.getInstance();

        sellerLoginBtn = findViewById(R.id.seller_login_btn);
        emailInput = findViewById(R.id.seller_login_email);
        passwordInput = findViewById(R.id.seller_login_password);

        loadingBar = new ProgressDialog(SellerLoginActivity.this);

        sellerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSeller();
            }
        });


    }

    private void loginSeller() {

        String email  = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if(TextUtils.isEmpty(email) || email.indexOf("@") == -1)
        {
            Toast.makeText(this, "Enter Seller Email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password) || password.length()<6)
        {
            Toast.makeText(this, "Enter Seller Password of Atleast 6 digits", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Seller Account Login");
            loadingBar.setMessage("Please Wait,While we are checking credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                        Toast.makeText(SellerLoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        loadingBar.dismiss();
                        Toast.makeText(SellerLoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            
            
        }

    }
}