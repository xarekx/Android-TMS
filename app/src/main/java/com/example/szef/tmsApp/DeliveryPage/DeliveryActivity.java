package com.example.szef.tmsApp.DeliveryPage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.szef.tmsApp.CustomListAdapter;
import com.example.szef.tmsApp.OrderPage.OrderDetails;
import com.example.szef.tmsApp.OrderPage.OrderList;
import com.example.szef.tmsApp.R;
import com.example.szef.tmsApp.TmsApplication;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.example.szef.tmsApp.CardActivity.CAMERA_PERMISSION_REQUEST_CODE;
import static com.example.szef.tmsApp.CardActivity.CAMERA_REQUEST_CODE;
import static com.example.szef.tmsApp.CardActivity.IMAGE_GALLERY_REQUEST;

public class DeliveryActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();
    ArrayList<HashMap<String,String>> deliveryValue;
    ListView clientDeliveryList;
    private String LastFile;
    private Uri imageUri;
    String filePath;
    private EditText description;
    private EditText title;
    private String getId;
    private ProgressDialog pDialog;
    private TextView imageView;
    private Button attachButton;
    private Button sendButton;
    private ArrayList<String> files = new ArrayList<>();
    Delivery mDelivery;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        final Intent intent = getIntent();
        deliveryValue = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("deliveryValue");
        clientDeliveryList = findViewById(R.id.clientDeliveryName);
        description = findViewById(R.id.EditDescription);
        title = findViewById(R.id.EditTitle);
        mDelivery = new Delivery(deliveryValue);
        getId = mDelivery.getUserId();
        imageView = findViewById(R.id.imageView);
        attachButton = findViewById(R.id.attachmentButton);
        sendButton = findViewById(R.id.sendDataButton);

        final Integer[] imageArray = {R.mipmap.ic_name};
        final String[] nameArray = {"Klient"};
        final String[] infoArray = {mDelivery.getName()};
        final CustomListAdapter listAdapter = new CustomListAdapter(this, imageArray, infoArray, nameArray, R.layout.list_item);

        if(filePath==null) {
            sendButton.setEnabled(false);
        }

        Toolbar toolbarDetails = findViewById(R.id.toolbarDetails);
        setSupportActionBar(toolbarDetails);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbarDetails.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryActivity.this, OrderDetails.class);
                intent.putExtra("json",deliveryValue);
                startActivity(intent);
            }
        });

        clientDeliveryList.setAdapter(listAdapter);
        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] dialogItems = new String[]{"Z Kamery", "Z Galerii"};
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(DeliveryActivity.this);
                mBuilder.setTitle("Dodaj załącznik");
                mBuilder.setIcon(R.drawable.ic_action_dialoglist);
                mBuilder.setSingleChoiceItems(dialogItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            onTakePhotoClicked();
                        }
                        else {
                            onImageGalleryClicked();
                        }
                        dialog.dismiss();
                    }
                });
                mBuilder.setNegativeButton("Odrzuć", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(title.getText().toString().equals("") && description.getText().toString().equals("")) {
                    Toast.makeText(DeliveryActivity.this, "Ustaw szczegóły", Toast.LENGTH_LONG).show();
                }
                else if(title.getText().toString().equals("")) {
                    Toast.makeText(DeliveryActivity.this, "Wpisz tytuł!", Toast.LENGTH_LONG).show();
                }
                else if(description.getText().toString().equals("")) {
                    Toast.makeText(DeliveryActivity.this, "Ustaw opis!", Toast.LENGTH_LONG).show();
                }
                else {
                    new postData().execute();
                    LinearLayout linearLayout = findViewById(R.id.liniowyLayout);
                    linearLayout.removeAllViews();
                }
            }
        });
    }

    // wywołanie kamery
    public void invokeCamera() {
        Uri data = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", createImageFile());

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, data);

        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                invokeCamera();
                Toast.makeText(this, R.string.activecamera, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.cannotopencamera, Toast.LENGTH_LONG).show();
            }

        }
    }

    //tworzymy plik ze zdjęciem
    private File createImageFile() {

        File picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        String timestamp = sdf.format(new Date());

        File imageFile = new File(picturesDirectory, "TMS" + timestamp + ".jpg");

        LastFile = imageFile.getAbsolutePath();

        return imageFile;

    }

    public void onTakePhotoClicked() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            invokeCamera();
        } else {
            String[] permissionRequest = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    //wywolanie galerii
    public void onImageGalleryClicked() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        String pictureDirectoryPath = pictureDirectory.getPath();

        Uri data = Uri.parse(pictureDirectoryPath);

        photoPickerIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Pattern p = Pattern.compile("[^/]*\\.(?:jpg|jpeg|gif|png|svg)");
        Matcher m;
        String view = null;


        if(requestCode == CAMERA_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                scanFile(LastFile);
                filePath = LastFile;
                m = p.matcher(filePath);
                if(filePath != null) {
                    while(m.find()) {
                        view = m.group();
                    }
                    TextView textView = new TextView(this);
                    textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.maciek));
                    LinearLayout linearLayout = findViewById(R.id.liniowyLayout);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    textView.setText(view);
                    linearLayout.addView(textView);
                    sendButton.setEnabled(true);
                    files.add(filePath);
                }
                Toast.makeText(this, "Zapisano zdjęcie", Toast.LENGTH_SHORT).show();
            }
        }
        if(resultCode == RESULT_OK) {
            if (requestCode == IMAGE_GALLERY_REQUEST)
            {
                scanFile(LastFile);
                Toast.makeText(this, "Wybrano zdjęcie", Toast.LENGTH_SHORT).show();
                imageUri = data.getData();
                filePath = getPath(imageUri);
                m = p.matcher(filePath);

                if(filePath != null) {
                    while(m.find()) {
                        view = m.group();
                    }
                    TextView textView = new TextView(this);
                    textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.maciek));
                    LinearLayout linearLayout = findViewById(R.id.liniowyLayout);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    textView.setText(view);
                    linearLayout.addView(textView);
                    sendButton.setEnabled(true);
                    files.add(filePath);
                }

            }
        }
    }

    private String getPath(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(),    contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void scanFile(String path) {

        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        MediaScannerConnection.scanFile(DeliveryActivity.this,
                new String[] { path }, new String[]{type},
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("TAG", "Finished scanning " + path);
                    }
                });
    }


    public class postData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DeliveryActivity.this, R.style.MyAlertDialogStyle);
            pDialog.setMessage("Proszę czekać...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("title",title.getText().toString());
            builder.addFormDataPart("description",description.getText().toString());
            for(int i=0;i<files.size();i++) {
                File file = new File(files.get(i));
                builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }

            MultipartBody body = builder.build();

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(TmsApplication.URL + "/order/" + getId + "/attachment")
                    .header("Authorization","Bearer "+ OrderList.token)
                    .addHeader("Content-Type", " application/x-www-form-urlencoded")
                    .post(body)
                    .build();

            Call call = client.newCall(request);
            try {
                call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            Toast.makeText(DeliveryActivity.this, "Wysłano Zlecenie!", Toast.LENGTH_SHORT).show();
        }
    }




}
