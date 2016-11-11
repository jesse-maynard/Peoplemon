package com.example.jessemaynard.peoplemon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.davidstemmer.flow.plugin.screenplay.ScreenplayDispatcher;
import com.example.jessemaynard.peoplemon.Models.Account;
import com.example.jessemaynard.peoplemon.Models.ImageLoadedEvent;
import com.example.jessemaynard.peoplemon.Network.RestClient;
import com.example.jessemaynard.peoplemon.Network.UserStore;
import com.example.jessemaynard.peoplemon.Stages.LoginStage;
import com.example.jessemaynard.peoplemon.Stages.PeopleMonMapStage;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import flow.Flow;
import flow.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {


    public static int RESULT_LOAD_IMAGE = 1;
    private String avatar;

    //Variable for logging.
    private String TAG = "MainActivity";
    //Flow and Screenplay.
    private Flow flow;
    private ScreenplayDispatcher dispatcher;
    //Binding the container for the relative layout.
    @Bind(R.id.container)
    RelativeLayout container;
//TODO Hanging onto this in case I decide that a menu is a good thing to have.
//    private Menu menu;

    public Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // Setup Flow and the dispatcher.
        flow = PeopleMonApplication.getMainFlow();
        dispatcher = new ScreenplayDispatcher(this, container);
        dispatcher.setUp(flow);

        //TODO Add a button to Log out.
        //Set to a button to log out :D
        // Logs the user out by setting the token to null.
//        UserStore.getInstance().setToken(null);

        if (Build.VERSION.SDK_INT >= 23) {
            if (!(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            if (!(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            if (!(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        // If a token is invalid or null take them to the Login Screen.
        if (UserStore.getInstance().getToken() == null || UserStore.getInstance().getTokenExpiration() == null){
            History newHistory = History.single(new LoginStage());
            flow.setHistory(newHistory, Flow.Direction.REPLACE);
        }
    }

    public void getImage(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{

            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imageString = cursor.getString(columnIndex);
                cursor.close();

//                //Convert to Bitmap Array
//                Bitmap bm = BitmapFactory.decodeFile(imageString);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
//                byte[] b = baos.toByteArray();
//
//                //Take the bitmap Array and e
//                // encode it to Base64
//                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

//                Log.d("***BASE64****", encodedImage);
//                makeApiCallForProfile(encodedImage);

                //Make API Call to Send Base64 to Server


                EventBus.getDefault().post(new ImageLoadedEvent(imageString));

            }else{
                Toast.makeText(this,"Error Retrieving Image", Toast.LENGTH_LONG).show();

            }

        } catch (Exception e){
            Toast.makeText(this,"Error Retrieving Image", Toast.LENGTH_LONG).show();
        }
    }

//    private void makeApiCallForProfile(String imageString){
//        avatar = imageString;
//        Account user = new Account(null, avatar);
//        RestClient restClient = new RestClient();
//        restClient.getApiService().postInfo(user).enqueue(new Callback<Void>() {
//
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                // Is the server response between 200 to 299
//                if (response.isSuccessful()){
//
//
//                }else{
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        if (!flow.goBack()){
            flow.removeDispatcher(dispatcher);
            flow.setHistory(History.single(new PeopleMonMapStage()),
                    Flow.Direction.BACKWARD);
            super.onBackPressed();
        }
    }

    //TODO More than likely don't need this, but just in case I need a menu item.
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_menu, menu);
//        this.menu = menu;
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.open_calendar:
//                Flow flow = BudgetApplication.getMainFlow();
//                History newHistory = flow.getHistory().buildUpon()
//                        .push(new CalendarListStage())
//                        .build();
//                flow.setHistory(newHistory, Flow.Direction.FORWARD);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

//    public void showMenuItem(boolean show){
//        if (menu != null){
//            menu.findItem(R.id.open_calendar).setVisible(show);
//        }
//    }
}
