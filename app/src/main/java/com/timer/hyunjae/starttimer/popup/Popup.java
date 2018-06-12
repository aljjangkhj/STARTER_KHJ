package com.timer.hyunjae.starttimer.popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timer.hyunjae.starttimer.R;
import com.timer.hyunjae.starttimer.utils.AutoScreen;


public class Popup extends Dialog
{
    static Context mContext = null;
    private String TAG;

    private TextView tv_popup_msg = null;

    private LinearLayout layout_popup_1btn = null;
    private FrameLayout btn_popup_1btn_ok = null;
    private TextView tv_popup_1btn_ok = null;

    private LinearLayout layout_popup_2btn = null;
    private FrameLayout btn_popup_2btn_cancel = null;
    private TextView tv_popup_2btn_cancel = null;
    private FrameLayout btn_popup_2btn_ok = null;
    private TextView tv_popup_2btn_ok = null;

    public onClick Cancel_Click = null;
    public onClick OK_Click = null;

    public abstract interface onClick
    {
        public abstract void onClick();
    }

    public Popup(Context context, String title, String msg, String Cancel, String OK)
    {

        super(context);
        // TODO Auto-generated constructor stub
        TAG = "Popup";
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup);

//        LayoutInflater inflater = getLayoutInflater();
//        View popup_view = inflater.inflate(R.layout.popup, null);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setCancelable(false);
        AutoScreen.Adjust(mContext, getWindow().getDecorView(), 1);

        tv_popup_msg = (TextView)this.findViewById(R.id.tv_popup_msg);

        layout_popup_1btn = (LinearLayout)this.findViewById(R.id.layout_popup_1btn);
        btn_popup_1btn_ok = (FrameLayout)this.findViewById(R.id.btn_popup_1btn_ok);
        tv_popup_1btn_ok = (TextView)this.findViewById(R.id.tv_popup_1btn_ok);

        layout_popup_2btn = (LinearLayout)this.findViewById(R.id.layout_popup_2btn);
        btn_popup_2btn_cancel = (FrameLayout)this.findViewById(R.id.btn_popup_2btn_cancel);
        tv_popup_2btn_cancel = (TextView)this.findViewById(R.id.tv_popup_2btn_cancel);
        btn_popup_2btn_ok = (FrameLayout)this.findViewById(R.id.btn_popup_2btn_ok);
        tv_popup_2btn_ok = (TextView)this.findViewById(R.id.tv_popup_2btn_ok);

        btn_popup_1btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Popup.this.dismiss();

                if(OK_Click != null)
                {
                    OK_Click.onClick();
                }
            }
        });

        btn_popup_2btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Popup.this.dismiss();

                if(OK_Click != null)
                {
                    OK_Click.onClick();
                }
            }
        });

        btn_popup_2btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Popup.this.dismiss();

                if(Cancel_Click != null)
                {
                    Cancel_Click.onClick();
                }

            }
        });

        tv_popup_msg.setText(msg);

        if(Cancel.equals(""))
        {
            layout_popup_1btn.setVisibility(View.VISIBLE);
            layout_popup_2btn.setVisibility(View.GONE);
            tv_popup_1btn_ok.setText(OK);
        }
        else
        {
            layout_popup_1btn.setVisibility(View.GONE);
            layout_popup_2btn.setVisibility(View.VISIBLE);
            tv_popup_2btn_ok.setText(OK);
            tv_popup_2btn_cancel.setText(Cancel);
        }
    }

    @Override
    public void show()
    {
        super.show();
    }

}