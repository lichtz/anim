package cn.licht.mobile.anim.frzz;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FloatViewFragmentLifecycleCallbacks extends FragmentManager.FragmentLifecycleCallbacks {
    @Override
    public void onFragmentAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
        super.onFragmentAttached(fm, f, context);
    }

    @Override
    public void onFragmentDetached(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentDetached(fm, f);

    }
}
