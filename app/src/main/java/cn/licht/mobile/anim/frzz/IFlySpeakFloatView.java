package cn.licht.mobile.anim.frzz;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.didichuxing.doraemonkit.config.FloatIconConfig;

import cn.licht.mobile.anim.R;
import cn.licht.mobile.anim.Utils;

public class IFlySpeakFloatView extends AbsFloatView {



    @Override
    public void onCreate(Context context) {

    }

    @Override
    public View onCreateView(Context context, FrameLayout rootView) {
        return LayoutInflater.from(context).inflate(R.layout.ifly_speak_layout,rootView,false);
    }

    @Override
    public void onViewCreated(FrameLayout rootView) {
//        View viewById = rootView.findViewById(R.id.control);
//        viewById.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(),"SS",Toast.LENGTH_LONG).show();
//            }
//        });

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void initFloatViewLayoutParams(FloatViewLayoutParams params) {
        params.x =0;
        params.y =0;
        params.width = FloatViewLayoutParams.WRAP_CONTENT;
        params.height = FloatViewLayoutParams.WRAP_CONTENT;

    }

    @Override
    public void onUp(int x, int y) {
        Log.i("zylcc","X :" +x +" y :"+y);
        int appScreenWidth = Utils.getAppScreenWidth(getContext());
        if (x > appScreenWidth/2){
            //右边
            FrameLayout.LayoutParams normalLayoutParams = getNormalLayoutParams();

        }else {
            FrameLayout.LayoutParams normalLayoutParams = getNormalLayoutParams();
        normalLayoutParams.leftMargin = 0;
        }
        super.onUp(x, y);
    }
}
