package com.example.android.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.ecommerce.Buyers.MainActivity;
import com.example.android.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {


    private Button sellerLoginButton,registerButton;
    private EditText nameInput,phoneInput,emailInput,passwordInput,addressInput;

    private FirebaseAuth mAuth;

    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        mAuth = FirebaseAuth.getInstance();

        sellerLoginButton = findViewById(R.id.seller_already_have_account_btn);
        registerButton = findViewById(R.id.seller_register_btn);
        nameInput = findViewById(R.id.seller_name);
        phoneInput = findViewById(R.id.seller_phone);
        emailInput = findViewById(R.id.seller_email);
        passwordInput = findViewById(R.id.seller_password);
        addressInput = findViewById(R.id.seller_address);

        loadingBar = new ProgressDialog(SellerRegistrationActivity.this);



        sellerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegistrationActivity.this,SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();
            }
        });
    }

    private void registerSeller() {

        String name = nameInput.getText().toString();
        String phone  = phoneInput.getText().toString();
        String email  = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String address = addressInput.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Enter Seller Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Enter Seller Phone", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email) || email.indexOf("@") == -1)
        {
            Toast.makeText(this, "Enter Seller Email with proper suffix", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password) || password.length()<6)
        {
            Toast.makeText(this, "Enter Seller Password of Min 6 digits", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(address))
        {
            Toast.makeText(this, "Enter Seller Address", Toast.LENGTH_SHORT).show();
        }

        else
        {

            loadingBar.setTitle("Creating Seller Account");
            loadingBar.setMessage("Please Wait,We Are Registering your account");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        final DatabaseReference rootRef;
                        rootRef = FirebaseDatabase.getInstance().getReference();

                        String sid = mAuth.getCurrentUser().getUid();

                        HashMap<String,Object> sellerMap = new HashMap<>();
                        sellerMap.put("sid",sid);
                        sellerMap.put("phone",phone);
                        sellerMap.put("email",email);
                        sellerMap.put("address",address);
                        sellerMap.put("name",name);

                        rootRef.child("Sellers").child(sid).updateChildren(sellerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    loadingBar.dismiss();
                                    Toast.makeText(SellerRegistrationActivity.this, "You are Registered Successfully", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(SellerRegistrationActivity.this, SellerHomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                }
                                else
                                {
                                    loadingBar.dismiss();
                                    Toast.makeText(SellerRegistrationActivity.this, "Unable to update details to database", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }

    }
}