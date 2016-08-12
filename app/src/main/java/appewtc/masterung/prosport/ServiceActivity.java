package appewtc.masterung.prosport;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.jibble.simpleftp.SimpleFTP;

import java.io.File;

public class ServiceActivity extends AppCompatActivity {

    //Explicit
    private static final int myINT = 1;
    private static final int videoINT = 2;
    private String videoUrlString, nameVideoString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
    }   // Main Method

    public void clickAnalysis(View view) {
        startActivity(new Intent(ServiceActivity.this, AnalysinActivity.class));
    }


    public void clickRecordVideo(View view) {

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, videoINT);

    }   // clickRecord

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == myINT) && (resultCode == RESULT_OK)) {

            Uri uri = data.getData();
            videoUrlString = findPath(uri);

            Log.d("12AugV1", "Path of Video ==> " + videoUrlString);

            nameVideoString = videoUrlString.substring(videoUrlString.lastIndexOf("/") + 1);

            Log.d("12AugV1", "Name Video ==> " + nameVideoString);

            uploadVideoToServer(videoUrlString);

        }   // if

    }   // onActivityResult

    private void uploadVideoToServer(String videoUrlString) {

        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        try {

            SimpleFTP simpleFTP = new SimpleFTP();

            simpleFTP.connect("ftp.swiftcodingthai.com",
                    21, "mama@swiftcodingthai.com", "Abc12345");

            simpleFTP.bin();

            simpleFTP.cwd("Video");

            simpleFTP.stor(new File(videoUrlString));

            simpleFTP.disconnect();

            Toast.makeText(this, "Upload Video OK", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.d("12AugV1", "e ==> " + e.toString());
        }

    }   // upload

    private String findPath(Uri uri) {

        String strVideoPath = null;

        String[] columnStrings = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, columnStrings, null, null, null);

        if (cursor != null) {

            cursor.moveToFirst();
            int intColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            strVideoPath = cursor.getString(intColumnIndex);

        } else {

            strVideoPath = uri.getPath();

        }

        return strVideoPath;
    }

    public void clickUploadVideo(View view) {

        //Choose Video
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent
                , "Select Video"), myINT);


    }   // upload

}   // Main Class
