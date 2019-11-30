package com.rumtel.ad.helper.premovie.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ifmvo.imageloader.ILFactory;
import com.ifmvo.imageloader.LoadListener;
import com.ifmvo.imageloader.progress.LoaderOptions;

import com.rumtel.ad.TogetherAd;
import com.rumtel.ad.other.AdExtKt;
import com.rumtel.ad.other.AdNameType;

import java.util.List;

/*
 * (●ﾟωﾟ●)
 *
 * Created by Matthew_Chen on 2018/8/14.
 */
public class AdViewPreMovieGDT extends AdViewPreMovieBase {

    public AdViewPreMovieGDT(@NonNull Context context, boolean needTimer) {
        super(context, needTimer);
    }

    public AdViewPreMovieGDT(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdViewPreMovieGDT(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void start(String locationId) {

    }
}
