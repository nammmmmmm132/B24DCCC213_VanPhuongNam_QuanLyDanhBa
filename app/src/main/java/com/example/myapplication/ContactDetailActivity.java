package com.example.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

/** Màn hình 4: Chi tiết liên hệ. */
public class ContactDetailActivity extends AppCompatActivity {

    public static final String EXTRA_CONTACT = "contact";
    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_FAVORITE = "favorite";
    public static final String EXTRA_DELETED = "deleted";

    private Contact contact;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_detail);
        Ui.applyInsets(findViewById(R.id.main));

        contact = IntentCompat.getSerializableExtra(getIntent(), EXTRA_CONTACT, Contact.class);
        position = getIntent().getIntExtra(EXTRA_POSITION, -1);
        if (contact == null || position < 0) {
            finish();
            return;
        }

        ((TextView) findViewById(R.id.tvAvatar)).setText(contact.getInitial());
        ((TextView) findViewById(R.id.tvName)).setText(contact.getFullName());
        ((TextView) findViewById(R.id.tvPhone)).setText(contact.getPhone());
        ((TextView) findViewById(R.id.tvGroup))
                .setText(getString(R.string.detail_group, contact.getGroup()));
        ((TextView) findViewById(R.id.tvGender))
                .setText(getString(R.string.detail_gender, contact.getGender()));

        String note = contact.getNote();
        ((TextView) findViewById(R.id.tvNote)).setText(
                (note == null || note.isEmpty())
                        ? getString(R.string.detail_no_note)
                        : getString(R.string.detail_note, note));

        CheckBox cbFavorite = findViewById(R.id.cbFavorite);
        cbFavorite.setChecked(contact.isFavorite());
        cbFavorite.setOnCheckedChangeListener((button, isChecked) -> {
            contact.setFavorite(isChecked);
            // Đặt kết quả ngay, nên Back hệ thống hay nút Quay lại đều cập nhật được danh sách
            Intent data = new Intent();
            data.putExtra(EXTRA_POSITION, position);
            data.putExtra(EXTRA_FAVORITE, isChecked);
            setResult(RESULT_OK, data);
        });

        Button btnCall = findViewById(R.id.btnCall);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnDelete = findViewById(R.id.btnDelete);

        btnCall.setOnClickListener(v -> {
            // Intent ngầm định mở màn hình quay số
            Intent dial = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + Uri.encode(contact.getPhone())));
            try {
                startActivity(dial);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, R.string.err_no_dialer, Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());

        btnDelete.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_title)
                .setMessage(getString(R.string.dialog_delete_message, contact.getFullName()))
                .setNegativeButton(R.string.dialog_cancel, null)
                .setPositiveButton(R.string.btn_delete, (dialog, which) -> {
                    Intent data = new Intent();
                    data.putExtra(EXTRA_POSITION, position);
                    data.putExtra(EXTRA_DELETED, true);
                    setResult(RESULT_OK, data);
                    Toast.makeText(this, R.string.toast_deleted, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .show());
    }
}
