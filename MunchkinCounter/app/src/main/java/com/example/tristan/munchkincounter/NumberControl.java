package com.example.tristan.munchkincounter;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * NumberControl is a custom UI element that contains a number
 * which can increment and decrement.
 */
public class NumberControl extends LinearLayout {
    private int numberValue;
    private TextView txtNumber;
    private ImageView upBtn;
    private ImageView downBtn;
    private boolean canGoNegative;

    public int getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(int value) {
        numberValue = value;
    }

    /**
     * Initialise the NumberControl
     */
    public NumberControl(Context context, AttributeSet attrs) {
        super(context, attrs);

        // get property values
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.NumberControl, 0, 0);
        String titleText = a.getString(R.styleable.NumberControl_titleText);
        float numberSize = a.getDimension(R.styleable.NumberControl_numberSize, 24);
        Boolean negative = a.getBoolean(R.styleable.NumberControl_negativeValue, true);
        a.recycle();

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.number_control, this);
        upBtn = (ImageView)findViewById(R.id.btn_up);
        downBtn = (ImageView)findViewById(R.id.btn_down);
        txtNumber = (TextView)findViewById(R.id.txt_number);

        // set initial values
        TextView title = (TextView)findViewById(R.id.txt_title);
        title.setText(titleText);
        txtNumber.setTextSize(numberSize);
        numberValue = 0;
        canGoNegative = negative;

        setClickListeners();
        update();
    }

    /**
     * Set the OnClickListeners of each ImageView.
     */
    private void setClickListeners() {
        upBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                increment();
            }
        });

        downBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                decrement();
            }
        });
    }

    private void increment() {
        numberValue++;
        update();
        playSound();
    }

    private void decrement() {
        numberValue--;

        if (!canGoNegative) {
            numberValue = 1;
        }
        update();
        playSound();
    }

    private void playSound() {
        SoundPlayer.playSound(R.raw.tick);
    }

    private void update() {
        txtNumber.setText(Integer.toString(numberValue));
    }
}
