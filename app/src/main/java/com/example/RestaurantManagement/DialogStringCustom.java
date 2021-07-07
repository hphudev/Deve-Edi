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

public class DialogStringCustom{
    private String Title, Turial, HintEditText, TextEditText;
    private int Position;
    private Context context;
    private Dialog_string_interface dialog_string_interface;

    public DialogStringCustom(Context context, Dialog_string_interface dialog_string_interface, String title, String turial, String hintEditText, String textEditText, int position) {
        Title = title;
        Turial = turial;
        HintEditText = hintEditText;
        this.context = context;
        this.dialog_string_interface = (Dialog_string_interface)context;
        TextEditText = textEditText;
        Position = position;
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

    public String getHintEditText() {
        return HintEditText;
    }

    public void setHintEditText(String hintEditText) {
        HintEditText = hintEditText;
    }

    public String getTextEditText() {
        return TextEditText;
    }

    public void setTextEditText(String textEditText) {
        TextEditText = textEditText;
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

    public Dialog_string_interface getDialog_string_interface() {
        return dialog_string_interface;
    }

    public void setDialog_string_interface(Dialog_string_interface dialog_string_interface) {
        this.dialog_string_interface = dialog_string_interface;
    }

    public void ShowDialogString(int gravity)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_string);

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
        EditText editTextContent = dialog.findViewById(R.id.edtContent_dialog_string);
        Button btnCancel = dialog.findViewById(R.id.btnCancle_dialog_string);
        Button btnSave = dialog.findViewById(R.id.btnSave_dialog_string);

        textViewTitle.setText(getTitle());
        textViewTurial.setText(getTurial());
        editTextContent.setHint(getHintEditText());
        editTextContent.setText(getTextEditText());
        editTextContent.selectAll();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextContent.getText().equals(""))
                    dialog_string_interface.onButtonSaveClicked(Position, editTextContent.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
