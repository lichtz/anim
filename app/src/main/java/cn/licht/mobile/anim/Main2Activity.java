package cn.licht.mobile.anim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TextView viewById = findViewById(R.id.scrollView);
        viewById.setText("？？？11今夜无眠正式脱离欧亚大陆\n sa 变的\n 轻轻对你唱歌XXXXXX\n\n\n我也想照料|||\n 咳咳咳咳咳sa 变的\n 轻轻对你唱歌XXXXXX\n\n\n我也想照料|||\n 咳咳 轻轻对你唱歌XXXXXX\n\n\n我也想照料|||\n 咳咳咳咳咳sa 变的\n 轻轻对你唱歌XXXXXX" +
                "？？？11今夜无眠正式脱离欧亚大陆\n sa 变的\n 轻轻对你唱歌XXXXXX\n\n\n我也想照料|||\n 咳咳咳咳咳sa 变的\n 轻" +
                "？？？11今夜无眠正式脱离欧亚大陆\n sa 变的\n 轻轻对你唱歌XXXXXX\n\n\n我也想照料|||\n 咳咳咳咳咳sa 变的\n 轻？？？11今夜无眠正式脱离欧亚大陆\n sa 变的\n 轻轻对你唱歌XXXXXX\n\n\n我也想照料|||\n 咳咳咳咳咳sa 变的\n 轻？？？11今夜无眠正式脱离欧亚大陆\n sa 变的\n 轻轻对你唱歌XXXXXX\n\n\n我也想照料|||\n 咳咳咳咳咳sa 变的\n 轻" +
                "？？？11今夜无眠正式脱离欧亚大陆\n sa 变的\n 轻轻对你唱歌XXXXXX\n\n\n我也想照料|||\n 咳咳咳咳咳sa 变的\n 轻" +
                "？？？11今夜无眠正式脱离欧亚大陆\n sa 变的\n 轻轻对你唱歌XXXXXX\n\n\n我也想照料|||\n 咳咳咳咳咳sa 变的\n 轻" +
                "？？？11今夜无眠正式脱离欧亚大陆\n sa 变的\n 轻轻对你唱歌XXXXXX\n\n\n我也想照料|||\n 咳咳咳咳咳sa 变的\n 轻" +
                "？？？11今夜无眠正式脱离欧亚大陆\n sa 变的\n 轻轻对你唱歌XXXXXX\n\n\n我也想照料|||\n 咳咳咳咳咳sa 变的\n 轻" +
                "？？？11今夜无眠正式脱离欧亚大陆\n sa 变的\n 轻轻对你唱歌XXXXXX\n\n\n我也想照料|||\n 咳咳咳咳咳sa 变的\n 轻" +
                "？？？11今夜无眠正式脱离欧亚大陆\n sa 变的\n 轻轻对你唱歌XXXXXX\n\n\n我也想照料|||\n 咳咳咳咳咳sa 变的\n 轻" +
                "？？？11今夜无眠正式脱离欧亚大陆\n sa 变的\n 轻轻对你唱歌XXXXXX\n\n\n我也想照料|||\n 咳咳咳咳咳sa 变的\n 轻？？？11今夜无眠正式脱离欧亚大陆\n sa 变的\n 轻轻对你唱歌XXXXXX\n\n\n我也想照料|||\n 咳咳咳咳咳sa 变的\n 轻" +
                "？？？11今夜无眠正式脱离欧亚大陆\n sa 变的\n 轻轻对你唱歌XXXXXX\n\n\n我也想照料|||\n 咳咳咳咳咳sa 变的\n 轻" +
                "？？？11今夜无眠正式脱离欧亚大陆\n sa 变的\n 轻轻对你唱歌XXXXXX\n\n\n我也想照料|||\n 咳咳咳咳咳sa 变的\n 轻    ？？？11今夜无眠正式脱离欧亚大陆\n sa 变的\n 轻轻对你唱歌XXXXXX\n\n\n我也想照料|||\n 咳咳咳咳咳sa 变的\n 轻" +
                "" +
                "" +
                "" +
                "" +
                "\n\n\n我咳咳咳");


    }

    public void cvv(View view) {
        startActivity(new Intent(this, Main3Activity.class));
        finish(false);
        overridePendingTransition(R.anim.h5_slide_in_right, R.anim.h5_slide_out_left);

    }

    private void finish(boolean b) {
        super.finish();
        if (b) {
        }

    }

    @Override
    public void finish() {
        finish(true);
    }
}
