package com.example.uktourism.Views.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uktourism.Model.Repository.LoginCallback;
import com.example.uktourism.R;
import com.example.uktourism.Model.DataModel.User;
import com.example.uktourism.ViewModel.AuthViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText email,password,confirmPassword,name;
    private TextInputLayout lpassword,lconfirPassword;
    private Button signUpBtn;
    private TextView signInHyperlink;

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Casting Views
        name=findViewById(R.id.name_edit_text);
        email=findViewById(R.id.email_edit_text);
        password=findViewById(R.id.password_edit_text);
        lpassword=findViewById(R.id.password_layout);
        lconfirPassword=findViewById(R.id.confirm_password_layout);
        confirmPassword=findViewById(R.id.confirm_password_edit_text);
        signUpBtn=findViewById(R.id.sign_up_btn);
        signInHyperlink=findViewById(R.id.sign_in_hyperlink);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        //SignUp logic
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nameTxt = name.getText().toString();
                String emailTxt = email.getText().toString();
                String passwordTxt = password.getText().toString();
                String confirmPasswordTxt = confirmPassword.getText().toString();

                boolean isEqual = confirmPasswordTxt.equals(passwordTxt);

                if(nameTxt.isEmpty() || emailTxt.isEmpty()
                        || passwordTxt.isEmpty() || confirmPasswordTxt.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                }
                else if (passwordTxt.length() < 8) {
                    lpassword.setError("Password too short");
                }
                else if (isEqual == false) {
                    lconfirPassword.setError("Passwords are not same");
                }
                else {

                    // Get the input method manager
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    // Hide the keyboard
                    inputMethodManager.hideSoftInputFromWindow(password.getWindowToken(), 0);

                    //Registering user
                    authViewModel.registerUser(nameTxt, emailTxt, passwordTxt, new LoginCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(RegisterActivity.this, "User Registered!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // signIn Redirect
        signInHyperlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}