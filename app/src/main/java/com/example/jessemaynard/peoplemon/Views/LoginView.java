package com.example.jessemaynard.peoplemon.Views;

import android.content.Context;
import android.util.AttributeSet;
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
import com.example.jessemaynard.peoplemon.Stages.PeopleMonMapStage;
import com.example.jessemaynard.peoplemon.Stages.RegisterStage;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jessemaynard on 11/7/16.
 */
//TODO Add the additional fields that the peoplemon api requires.
public class LoginView extends LinearLayout{
    private Context context;

    @Bind(R.id.email_field)
    EditText emailField;

    @Bind(R.id.password_field)
    EditText passwordField;

    @Bind(R.id.login_button)
    Button loginButton;

    @Bind(R.id.register_button)
    Button registerButton;

    @Bind(R.id.spinner)
    ProgressBar spinner;

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.register_button)
    public void showRegisterView(){
        Flow flow = PeopleMonApplication.getMainFlow();
        History newHistory = flow.getHistory().buildUpon().push(new RegisterStage()).build();

        flow.setHistory(newHistory, Flow.Direction.FORWARD);
    }

    @OnClick(R.id.login_button)
    public void login(){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(emailField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);

        String username = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (username.isEmpty() || password.isEmpty()){
            Toast.makeText(context, "Username/Password is Empty", Toast.LENGTH_LONG).show();
        } else {
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);
            spinner.setVisibility(VISIBLE);

            Account account = new Account(username, password, "password");
            RestClient restClient = new RestClient();
            restClient.getApiService().login(username, password, "password").enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if (response.isSuccessful()){
                        Account authAccount = response.body();
                        UserStore.getInstance().setToken(authAccount.getToken());
                        UserStore.getInstance().setTokenExpiration(authAccount.getTokenExpiration());
                        Flow flow = PeopleMonApplication.getMainFlow();
                        History newHistory = History.single(new PeopleMonMapStage());
                        flow.setHistory(newHistory, Flow.Direction.REPLACE);
                    } else {
                        Toast.makeText(context, "Login Failed" + ": " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
