package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import java.util.ArrayList;

/** Màn hình 2: Danh sách liên hệ + thống kê. Dữ liệu lưu trong ArrayList (RAM). */
public class ContactListActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "username";

    private final ArrayList<Contact> contacts = new ArrayList<>();
    private ContactAdapter adapter;
    private TextView tvStats, tvEmpty;
    private ListView lvContacts;

    // Nhận liên hệ mới từ AddContactActivity
    private final ActivityResultLauncher<Intent> addLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Contact c = IntentCompat.getSerializableExtra(
                            result.getData(), AddContactActivity.EXTRA_CONTACT, Contact.class);
                    if (c != null) {
                        contacts.add(c);
                        refresh();
                        lvContacts.setSelection(contacts.size() - 1);
                    }
                }
            });

    // Nhận kết quả (đổi yêu thích / xóa) từ ContactDetailActivity
    private final ActivityResultLauncher<Intent> detailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                Intent data = result.getData();
                if (result.getResultCode() != RESULT_OK || data == null) return;

                int pos = data.getIntExtra(ContactDetailActivity.EXTRA_POSITION, -1);
                if (pos < 0 || pos >= contacts.size()) return;

                if (data.getBooleanExtra(ContactDetailActivity.EXTRA_DELETED, false)) {
                    contacts.remove(pos);
                } else {
                    boolean fav = data.getBooleanExtra(
                            ContactDetailActivity.EXTRA_FAVORITE, contacts.get(pos).isFavorite());
                    contacts.get(pos).setFavorite(fav);
                }
                refresh();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_list);
        Ui.applyInsets(findViewById(R.id.main));

        TextView tvGreeting = findViewById(R.id.tvGreeting);
        tvStats = findViewById(R.id.tvStats);
        tvEmpty = findViewById(R.id.tvEmpty);
        lvContacts = findViewById(R.id.lvContacts);
        Button btnAdd = findViewById(R.id.btnAdd);

        String username = getIntent().getStringExtra(EXTRA_USERNAME);
        if (username != null) {
            tvGreeting.setText(getString(R.string.list_greeting, username));
        }

        // Dữ liệu mẫu ban đầu
        contacts.add(new Contact("Nguyễn Văn An", "0901234567", "Bạn bè", "Nam", false, ""));
        contacts.add(new Contact("Trần Thị Bình", "0912345678", "Gia đình", "Nữ", true, ""));
        contacts.add(new Contact("Lê Minh Châu", "0987654321", "Công việc", "Nữ", false, ""));

        adapter = new ContactAdapter(this, contacts);
        lvContacts.setAdapter(adapter);

        lvContacts.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ContactDetailActivity.class);
            intent.putExtra(ContactDetailActivity.EXTRA_CONTACT, contacts.get(position));
            intent.putExtra(ContactDetailActivity.EXTRA_POSITION, position);
            detailLauncher.launch(intent);
        });

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddContactActivity.class);
            // Gửi danh sách số điện thoại hiện có để màn Thêm kiểm tra trùng
            ArrayList<String> phones = new ArrayList<>();
            for (Contact c : contacts) phones.add(c.getPhone());
            intent.putStringArrayListExtra(AddContactActivity.EXTRA_EXISTING_PHONES, phones);
            addLauncher.launch(intent);
        });

        refresh();
    }

    /** Cập nhật danh sách + khối thống kê. */
    private void refresh() {
        adapter.notifyDataSetChanged();

        int favorites = 0;
        for (Contact c : contacts) {
            if (c.isFavorite()) favorites++;
        }
        tvStats.setText(getString(R.string.list_stats, contacts.size(), favorites));
        tvEmpty.setVisibility(contacts.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
