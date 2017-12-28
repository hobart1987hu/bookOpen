package org.hobart.bookopen;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
    GridView bookGrid;
    BookShelfAdapter adapter;
    private PerspectiveView perspectiveView;
    private FrameLayout container;
    private View currentBookView;

    private ImageView iv_newContent;

    private boolean isReverse = false;
    private float x, y;
    private int width, height;
    private int coverId, innerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (FrameLayout) this.findViewById(R.id.containers);

        iv_newContent = (ImageView) findViewById(R.id.iv_newContent);
        iv_newContent.setVisibility(View.GONE);

        bookGrid = (GridView) this.findViewById(R.id.bookGrid);
        adapter = new BookShelfAdapter(this);
        container.post(new Runnable() {

            @Override
            public void run() {
                bookGrid.setAdapter(adapter);
            }
        });

        perspectiveView = new PerspectiveView(this);
        perspectiveView.setAnimationCallback(new PerspectiveView.AnimationCallback() {
            @Override
            public void onAnimationEnd() {
                if (isReverse) {
                    container.setVisibility(View.GONE);
                    iv_newContent.setVisibility(View.GONE);
                    currentBookView.setVisibility(View.VISIBLE);
                } else {
                    iv_newContent.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
                    iv_newContent.setVisibility(View.VISIBLE);
                }
            }
        });
        iv_newContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("点击了我");
                addPerspectiveView(true, currentBookView, x, y, width, height, coverId, innerId);
            }
        });
        container.addView(perspectiveView);
    }

    void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    public void addPerspectiveView(final boolean isReverse, final View view, float x, float y,
                                   int width, int height, int coverId, int innerId) {

        this.isReverse = isReverse;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.coverId = coverId;
        this.innerId = innerId;

        if (!isReverse) {
            container.setVisibility(View.VISIBLE);
        }

        Bitmap cover = BitmapFactory.decodeResource(this.getResources(), coverId);
        Bitmap innerCover = BitmapFactory.decodeResource(this.getResources(), innerId);
        currentBookView = view;
//+ BookUtils.dip2px(this, 10f)
        if (!isReverse) {
            perspectiveView.setTextures(isReverse, cover, innerCover,
                    x + BookUtils.dip2px(this, 10f),
                    x + BookUtils.dip2px(this, 10f) + width, y, y + height);
        } else {
            perspectiveView.setIsReverse(true);
        }
        if (isReverse) {
            final ObjectAnimator animator = ObjectAnimator.ofFloat(container, "alpha", 0.7f, 0f);
            animator.setDuration(300);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv_newContent.setVisibility(View.GONE);
                    perspectiveView.startAnimation();
                    animator.start();
                }
            }, 100);
        } else {
            final ObjectAnimator animator = ObjectAnimator.ofFloat(container, "alpha", 0f, 0.7f);
            animator.setDuration(300);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    perspectiveView.startAnimation();
                    animator.start();
                }
            }, 100);
        }

        if (isReverse) {

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.INVISIBLE);
                }
            }, 200);
        }

    }

    public void close() {
        perspectiveView.setVisibility(View.GONE);
    }


    @Override
    protected void onPause() {
        if (perspectiveView != null) {
            perspectiveView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
