package org.hobart.bookopen;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ScreenJudgeImageView extends ImageView {

    float ratio = 1;

    public ScreenJudgeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ratio = BookUtils.VIEW_W_H;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float height = MeasureSpec.getSize(heightMeasureSpec);
        float width = MeasureSpec.getSize(widthMeasureSpec);

        if (width / height > ratio) {
            int newWidthMeasureSpace = MeasureSpec.makeMeasureSpec((int) (height * ratio), MeasureSpec.EXACTLY);
            measure(newWidthMeasureSpace, heightMeasureSpec);
        } else if (width / height < ratio) {
            int newHeightMeasureSpace = MeasureSpec.makeMeasureSpec((int) (width / ratio), MeasureSpec.EXACTLY);
            measure(widthMeasureSpec, newHeightMeasureSpace);
        }
    }

}
