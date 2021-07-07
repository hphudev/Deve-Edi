package com.example.RestaurantManagement;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.RestaurantManagement.R;

public class DialogStringTwoContentCustom {
    private String Title, Turial, HintEditTextOne, HintEditTextTwo, TextEditTextOne, TextEditTextTwo;
    private int Position;
    private Context context;
    private Dialog_string_two_content_interface dialog_string_two_content_interface;

    public DialogStringTwoContentCustom(String title, String turial, String hintEditTextOne, String hintEditTextTwo, String textEditTextOne, String textEditTextTwo, int position, Context context, Dialog_string_two_content_interface dialog_string_two_content_interface) {
        Title = title;
        Turial = turial;
        HintEditTextOne = hintEditTextOne;
        HintEditTextTwo = hintEditTextTwo;
        TextEditTextOne = textEditTextOne;
        TextEditTextTwo = textEditTextTwo;
        Position = position;
        this.context = context;
        this.dialog_string_two_content_interface = dialog_string_two_content_interface;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTurial() {
        return Turial;
    }

    public void setTurial(String turial) {
        Turial = turial;
    }

    public String getHintEditTextOne() {
        return HintEditTextOne;
    }

    public void setHintEditTextOne(String hintEditTextOne) {
        HintEditTextOne = hintEditTextOne;
    }

    public String getHintEditTextTwo() {
        return HintEditTextTwo;
    }

    public void setHintEditTextTwo(String hintEditTextTwo) {
        HintEditTextTwo = hintEditTextTwo;
    }

    public String getTextEditTextOne() {
        return TextEditTextOne;
    }

    public void setTextEditTextOne(String textEditTextOne) {
        TextEditTextOne = textEditTextOne;
    }

    public String getTextEditTextTwo() {
        return TextEditTextTwo;
    }

    public void setTextEditTextTwo(String textEditTextTwo) {
        TextEditTextTwo = textEditTextTwo;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Dialog_string_two_content_interface getDialog_string_interface() {
        return dialog_string_two_content_interface;
    }

    public void setDialog_string_interface(Dialog_string_two_content_interface dialog_string_two_content_interface) {
        this.dialog_string_two_content_interface = dialog_string_two_content_interface;
    }

    public void ShowDialogString(int gravity)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_string_two_content);

        Window window = dialog.getWindow();
        if (window == null)
        {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(Gravity.CENTER == gravity);

        TextView textViewTitle = dialog.findViewById(R.id.tvTitle_dialog_string);
        TextView textViewTurial = dialog.findViewById(R.id.tvTurial_dialog_string);
        EditText editTextContentOne = dialog.findViewById(R.id.edtContentOne_dialog_string);
        EditText editTextContentTwo = dialog.findViewById(R.id.edtContentTwo_dialog_string);
        Button btnCancel = dialog.findViewById(R.id.btnCancle_dialog_string);
        Button btnSave = dialog.findViewById(R.id.btnSave_dialog_string);

        textViewTitle.setText(getTitle());
        textViewTurial.setText(getTurial());
        editTextContentOne.setHint(getHintEditTextOne());
        editTextContentTwo.setHint(getHintEditTextTwo());
        editTextContentOne.selectAll();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!(editTextContentOne.getText().toString().equals("") && !editTextContentTwo.getText().toString().equals("")))
                    && (editTextContentOne.getText().toString().equals(editTextContentTwo.getText().toString())))
                {
                    dialog_string_two_content_interface.onButtonSaveClicked(Position, editTextContentOne.getText().toString(), editTextContentTwo.getText().toString());
                    dialog.dismiss();
                }
                else
                    dialog.show();
            }
        });
        dialog.show();
    }

}
