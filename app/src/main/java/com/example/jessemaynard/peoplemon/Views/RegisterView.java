package com.example.jessemaynard.peoplemon.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jessemaynard.peoplemon.Models.Account;
import com.example.jessemaynard.peoplemon.Network.RestClient;
import com.example.jessemaynard.peoplemon.Network.UserStore;
import com.example.jessemaynard.peoplemon.PeopleMonApplication;
import com.example.jessemaynard.peoplemon.R;
import com.example.jessemaynard.peoplemon.Stages.LoginStage;
import com.example.jessemaynard.peoplemon.Stages.PeopleMonMapStage;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jessemaynard.peoplemon.Components.Constants.apikey;
import static com.example.jessemaynard.peoplemon.Components.Constants.grantType;

/**
 * Created by jessemaynard on 11/7/16.
 */

public class RegisterView extends LinearLayout{
    private Context context;

    @Bind(R.id.email_field)
    EditText emailField;

    @Bind(R.id.password_field)
    EditText passwordField;

    @Bind(R.id.confirm_field)
    EditText confirmPassword;

    @Bind(R.id.full_name_field)
    EditText fullnameField;

    @Bind(R.id.register_button)
    Button registerButton;

    @Bind(R.id.spinner)
    ProgressBar spinner;


    public RegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.register_button)
    public void register(){
        InputMethodManager imm =(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(emailField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(confirmPassword.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(fullnameField.getWindowToken(), 0);

        final String email = emailField.getText().toString();
        final String password = passwordField.getText().toString();
        String passConfirm = confirmPassword.getText().toString();
        String fullname = fullnameField.getText().toString();
        String avatar = "string";


        if (email.isEmpty() || password.isEmpty() || passConfirm.isEmpty() || fullname.isEmpty()){
            Toast.makeText(context, "You must enter something into all fields", Toast.LENGTH_LONG).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(context, "You must provide a valid email address", Toast.LENGTH_LONG).show();
        } else if (!password.equals(passConfirm)){
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show();
        } else {
            registerButton.setEnabled(false);
            spinner.setVisibility(VISIBLE);

//            Account account = new Account(email, fullname, password);
            final RestClient restClient = new RestClient();
            restClient.getApiService().register(email, fullname, avatar, apikey, password ).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(context, "Treats me right", Toast.LENGTH_LONG).show();
                        //Login when the registration is complete.
                        restClient.getApiService().login(email, password, grantType).enqueue(new Callback<Account>() {
                            @Override
                            public void onResponse(Call<Account> call, Response<Account> response) {
                                if (response.isSuccessful()) {
                                    Account regAccount = response.body();
                                    UserStore.getInstance().setToken(regAccount.getToken());
                                    UserStore.getInstance().setTokenExpiration(regAccount.getTokenExpiration());
                                    Flow flow = PeopleMonApplication.getMainFlow();
                                    History newHistory = History.single(new PeopleMonMapStage());
                                    flow.setHistory(newHistory, Flow.Direction.REPLACE);
                                } else {
                                    Toast.makeText(context, "Make me a sandwich", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Account> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });

                    } else {
                        resetView();
                        Toast.makeText(context, "Registration Failed" + ": " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }


                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    resetView();
                    Toast.makeText(context, "Registration Failed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void resetView(){
        registerButton.setEnabled(true);
        spinner.setVisibility(GONE);
    }
}
