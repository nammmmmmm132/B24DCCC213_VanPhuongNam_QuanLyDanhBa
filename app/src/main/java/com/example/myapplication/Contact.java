package com.example.myapplication;

import java.io.Serializable;
import java.util.Locale;

/** Một liên hệ trong danh bạ. Implements Serializable để truyền qua Intent. */
public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fullName;
    private String phone;
    private String group;
    private String gender;
    private boolean favorite;
    private String note;

    public Contact(String fullName, String phone, String group,
                   String gender, boolean favorite, String note) {
        this.fullName = fullName;
        this.phone = phone;
        this.group = group;
        this.gender = gender;
        this.favorite = favorite;
        this.note = note;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    /**
     * Chữ cái đầu của TÊN (từ cuối trong họ tên), dùng làm ảnh đại diện.
     * Ví dụ "Nguyễn Văn An" -> "A". Muốn lấy chữ cái đầu của cả họ tên
     * thì đổi thành fullName.trim().charAt(0).
     */
    public String getInitial() {
        if (fullName == null) return "?";
        String n = fullName.trim();
        if (n.isEmpty()) return "?";
        String given = n.substring(n.lastIndexOf(' ') + 1);
        return given.substring(0, 1).toUpperCase(Locale.forLanguageTag("vi"));
    }
}
