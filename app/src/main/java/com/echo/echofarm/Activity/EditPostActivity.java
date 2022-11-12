package com.echo.echofarm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.echo.echofarm.Interface.UploadPhotoClickListener;
import com.echo.echofarm.R;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditPostActivity extends AppCompatActivity implements View.OnClickListener{

    private String[] tags = {"IT / 가전", "패션의류", "패션잡화", "식품", "스포츠 / 레저", "애완용품", "기타"};
    private String userSelectedTag = tags[0], wantedSelectedTag = tags[0];
    private ImageButton firstUploadBtn, additionalUploadBtn;
    private Button postUploadBtn;
    private EditText titleEditText, contentsEditText;
    private CheckBox disallowOtherTags;
    private TextView photoCheck, titleCheck;
    private boolean cameraPermission;
    private boolean fileReadPermission;
    private boolean fileWritePermission;
    private ArrayList<UploadedPhotoData> list;
    private Uri photoURI;
    private String currentPhotoPath;

    private final int RESULT_TAKE_PHOTO = 0;
    private final int RESULT_SELECT_PHOTO = 1;

    public void checkNeededPermission() {
        if (ContextCompat.checkSelfPermission(EditPostActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            cameraPermission = true;
            Log.i("my", "camera permission granted", null);
        }
        if (ContextCompat.checkSelfPermission(EditPostActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            fileWritePermission = true;
            Log.i("my", "write permission granted", null);
        }
        if (ContextCompat.checkSelfPermission(EditPostActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            fileReadPermission = true;
            Log.i("my", "read permission granted", null);
        }

        // 권한요청
        if (!cameraPermission && !fileWritePermission && !fileReadPermission) {
            Log.i("my", "request", null);
            ActivityCompat.requestPermissions(EditPostActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ,Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("my", "request result called", null);
        if(requestCode == 100 && grantResults.length > 0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("my", "camera permission true", null);
                cameraPermission = true;
            }
            if(grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.i("my", "write permission true", null);
                fileWritePermission = true;
            }
            if(grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Log.i("my", "read permission true", null);
                fileReadPermission = true;
            }
        }
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            Toast.makeText(EditPostActivity.this, "이 디바이스에서 카메라 기능을 지원하지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private void setFirstPhoto(Bitmap bitmap) {
        if (bitmap != null) {
            firstUploadBtn.setImageBitmap(bitmap);
            firstUploadBtn.setPadding(0, 0, 0, 0);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            //after capture
            switch (requestCode) {
                case RESULT_TAKE_PHOTO: {
                    if (resultCode == RESULT_OK && intent != null) {
                        // 가장 처음 업로드한 사진
                        if(list == null) {
                            list = new ArrayList<>();
                            additionalUploadBtn.setVisibility(View.VISIBLE);

                            File file = new File(currentPhotoPath);
                            Bitmap bitmap = MediaStore.Images.Media
                                    .getBitmap(getContentResolver(), Uri.fromFile(file));

                            ExifInterface ei = new ExifInterface(currentPhotoPath);
                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_UNDEFINED);

                            setFirstPhoto(bitmap);
                        }
                        list.add(new UploadedPhotoData(photoURI));


                    }
                    break;
                }
                case RESULT_SELECT_PHOTO: {

                    boolean isFirst = false;

                    if (intent != null) {
                        if(list == null) {
                            isFirst = true;
                            list = new ArrayList<>();
                            additionalUploadBtn.setVisibility(View.VISIBLE);
                        }

                        if (intent.getClipData() == null) {     // 이미지를 하나만 선택한 경우
                            Log.e("single choice: ", String.valueOf(intent.getData()));
                            Uri imageUri = intent.getData();
                            list.add(new UploadedPhotoData(imageUri));

                            if(isFirst) {
                                Bitmap bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(),
                                        imageUri));
                                setFirstPhoto(bitmap);
                            }

                        } else {      // 이미지를 여러장 선택한 경우
                            ClipData clipData = intent.getClipData();
                            Log.e("clipData", String.valueOf(clipData.getItemCount()));

                            if (clipData.getItemCount() + list.size() > 10) {   // 선택한 이미지가 11장 이상인 경우
                                Toast.makeText(getApplicationContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                            } else {   // 선택한 이미지가 1장 이상 10장 이하인 경우

                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.

                                    if(isFirst) {
                                        Bitmap bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(),
                                                imageUri));
                                        setFirstPhoto(bitmap);
                                    }

                                    try {
                                        list.add(new UploadedPhotoData(imageUri));  //uri를 list에 담는다.
                                    } catch (Exception e) {
                                        Log.e("my", "File select error", e);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception e){
                Log.i("my", "onActivityResult Error !", e);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        Log.i("my",""+currentPhotoPath, null);
        Log.i("my", "createFile", null);
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
            Log.i("my", "resolveAct != null", null);
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.i("my", "create imagefile", null);
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.i("my", "photofile != null", null);
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, RESULT_TAKE_PHOTO);
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        list = null;

        //uploadPhotoDialog.show();
        firstUploadBtn = findViewById(R.id.first_upload_photo_btn);
        additionalUploadBtn = findViewById(R.id.additional_upload_btn);
        postUploadBtn = findViewById(R.id.post_upload_btn);
        titleEditText = findViewById(R.id.post_title_edittext);
        contentsEditText = findViewById(R.id.post_contents_edittext);
        disallowOtherTags = findViewById(R.id.other_tag_disallow_checkbox);
        photoCheck = findViewById(R.id.uploaded_check_text);
        titleCheck = findViewById(R.id.post_title_check_text);

        firstUploadBtn.setOnClickListener(this);
        additionalUploadBtn.setOnClickListener(this);
        postUploadBtn.setOnClickListener(this);

        // 액션바 제목
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#000'>게시물 작성</font>"));

        // 내 tag
        Spinner myTag = findViewById(R.id.my_tag);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, tags
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myTag.setAdapter(adapter);

        myTag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userSelectedTag = tags[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // 교환 tag
        Spinner exchangeTag = findViewById(R.id.exchange_tag);
        ArrayAdapter<String> exAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, tags
        );
        exAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exchangeTag.setAdapter(exAdapter);

        exchangeTag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                wantedSelectedTag = tags[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private View.OnClickListener TakeAPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("my", "take", null);
        }
    };
    private View.OnClickListener OpenGallery = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("my", "open", null);
        }
    };

    // 사진업로드 버튼 처리
    @Override
    public void onClick(View view) {
        if((view == firstUploadBtn && list == null) || view == additionalUploadBtn) {
            // 권한 확인, 요청
            checkNeededPermission();
            UploadPhotoDialog uploadPhotoDialog = new UploadPhotoDialog(EditPostActivity.this,
                    new UploadPhotoClickListener() {
                        @Override
                        public void onTakePhotoClick() {
                            // 모든권한 획득, 사진찍기
                            if (cameraPermission && fileWritePermission) {
                                // 업로드사진 10장으로 제한
                                if(list != null && list.size() == 10) {
                                    Toast.makeText(EditPostActivity.this, "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show();
                                }
                                else if (checkCameraHardware(EditPostActivity.this)) {
                                    dispatchTakePictureIntent();
                                }
                            }
                        }
                        @Override
                        public void onOpenGalleryClick() {
                            // 모든권한 획득, 갤러리 불러오기
                            if (fileReadPermission && fileWritePermission) {
                                // 업로드사진 10장으로 제한
                                if(list != null && list.size() == 10) {
                                    Toast.makeText(EditPostActivity.this, "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);  // 다중 이미지를 가져올 수 있도록 세팅
                                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(intent, RESULT_SELECT_PHOTO);
                                }
                            }
                        }
                    });
            uploadPhotoDialog.show();
        } else if(view == firstUploadBtn){
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.echo.echofarm",
                    "com.echo.echofarm.Activity.UploadedPhotosActivity"));
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("URI_ARRAY", list);
            intent.putExtra("BUNDLE", bundle);
            startActivity(intent);
        } else if(view == postUploadBtn) {
            boolean isPostable = true;

            if(list == null) {
                photoCheck.setVisibility(View.VISIBLE);
                isPostable = false;
            } else
                photoCheck.setVisibility(View.INVISIBLE);

            if(titleEditText.getText().toString().equals("")) {
                titleCheck.setVisibility(View.VISIBLE);
                isPostable = false;
            } else
                titleCheck.setVisibility(View.INVISIBLE);

            if(isPostable) {
                ArrayList<Uri> uriList = new ArrayList<>();
                for(UploadedPhotoData data : list)
                    uriList.add(data.getPhotoUri());

                String title = titleEditText.getText().toString();
                String contents = contentsEditText.getText().toString();
                Boolean isDisallowOtherTags = disallowOtherTags.isChecked();
            }
        }
    }
}