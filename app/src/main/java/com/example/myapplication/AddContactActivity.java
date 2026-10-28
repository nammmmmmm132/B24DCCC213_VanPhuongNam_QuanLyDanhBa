package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.regex.Pattern;

/** Màn hình 3: Thêm liên hệ. */
public class AddContactActivity extends AppCompatActivity {

    public static final String EXTRA_EXISTING_PHONES = "existing_phones";
    public static final String EXTRA_CONTACT = "contact";

    // 10 chữ số, bắt đầu bằng 0
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9}$");

    private EditText etName, etPhone, etNote;
    private Spinner spGroup;
    private RadioGroup rgGender;
    private CheckBox cbFavorite;
    private ArrayList<String> existingPhones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_contact);
        Ui.applyInsets(findViewById(R.id.main));

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etNote = findViewById(R.id.etNote);
        spGroup = findViewById(R.id.spGroup);
        rgGender = findViewById(R.id.rgGender);
        cbFavorite = findViewById(R.id.cbFavorite);
        Button btnSave = findViewById(R.id.btnSave);

        existingPhones = getIntent().getStringArrayListExtra(EXTRA_EXISTING_PHONES);
        if (existingPhones == null) existingPhones = new ArrayList<>();

        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError(getString(R.string.err_name_empty));
            etName.requestFocus();
            return;
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            etPhone.setError(getString(R.string.err_phone_invalid));
            etPhone.requestFocus();
            return;
        }
        if (existingPhones.contains(phone)) {
            etPhone.setError(getString(R.string.err_phone_exists));
            etPhone.requestFocus();
            return;
        }

        String group = (String) spGroup.getSelectedItem();
        RadioButton checked = findViewById(rgGender.getCheckedRadioButtonId());
        String gender = checked.getText().toString();
        String note = etNote.getText().toString().trim();

        Contact contact = new Contact(name, phone, group, gender, cbFavorite.isChecked(), note);

        Intent result = new Intent();
        result.putExtra(EXTRA_CONTACT, contact);
        setResult(RESULT_OK, result);
        Toast.makeText(this, R.string.toast_added, Toast.LENGTH_SHORT).show();
        finish();
    }
}
