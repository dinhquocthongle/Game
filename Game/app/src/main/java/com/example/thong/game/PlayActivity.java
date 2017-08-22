package com.example.thong.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayActivity extends AppCompatActivity {
    String direction;
    public static final String EXTRA_MESSAGE = "com.example.thong.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        direction = "r";
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativeLayout);
        final Circle circle = new Circle(getApplicationContext(), BitmapFactory.decodeResource(getResources(),R.drawable.character));
        layout.addView(circle);


        final Button upButton = (Button) findViewById(R.id.UpButton);
        upButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                direction = "u";
            }
        });
        final Button downButton = (Button) findViewById(R.id.DownButton);
        downButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                direction = "d";
            }
        });
        final Button rightButton = (Button) findViewById(R.id.RightButton);
        rightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                direction = "r";
            }
        });
        final Button leftButton = (Button) findViewById(R.id.LeftButton);
        leftButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                direction = "l";
            }
        });
        final Button optionButton = (Button) findViewById(R.id.buttonOption);
        optionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PlayActivity.this, OptionActivity.class);
                String message = "ingame";
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });

        final Button quitButton = (Button) findViewById(R.id.buttonQuit);
        quitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class Circle extends SurfaceView implements SurfaceHolder.Callback {

        private Bitmap mBitmap;
        private int mBitmapHeightAndWidth, mBitmapHeightAndWidthAdj;
        private DisplayMetrics mDisplay;
        private int mDisplayWidth, mDisplayHeight;
        private float mX, mY, mDx, mDy, mRotation;
        private SurfaceHolder mSurfaceHolder;
        private Paint mPainter = new Paint();
        private Thread mThread;
        int check = 0;
        private int MOVE_STEP = 1;

        public Circle(Context context, Bitmap bitmap) {
            super(context);

            mBitmapHeightAndWidth = (int) getResources().getDimension(R.dimen.image_height);

            this.mBitmap = Bitmap.createScaledBitmap(bitmap,mBitmapHeightAndWidth,mBitmapHeightAndWidth,false);
            mBitmapHeightAndWidthAdj = mBitmapHeightAndWidth/2;
            mDisplay = new DisplayMetrics();
            PlayActivity.this.getWindowManager().getDefaultDisplay().getMetrics(mDisplay);
            mDisplayWidth = mDisplay.widthPixels;
            mDisplayHeight = mDisplay.heightPixels;

            Random r = new Random();
            mX = (float) 10;
            mY = (float) 10;
            mDx = (float) 2;
            mDx *= r.nextInt(1) == 1 ? MOVE_STEP : -1*MOVE_STEP;
            mDy = (float) 2;
            mDy *= r.nextInt(1) == 1 ? MOVE_STEP : -1*MOVE_STEP;
            mRotation = 1.0f;

            mPainter.setAntiAlias(true);

            mSurfaceHolder = getHolder();
            mSurfaceHolder.addCallback(this);
        }

        private void drawCircle(Canvas canvas) {
            canvas.drawColor(Color.DKGRAY);
            canvas.drawBitmap(mBitmap, mY, mX, mPainter);
        }

        private boolean move(String direction) {
            int height = (int) getResources().getDimension(R.dimen.layout_height);
            int width = (int) getResources().getDimension(R.dimen.layout_width);
            if (direction.equals("u"))
                mX += mDx;
            else if (direction.equals("d"))
                mX -= mDx;
            else if (direction.equals("l"))
                mY += mDy;
            else if (direction.equals("r"))
                mY -= mDy;
            if (mX < 0
                    || mX >  height - mBitmapHeightAndWidth
                    || mY < 0
                    || mY > width - mBitmapHeightAndWidth) {
                return false;
            } else {
                return true;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            final List<Rect> list = new ArrayList<Rect>();
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Canvas canvas = null;
                    while (!Thread.currentThread().isInterrupted() && move(direction) && (check == 0)) {
                        canvas = mSurfaceHolder.lockCanvas();
                        if (null != canvas) {
                            Random r = new Random();
                            if (r.nextInt(1000) < 8) {
                                list.add(new Rect());
                            }
                            List<Integer> removing_list = new ArrayList<Integer>();
                            for (int i = 0; i < list.size(); i++) {
                                if (!list.get(i).move())
                                    removing_list.add(i);
                                if ((((mX < list.get(i).RectTop && mX > list.get(i).RectBottom) || (mX < list.get(i).RectTop - mBitmapHeightAndWidth && mX > list.get(i).RectBottom - mBitmapHeightAndWidth))
                                        || ((mX > list.get(i).RectTop && mX < list.get(i).RectBottom) || (mX > list.get(i).RectTop - mBitmapHeightAndWidth && mX < list.get(i).RectBottom - mBitmapHeightAndWidth)))
                                        && (((mY > list.get(i).RectLeft && mY < list.get(i).RectRight) || ((mY+mBitmapHeightAndWidth) > list.get(i).RectLeft && (mY+mBitmapHeightAndWidth) < list.get(i).RectRight))
                                        || ((mY < list.get(i).RectLeft && mY > list.get(i).RectRight) || ((mY+mBitmapHeightAndWidth) < list.get(i).RectLeft && (mY+mBitmapHeightAndWidth) > list.get(i).RectRight))) ) {
                                    check = 1;
                                    break;
                                }
                            }
                            for (int i = 0; i < removing_list.size(); i++)
                                list.remove((int) removing_list.get(i));
                            drawCircle(canvas);
                            for (int i = 0; i < list.size(); i++) {
                                canvas.drawRect(list.get(i).RectLeft, list.get(i).RectTop, list.get(i).RectRight, list.get(i).RectBottom, list.get(i).mPainter);
                            }
                            mSurfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
            });
            mThread.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (null != mThread)
                mThread.interrupt();
        }
    }

    private class Rect {
        float RectLeft, RectTop, RectRight, RectBottom;
        String direction;
        int height = (int) getResources().getDimension(R.dimen.layout_height);
        int width = (int) getResources().getDimension(R.dimen.layout_width);
        int min = (int) getResources().getDimension(R.dimen.image_height);
        Paint mPainter = new Paint();
        public Rect() {
            Random r = new Random();
            RectLeft = (float) r.nextInt(width);
            RectTop = (float) r.nextInt(height);
            RectRight = RectLeft + (float) r.nextInt(250-min+1) + (float) min;
            RectBottom = RectTop + (float) r.nextInt(250-min+1) + (float) min;
            int direct = r.nextInt(4);
            if (direct == 0) {
                direction = "u";
                RectBottom = (float) height + (RectBottom - RectTop);
                RectTop = (float) height;
            } else if (direct == 1) {
                direction = "d";
                RectBottom = -(RectBottom - RectTop);
                RectTop = 0;
            } else if (direct == 2) {
                direction = "l";
                RectRight = (float) width;
                RectLeft = (float) width + (RectRight - RectLeft);
            } else if (direct == 3) {
                direction = "r";
                RectLeft = - (RectRight - RectLeft);
                RectRight = 0;
            }
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.brick);
            int mBitmapHeightAndWidth = (int) getResources().getDimension(R.dimen.image_height);

            bitmap = Bitmap.createScaledBitmap(bitmap,mBitmapHeightAndWidth,mBitmapHeightAndWidth,false);
            mPainter.setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        }

        public boolean move() {
            if (direction == "u") {
                RectBottom -= (float) height/800;
                RectTop -= (float) height/800;
                if (RectBottom == 0)
                    return false;
            } else if (direction == "d") {
                RectBottom += (float) height/800;
                RectTop += (float) height/800;
                if (RectTop == (float) height)
                    return false;
            } else if (direction == "l") {
                RectLeft -= (float) width/800;
                RectRight -= (float) width/800;
                if (RectRight == 0)
                    return false;
            } else if (direction == "r") {
                RectLeft += (float) width/800;
                RectRight += (float) width/800;
                if (RectLeft == (float) width)
                    return false;
            }
            return true;
        }
    }
}