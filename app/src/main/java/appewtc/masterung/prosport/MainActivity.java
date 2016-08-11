package appewtc.masterung.prosport;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Widget
        userEditText = (EditText) findViewById(R.id.editText4);
        passwordEditText = (EditText) findViewById(R.id.editText5);

    }   // Main Method

    //Create Inner Class
    private class SynUser extends AsyncTask<Void, Void, String> {

        //Explicit
        private Context context;
        private String myUserString, myPasswordString, truePasswordString, nameString;
        private static final String urlJSON = "http://swiftcodingthai.com/mama/get_user_master.php";
        private boolean statusABoolean = true;

        public SynUser(Context context, String myUserString, String myPasswordString) {
            this.context = context;
            this.myUserString = myUserString;
            this.myPasswordString = myPasswordString;
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(urlJSON).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                return null;
            }


        }   // doInBack

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("11AugV1", "JSON ==> " + s);

            try {

                JSONArray jsonArray = new JSONArray(s);

                for (int i=0;i<jsonArray.length();i+=1) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if (myUserString.equals(jsonObject.getString("User"))) {

                        statusABoolean = false;
                        truePasswordString = jsonObject.getString("Password");
                        nameString = jsonObject.getString("Name");

                    }   //if

                }   // for

                //Check User
                if (statusABoolean) {
                    //User False
                    MyAlert myAlert = new MyAlert();
                    myAlert.myDialog(context, "User False",
                            "ไม่มี " + myUserString + " ในฐานข้อมูลของเรา");
                } else if (myPasswordString.equals(truePasswordString)) {
                    //Password True
                    Toast.makeText(context, "ยินดีต้อนรับ " + nameString,
                            Toast.LENGTH_SHORT).show();

                } else {
                    //Password False
                    MyAlert myAlert = new MyAlert();
                    myAlert.myDialog(context, "Password False",
                            "Please Try again Password False");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }   // onPost

    }   // SynUser Class


    public void clickSignIn(View view) {

        //Get Value
        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        //Check Space
        if (userString.equals("") || passwordString.equals("")) {
            //Have Space
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(this,
                    getResources().getString(R.string.have_space_title),
                    getResources().getString(R.string.have_space_message));
        } else {
            //No Space
            SynUser synUser = new SynUser(this, userString, passwordString);
            synUser.execute();
        }

    }   // clickSignIn


    public void clickSignUpMain(View view) {
        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
    }

}   // Main Class
