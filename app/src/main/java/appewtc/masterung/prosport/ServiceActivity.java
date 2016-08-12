package appewtc.masterung.prosport;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class ServiceActivity extends AppCompatActivity {

    //Explicit
    private static final int myINT = 1;
    private String videoUrlString, nameVideoString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
    }   // Main Method

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

        }   // if

    }   // onActivityResult

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
