package cn.licht.mobile.anim;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import cn.licht.mobile.anim.frzz.FloatViewManager;
import cn.licht.mobile.anim.frzz.FloatViewWraper;
import cn.licht.mobile.anim.frzz.IFlySpeakFloatView;
import cn.licht.mobile.anim.frzz.SpeakFloatView;
import cn.licht.mobile.anim.widget.TextFolatView;
import cn.licht.mobile.anim.widget.keyboard.KeyboardFullSafeWindow;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CCYXMain";
    private ImageView view;
    private ObjectAnimator anim;
    private RotateAnimation rotateAnimation;
    private boolean issFtar = true;
    private ImageView imageViewaa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.cc);
        view.setImageResource(R.drawable.icon_close_flashlight);
        View viewById = findViewById(R.id.bass);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("zyl","你是谁啊");
            }
        });
        imageViewaa = findViewById(R.id.pload);
        View textFm = findViewById(R.id.textfm);
        View hellov = findViewById(R.id.hellv);

        hellov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatViewManager.getInstance().detach(IFlySpeakFloatView.class);

                Log.i("zylbug","setOnClickListener");

            }
        });

        hellov.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("zylbug","setOnTouchListener");
                return false;
            }
        });
        int scaledTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        Log.d(TAG, "onCreate: scaleTouchSlop"+ scaledTouchSlop);
        int longPressTimeout = ViewConfiguration.getLongPressTimeout();
        int tapTimeout = ViewConfiguration.getTapTimeout();
        try {
            ApplicationInfo applicationInfo = getApplicationContext().getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String xiaoxi = applicationInfo.metaData.getString("xiaoxi");
            Log.d(TAG, "onCreate: "+ xiaoxi);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        Toast.makeText(this,scaledTouchSlop+"~" +" getLongPressTimeout()"+longPressTimeout+" tapTime "+tapTimeout,Toast.LENGTH_LONG).show();

//        textFm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: ");
//            }adb
//        });

//        File downloadCacheDirectory = Environment.getDownloadCacheDirectory();
//        String directoryDownloads = Environment.DIRECTORY_DOWNLOADS;
//        File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
//        Log.d(TAG, "onCreate: exDir"+ externalFilesDir);
        String absolutePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String externalStorageState = Environment.getExternalStorageState();
        File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
//        Log.d(TAG, "onCreate: externalFilesDir"+externalFilesDir.getAbsolutePath());
//        Log.d(TAG, "onCreate: externalStorageState "+ externalStorageState + Environment.MEDIA_MOUNTED);
//
//        Log.d(TAG, "onCreate: abs"+absolutePath);

        File externalFilesDir1 = getExternalFilesDir(null);
        Log.d(TAG, "onCreate: externalFilesDir1" + externalFilesDir1);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            Set<String> externalVolumeNames = MediaStore.getExternalVolumeNames(this);
            Iterator<String> iterator = externalVolumeNames.iterator();
            Log.d(TAG, "onCreate: externalVolumeNames" +             iterator.next());
        }

//        Log.d(TAG, "onCreate: "+downloadCacheDirectory+"~~~~~"+directoryDownloads);
        View imgAxc = findViewById(R.id.imgaaaa);
        imgAxc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
//                Uri uri = Uri.fromParts("package", getPackageName(), (String)null);
//                intent.setData(uri);
//                startActivity (intent);
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);


            }
        });

//        View viewById1 = findViewById(R.id.pp);
//        viewById1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

//        anim = ObjectAnimator.ofFloat(view, "rotation", 0, 360);
//        anim.setDuration(800);
//        anim.setRepeatCount(-1);
//        anim.setInterpolator(new LinearInterpolator());
//        anim.start();
//        rotateAnimation = new RotateAnimation(0,360);
//        rotateAnimation.setDuration(800);
//        rotateAnimation.setRepeatCount(-1);
//
//        view.setAnimation(rotateAnimation);
////        rotateAnimation.start();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sss(View view) {
        KeyboardFullSafeWindow keyboardFullSafeWindow = new KeyboardFullSafeWindow(this, null);
        keyboardFullSafeWindow.showKeyBoard(this);
//        startActivity(new Intent(this,Main2Activity.class));
//        overridePendingTransition(R.anim.h5_slide_in_right,R.anim.h5_slide_out_left);

//          UserProtocolDialog  protocolDialog = new UserProtocolDialog(this, "Cw?",
//                    "识与近体xxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxf暗夜xxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxfxxf？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？",
//                    "同意授权", "不同意", new UserProtocolDialog.DialogOnClickListener() {
//                @Override
//                public void leftOnClickListener(BaseDialog dialog, View view) {
//                    dialog.dismiss();
//
//                }
//
//                @Override
//                public void rightOnClickListener(BaseDialog dialog, View view) {
//                    dialog.dismiss();
//
//                }
//            });
//
//            protocolDialog.show();
//        SharePopupwindow sharePopupwindow = new SharePopupwindow(this, "1|2|3|4");
//        sharePopupwindow.showPopupWindow();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("zyl",requestCode+"AAAAAAAAAAAA");
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public void oo(View view) {
        FloatViewManager.getInstance().init(getApplication());
        FloatViewWraper floatViewWraper = new FloatViewWraper(SpeakFloatView.class, getClass().getCanonicalName());
        FloatViewManager.getInstance().attach(this,floatViewWraper);




    }

    public void dettach(View view) {
    }
}
