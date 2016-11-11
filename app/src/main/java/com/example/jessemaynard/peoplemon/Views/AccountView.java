package com.example.jessemaynard.peoplemon.Views;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jessemaynard.peoplemon.MainActivity;
import com.example.jessemaynard.peoplemon.Models.Account;
import com.example.jessemaynard.peoplemon.Models.ImageLoadedEvent;
import com.example.jessemaynard.peoplemon.Network.RestClient;
import com.example.jessemaynard.peoplemon.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountView extends RelativeLayout {
    private Context context;
    // Bind all of the fields from the account_view.xml with ButterKnife.
        @Bind(R.id.edit_name_field)
        EditText editNameField;

        @Bind(R.id.change_username)
        Button changeUsername;

        @Bind(R.id.user_name)
        TextView userName;

        @Bind(R.id.email_address)
        TextView emailAddress;

        @Bind(R.id.profile_avatar)
        ImageView profileAvatar;

    private String selectedImage;
    public Bitmap scaledImage;

    public AccountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        //Sets the content for the user profile page
            makeApiCallForProfile();
            EventBus.getDefault().register(this);
    }

    @OnClick(R.id.upload_avatar)
    public void uploadAvatar(){
        ((MainActivity)context).getImage();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setSelectedImage(ImageLoadedEvent event){
        selectedImage = event.selectedImage;
        Bitmap image = BitmapFactory.decodeFile(selectedImage);
        profileAvatar.setImageBitmap(image);
    }

    @OnClick(R.id.change_username)
    public void editProfile(){

        //Hide the keyboard.
            InputMethodManager imm = (InputMethodManager)context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editNameField.getWindowToken(), 0);

        // Add the input to a variable.
            String name = editNameField.getText().toString();
            Account account = new Account(name, null);

            RestClient restClient = new RestClient();
            restClient.getApiService().postInfo(account).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (response.isSuccessful()){
                        // After posting the info to the server return it with the profile api call.
                            makeApiCallForProfile();
                            Toast.makeText(context,"Username Successfully Changed", Toast.LENGTH_LONG).show();

                    }else{

                        resetView();
                        Toast.makeText(context,getContext().getString(R.string.get_info_error) + ": " + response.code(), Toast.LENGTH_LONG).show();

                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    resetView();
                    Toast.makeText(context,getContext().getString(R.string.get_info_error), Toast.LENGTH_LONG).show();
                }
            });
    }

    private void resetView(){
        changeUsername.setEnabled(true);
    }

    private void makeApiCallForProfile(){

        RestClient restClient = new RestClient();
        restClient.getApiService().getInfo().enqueue(new Callback<Account>() {

            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                // Is the server response between 200 to 299
                    if (response.isSuccessful()){
                        // Obtain the info from the returned account.
                            Account authUser = response.body();
                        // Log what the server returns.
                            Log.d("--Username--: ", authUser.getFullname());
                            Log.d("--E-mail Address--: ",authUser.getEmail());
                            Log.d("--Avatar--: ",authUser.getAvatar());
                        // Convert the image into base64.
                            String encodedImage = authUser.getAvatar();
                            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        // Set the Username, E-mail and user Avatar based on what is returned from the server.
                            userName.setText(authUser.getFullname());
                            emailAddress.setText(authUser.getEmail());
                            profileAvatar.setImageBitmap(decodedByte);

                }else{
                    resetView();
                    Toast.makeText(context,"Get User Info Failed" + ": " + response.code(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                resetView();
                Toast.makeText(context,"Get User Info Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }
}
