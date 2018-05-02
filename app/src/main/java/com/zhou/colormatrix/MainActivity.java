package com.zhou.colormatrix;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.security.DomainCombiner;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private RadioGroup mRadioGroup;
    private ImageView mImageView;
    private TextView mTextView;
    private SeekBar mSeekBar;
    private int mCurrentIndex = 0;//默认的数组下标
    private float[] mColorMatrix = new float[20];//颜色矩阵一维数组
    private StringBuilder mStringBuilder = new StringBuilder();
    private DecimalFormat mDecimalFormat = new DecimalFormat("0.00");

    private Bitmap mBitmap;
    private int mBitmapWidth;
    private int mBitmapHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRadioGroup = findViewById(R.id.color_matrix_vector);
        mImageView = findViewById(R.id.color_matrix_img);
        mTextView = findViewById(R.id.color_matrix_detail);
        mSeekBar = findViewById(R.id.color_matrix_seekbar);
        resetMatrix();
        updateMatrixText();
        mSeekBar.setMax(100);
        mSeekBar.setProgress(100);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.weizhuang);
        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
        updateImgSrc();
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.color_matrix_vector_r:
                        mCurrentIndex = 0;
                        break;
                    case  R.id.color_matrix_vector_g:
                        mCurrentIndex = 6;
                        break;
                    case  R.id.color_matrix_vector_b:
                        mCurrentIndex = 12;
                        break;
                    case  R.id.color_matrix_vector_a:
                        mCurrentIndex = 18;
                        break;
                }
                  mSeekBar.setProgress((int) (mColorMatrix[mCurrentIndex] * 100));
            }
        });


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mCurrentIndex < 0) {
                    return;
                }
                mColorMatrix[mCurrentIndex] = i / 100f;
                updateMatrixText();
                updateImgSrc();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    /**
     * 更新数组显示
     */
    private void updateMatrixText() {

        mStringBuilder.delete(0, mStringBuilder.length());//清空builder
        for (int i = 0; i < 20; i++) {

            mStringBuilder.append(mDecimalFormat.format(mColorMatrix[i]) + "    ");
            if ((i + 1) % 5 == 0) {
                mStringBuilder.append("\n\n");
            }
        }
        mTextView.setText(mStringBuilder.toString());
    }


    /***
     * 重置数组
     */
    private void resetMatrix() {
        mColorMatrix = new float[20];
        mColorMatrix[0] = mColorMatrix[6] = mColorMatrix[12] = mColorMatrix[18] = 1;
    }


    /***
     * 更新img的src
     */
    private void updateImgSrc() {
        Bitmap newBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ColorMatrix matrix = new ColorMatrix(mColorMatrix);
        paint.setColorFilter(new ColorMatrixColorFilter(matrix));
        canvas.drawBitmap(mBitmap, 0, 0, paint);
        mImageView.setImageBitmap(newBitmap);
    }


}
