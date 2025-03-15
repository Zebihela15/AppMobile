package com.sinhvien.nhom11_app_dat_tiec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Userdatabase.db";
    private static final int DATABASE_VERSION = 6; // Tăng từ 5 lên 6 để thêm cột booking_id

    // Bảng users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // Bảng restaurants
    private static final String TABLE_RESTAURANTS = "restaurants";
    private static final String COLUMN_RESTAURANT_ID = "id";
    private static final String COLUMN_RESTAURANT_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE = "image";

    // Bảng menus
    private static final String TABLE_MENUS = "menus";
    private static final String COLUMN_MENU_ID = "id";
    private static final String COLUMN_MENU_TITLE = "title";
    private static final String COLUMN_MENU_PRICE = "price";

    // Bảng services
    private static final String TABLE_SERVICES = "services";
    private static final String COLUMN_SERVICE_ID = "id";
    private static final String COLUMN_SERVICE_TITLE = "title";
    private static final String COLUMN_SERVICE_PRICE = "price";

    // Bảng bookings
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String COLUMN_BOOKING_ID = "id";
    private static final String COLUMN_BOOKING_USER_ID = "user_id";
    private static final String COLUMN_BOOKING_RESTAURANT_ID = "restaurant_id";
    private static final String COLUMN_BOOKING_TABLE_COUNT = "table_count";
    private static final String COLUMN_BOOKING_DATE = "booking_date";
    private static final String COLUMN_BOOKING_TIME = "booking_time";
    private static final String COLUMN_BOOKING_MENU_ID = "menu_id";
    private static final String COLUMN_BOOKING_TOTAL_PRICE = "total_price";

    // Bảng booking_services
    private static final String TABLE_BOOKING_SERVICES = "booking_services";
    private static final String COLUMN_BS_ID = "id";
    private static final String COLUMN_BS_BOOKING_ID = "booking_id";
    private static final String COLUMN_BS_SERVICE_ID = "service_id";

    // Bảng dishes
    private static final String TABLE_DISHES = "dishes";
    private static final String COLUMN_DISH_ID = "id";
    private static final String COLUMN_DISH_TITLE = "title";
    private static final String COLUMN_DISH_PRICE = "price";

    // Bảng thanhtoan
    private static final String TABLE_THANHTOAN = "thanhtoan";
    private static final String COLUMN_TT_ID = "MaThanhToan";
    private static final String COLUMN_TT_BOOKING_ID = "booking_id"; // Cột mới
    private static final String COLUMN_TT_HOTEN = "HoTen";
    private static final String COLUMN_TT_EMAIL = "Email";
    private static final String COLUMN_TT_SODIENTHOAI = "SoDienThoai";
    private static final String COLUMN_TT_SOLUONGBAN = "SoLuongBan";
    private static final String COLUMN_TT_NGAYDATTIEC = "NgayDatTiec";
    private static final String COLUMN_TT_GHICHU = "GhiChu";
    private static final String COLUMN_TT_TONGTIEN = "TongTien";
    private static final String COLUMN_TT_PHUONGTHUC = "PhuongThucThanhToan";
    private static final String COLUMN_TT_SOTIENDATHANHTOAN = "SoTienDaThanhToan";
    private static final String COLUMN_TT_TRANGTHAI = "TrangThaiThanhToan";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng users
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)");

        // Tạo bảng restaurants
        db.execSQL("CREATE TABLE " + TABLE_RESTAURANTS + " (" +
                COLUMN_RESTAURANT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RESTAURANT_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_IMAGE + " INTEGER)");

        // Tạo bảng menus
        db.execSQL("CREATE TABLE " + TABLE_MENUS + " (" +
                COLUMN_MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MENU_TITLE + " TEXT, " +
                COLUMN_MENU_PRICE + " REAL)");

        // Tạo bảng services
        db.execSQL("CREATE TABLE " + TABLE_SERVICES + " (" +
                COLUMN_SERVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SERVICE_TITLE + " TEXT, " +
                COLUMN_SERVICE_PRICE + " REAL)");

        // Tạo bảng bookings
        db.execSQL("CREATE TABLE " + TABLE_BOOKINGS + " (" +
                COLUMN_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BOOKING_USER_ID + " INTEGER, " +
                COLUMN_BOOKING_RESTAURANT_ID + " INTEGER, " +
                COLUMN_BOOKING_TABLE_COUNT + " INTEGER, " +
                COLUMN_BOOKING_DATE + " TEXT, " +
                COLUMN_BOOKING_TIME + " TEXT, " +
                COLUMN_BOOKING_MENU_ID + " INTEGER, " +
                COLUMN_BOOKING_TOTAL_PRICE + " REAL, " +
                "FOREIGN KEY(" + COLUMN_BOOKING_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY(" + COLUMN_BOOKING_RESTAURANT_ID + ") REFERENCES " + TABLE_RESTAURANTS + "(" + COLUMN_RESTAURANT_ID + "), " +
                "FOREIGN KEY(" + COLUMN_BOOKING_MENU_ID + ") REFERENCES " + TABLE_MENUS + "(" + COLUMN_MENU_ID + "))");

        // Tạo bảng booking_services
        db.execSQL("CREATE TABLE " + TABLE_BOOKING_SERVICES + " (" +
                COLUMN_BS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BS_BOOKING_ID + " INTEGER, " +
                COLUMN_BS_SERVICE_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_BS_BOOKING_ID + ") REFERENCES " + TABLE_BOOKINGS + "(" + COLUMN_BOOKING_ID + "), " +
                "FOREIGN KEY(" + COLUMN_BS_SERVICE_ID + ") REFERENCES " + TABLE_SERVICES + "(" + COLUMN_SERVICE_ID + "))");

        // Tạo bảng dishes
        db.execSQL("CREATE TABLE " + TABLE_DISHES + " (" +
                COLUMN_DISH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DISH_TITLE + " TEXT, " +
                COLUMN_DISH_PRICE + " REAL)");

        // Tạo bảng thanhtoan với cột booking_id
        db.execSQL("CREATE TABLE " + TABLE_THANHTOAN + " (" +
                COLUMN_TT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TT_BOOKING_ID + " INTEGER, " +
                COLUMN_TT_HOTEN + " TEXT, " +
                COLUMN_TT_EMAIL + " TEXT, " +
                COLUMN_TT_SODIENTHOAI + " TEXT, " +
                COLUMN_TT_SOLUONGBAN + " INTEGER, " +
                COLUMN_TT_NGAYDATTIEC + " TEXT, " +
                COLUMN_TT_GHICHU + " TEXT, " +
                COLUMN_TT_TONGTIEN + " REAL, " +
                COLUMN_TT_PHUONGTHUC + " TEXT, " +
                COLUMN_TT_SOTIENDATHANHTOAN + " REAL, " +
                COLUMN_TT_TRANGTHAI + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_TT_BOOKING_ID + ") REFERENCES " + TABLE_BOOKINGS + "(" + COLUMN_BOOKING_ID + "))");

        // Thêm dữ liệu ban đầu
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 6) {
            // Thêm cột booking_id vào bảng thanhtoan nếu chưa có
            try {
                db.execSQL("ALTER TABLE " + TABLE_THANHTOAN + " ADD COLUMN " + COLUMN_TT_BOOKING_ID + " INTEGER");
            } catch (Exception e) {
                // Nếu không thêm được cột (do bảng không tồn tại), tạo lại bảng thanhtoan
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_THANHTOAN);
                db.execSQL("CREATE TABLE " + TABLE_THANHTOAN + " (" +
                        COLUMN_TT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TT_BOOKING_ID + " INTEGER, " +
                        COLUMN_TT_HOTEN + " TEXT, " +
                        COLUMN_TT_EMAIL + " TEXT, " +
                        COLUMN_TT_SODIENTHOAI + " TEXT, " +
                        COLUMN_TT_SOLUONGBAN + " INTEGER, " +
                        COLUMN_TT_NGAYDATTIEC + " TEXT, " +
                        COLUMN_TT_GHICHU + " TEXT, " +
                        COLUMN_TT_TONGTIEN + " REAL, " +
                        COLUMN_TT_PHUONGTHUC + " TEXT, " +
                        COLUMN_TT_SOTIENDATHANHTOAN + " REAL, " +
                        COLUMN_TT_TRANGTHAI + " TEXT, " +
                        "FOREIGN KEY(" + COLUMN_TT_BOOKING_ID + ") REFERENCES " + TABLE_BOOKINGS + "(" + COLUMN_BOOKING_ID + "))");
            }
        }
        if (oldVersion < 5) {
            // Xóa và tạo lại toàn bộ database nếu phiên bản nhỏ hơn 5
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENUS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING_SERVICES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISHES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_THANHTOAN);
            onCreate(db);
        }
    }

    private void insertInitialData(SQLiteDatabase db) {
        // Restaurants
        insert(db, TABLE_RESTAURANTS, new String[]{COLUMN_RESTAURANT_TITLE, COLUMN_DESCRIPTION, COLUMN_IMAGE},
                new Object[]{"Diamond Place 1", "Không gian thoáng đãng, dịch vụ chu đáo.", R.drawable.dtc1});
        insert(db, TABLE_RESTAURANTS, new String[]{COLUMN_RESTAURANT_TITLE, COLUMN_DESCRIPTION, COLUMN_IMAGE},
                new Object[]{"Tràng An Palace", "Phong cách sang trọng, chuyên nghiệp.", R.drawable.dtc2});
        insert(db, TABLE_RESTAURANTS, new String[]{COLUMN_RESTAURANT_TITLE, COLUMN_DESCRIPTION, COLUMN_IMAGE},
                new Object[]{"New Orient Hotel", "Địa điểm lý tưởng để tổ chức tiệc cưới.", R.drawable.dtc3});

        // Menus
        insert(db, TABLE_MENUS, new String[]{COLUMN_MENU_TITLE, COLUMN_MENU_PRICE}, new Object[]{"Menu 1", 3550000});
        insert(db, TABLE_MENUS, new String[]{COLUMN_MENU_TITLE, COLUMN_MENU_PRICE}, new Object[]{"Menu 2", 5000000});

        // Services
        insert(db, TABLE_SERVICES, new String[]{COLUMN_SERVICE_TITLE, COLUMN_SERVICE_PRICE}, new Object[]{"Dịch vụ 1", 100000});
        insert(db, TABLE_SERVICES, new String[]{COLUMN_SERVICE_TITLE, COLUMN_SERVICE_PRICE}, new Object[]{"Dịch vụ 2", 150000});
        insert(db, TABLE_SERVICES, new String[]{COLUMN_SERVICE_TITLE, COLUMN_SERVICE_PRICE}, new Object[]{"Dịch vụ 3", 200000});

        // Dishes
        insert(db, TABLE_DISHES, new String[]{COLUMN_DISH_TITLE, COLUMN_DISH_PRICE}, new Object[]{"Salad Xá Xíu Rau Mùa Sốt Mè Rang", 450000});
        insert(db, TABLE_DISHES, new String[]{COLUMN_DISH_TITLE, COLUMN_DISH_PRICE}, new Object[]{"Súp Gà Hầm Nấm đông cô", 550000});
        insert(db, TABLE_DISHES, new String[]{COLUMN_DISH_TITLE, COLUMN_DISH_PRICE}, new Object[]{"Cá Điêu Hồng Hấp Tàu Xì", 750000});
        insert(db, TABLE_DISHES, new String[]{COLUMN_DISH_TITLE, COLUMN_DISH_PRICE}, new Object[]{"Sườn Non Hầm Pate Pháp-Bánh Mì", 680000});
        insert(db, TABLE_DISHES, new String[]{COLUMN_DISH_TITLE, COLUMN_DISH_PRICE}, new Object[]{"Cải Thìa Sốt Nấm Kim Châm", 450000});
        insert(db, TABLE_DISHES, new String[]{COLUMN_DISH_TITLE, COLUMN_DISH_PRICE}, new Object[]{"Cơm Chiên Hạnh Phúc", 600000});
        insert(db, TABLE_DISHES, new String[]{COLUMN_DISH_TITLE, COLUMN_DISH_PRICE}, new Object[]{"Rau Câu Sữa", 200000});
        insert(db, TABLE_DISHES, new String[]{COLUMN_DISH_TITLE, COLUMN_DISH_PRICE}, new Object[]{"Gỏi Bò Mỹ Vị Chua Cay", 600000});
        insert(db, TABLE_DISHES, new String[]{COLUMN_DISH_TITLE, COLUMN_DISH_PRICE}, new Object[]{"Súp Bong Bóng Cá Tam Tơ", 750000});
        insert(db, TABLE_DISHES, new String[]{COLUMN_DISH_TITLE, COLUMN_DISH_PRICE}, new Object[]{"Bacon Cuộn Tôm Nướng Sốt BBQ-Bánh Bao", 900000});
        insert(db, TABLE_DISHES, new String[]{COLUMN_DISH_TITLE, COLUMN_DISH_PRICE}, new Object[]{"Bắp Bò Hầm Rượu Vang-Bánh mì", 850000});
        insert(db, TABLE_DISHES, new String[]{COLUMN_DISH_TITLE, COLUMN_DISH_PRICE}, new Object[]{"Lẩu Bao Tử Hầm Tiêu Xanh-Bún Tươi", 1200000});
        insert(db, TABLE_DISHES, new String[]{COLUMN_DISH_TITLE, COLUMN_DISH_PRICE}, new Object[]{"Chè Nha Đam Ngũ sắc", 300000});
    }

    private void insert(SQLiteDatabase db, String table, String[] columns, Object[] values) {
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < columns.length; i++) {
            if (values[i] instanceof Integer) contentValues.put(columns[i], (Integer) values[i]);
            else if (values[i] instanceof Double) contentValues.put(columns[i], (Double) values[i]);
            else contentValues.put(columns[i], String.valueOf(values[i]));
        }
        db.insert(table, null, contentValues);
    }

    // --- Các phương thức CRUD ---

    public List<Restaurant> getAllRestaurants() {
        return queryList(this.getReadableDatabase(), TABLE_RESTAURANTS, Restaurant.class, null, null, null);
    }

    public List<Restaurant> searchRestaurants(String query) {
        return queryList(this.getReadableDatabase(), TABLE_RESTAURANTS, Restaurant.class,
                COLUMN_RESTAURANT_TITLE + " LIKE ?", new String[]{"%" + query + "%"}, null);
    }

    public boolean addUser(String name, String phone, String email, String password) {
        if (checkUserExists(email)) return false;
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUserExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT 1 FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT 1 FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                    new String[]{email, password});
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    public int getUserId(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                    new String[]{email, password});
            return cursor.moveToFirst() ? cursor.getInt(0) : -1;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    public User getUserInfo(String email) {
        return querySingle(this.getReadableDatabase(), TABLE_USERS, User.class,
                COLUMN_EMAIL + "=?", new String[]{email});
    }

    public User getUserInfoByName(String name) {
        return querySingle(this.getReadableDatabase(), TABLE_USERS, User.class,
                COLUMN_NAME + "=?", new String[]{name});
    }

    public long addBooking(int userId, int restaurantId, int tableCount, String bookingDate, String bookingTime, int menuId, double totalPrice) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOKING_USER_ID, userId);
        values.put(COLUMN_BOOKING_RESTAURANT_ID, restaurantId);
        values.put(COLUMN_BOOKING_TABLE_COUNT, tableCount);
        values.put(COLUMN_BOOKING_DATE, bookingDate);
        values.put(COLUMN_BOOKING_TIME, bookingTime);
        values.put(COLUMN_BOOKING_MENU_ID, menuId);
        values.put(COLUMN_BOOKING_TOTAL_PRICE, totalPrice);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_BOOKINGS, null, values);
        db.close();
        return result;
    }

    public boolean addBookingService(long bookingId, int serviceId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_BS_BOOKING_ID, bookingId);
        values.put(COLUMN_BS_SERVICE_ID, serviceId);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_BOOKING_SERVICES, null, values);
        db.close();
        return result != -1;
    }

    public List<Menu> getAllMenus() {
        return queryList(this.getReadableDatabase(), TABLE_MENUS, Menu.class, null, null, null);
    }

    public List<Service> getAllServices() {
        return queryList(this.getReadableDatabase(), TABLE_SERVICES, Service.class, null, null, null);
    }

    public List<Dish> getAllDishes() {
        return queryList(this.getReadableDatabase(), TABLE_DISHES, Dish.class, null, null, null);
    }

    public Booking getBookingById(long bookingId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT b.*, r." + COLUMN_RESTAURANT_TITLE + " AS restaurant_name " +
                "FROM " + TABLE_BOOKINGS + " b " +
                "JOIN " + TABLE_RESTAURANTS + " r ON b." + COLUMN_BOOKING_RESTAURANT_ID + " = r." + COLUMN_RESTAURANT_ID +
                " WHERE b." + COLUMN_BOOKING_ID + "=?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(bookingId)});
            return cursor.moveToFirst() ? new Booking(cursor) : null;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    public boolean updateBooking(long bookingId, int tableCount, String bookingDate, String bookingTime, int menuId, double totalPrice) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOKING_TABLE_COUNT, tableCount);
        values.put(COLUMN_BOOKING_DATE, bookingDate);
        values.put(COLUMN_BOOKING_TIME, bookingTime);
        values.put(COLUMN_BOOKING_MENU_ID, menuId);
        values.put(COLUMN_BOOKING_TOTAL_PRICE, totalPrice);
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.update(TABLE_BOOKINGS, values, COLUMN_BOOKING_ID + "=?", new String[]{String.valueOf(bookingId)});
        db.close();
        return rows > 0;
    }

    public void deleteBookingServices(long bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKING_SERVICES, COLUMN_BS_BOOKING_ID + "=?", new String[]{String.valueOf(bookingId)});
        db.close();
    }

    public List<Booking> getUserBookings(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT b.*, r." + COLUMN_RESTAURANT_TITLE + " AS restaurant_name " +
                "FROM " + TABLE_BOOKINGS + " b " +
                "JOIN " + TABLE_RESTAURANTS + " r ON b." + COLUMN_BOOKING_RESTAURANT_ID + " = r." + COLUMN_RESTAURANT_ID +
                " JOIN " + TABLE_USERS + " u ON b." + COLUMN_BOOKING_USER_ID + " = u." + COLUMN_ID +
                " WHERE u." + COLUMN_EMAIL + "=?";
        Cursor cursor = null;
        List<Booking> bookings = new ArrayList<>();
        try {
            cursor = db.rawQuery(query, new String[]{email});
            while (cursor.moveToNext()) {
                bookings.add(new Booking(cursor));
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return bookings;
    }

    public long addThanhToan(String hoTen, String email, String soDienThoai, int soLuongBan,
                             String ngayDatTiec, String ghiChu, double tongTien, String phuongThucThanhToan,
                             double soTienDaThanhToan, String trangThaiThanhToan, long bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TT_BOOKING_ID, bookingId);
        values.put(COLUMN_TT_HOTEN, hoTen);
        values.put(COLUMN_TT_EMAIL, email);
        values.put(COLUMN_TT_SODIENTHOAI, soDienThoai);
        values.put(COLUMN_TT_SOLUONGBAN, soLuongBan);
        values.put(COLUMN_TT_NGAYDATTIEC, ngayDatTiec);
        values.put(COLUMN_TT_GHICHU, ghiChu);
        values.put(COLUMN_TT_TONGTIEN, tongTien);
        values.put(COLUMN_TT_PHUONGTHUC, phuongThucThanhToan);
        values.put(COLUMN_TT_SOTIENDATHANHTOAN, soTienDaThanhToan);
        values.put(COLUMN_TT_TRANGTHAI, trangThaiThanhToan);
        long id = db.insert(TABLE_THANHTOAN, null, values);
        db.close();
        return id;
    }

    public ThanhToan getThanhToanByBookingId(long bookingId) {
        return querySingle(this.getReadableDatabase(), TABLE_THANHTOAN, ThanhToan.class,
                COLUMN_TT_BOOKING_ID + "=?", new String[]{String.valueOf(bookingId)});
    }

    public boolean updateThanhToan(int maThanhToan, String hoTen, String email, String soDienThoai, int soLuongBan,
                                   String ngayDatTiec, String ghiChu, double tongTien, String phuongThucThanhToan,
                                   double soTienDaThanhToan, String trangThaiThanhToan) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TT_HOTEN, hoTen);
        values.put(COLUMN_TT_EMAIL, email);
        values.put(COLUMN_TT_SODIENTHOAI, soDienThoai);
        values.put(COLUMN_TT_SOLUONGBAN, soLuongBan);
        values.put(COLUMN_TT_NGAYDATTIEC, ngayDatTiec);
        values.put(COLUMN_TT_GHICHU, ghiChu);
        values.put(COLUMN_TT_TONGTIEN, tongTien);
        values.put(COLUMN_TT_PHUONGTHUC, phuongThucThanhToan);
        values.put(COLUMN_TT_SOTIENDATHANHTOAN, soTienDaThanhToan);
        values.put(COLUMN_TT_TRANGTHAI, trangThaiThanhToan);
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.update(TABLE_THANHTOAN, values, COLUMN_TT_ID + "=?", new String[]{String.valueOf(maThanhToan)});
        db.close();
        return rows > 0;
    }

    private <T> List<T> queryList(SQLiteDatabase db, String table, Class<T> clazz, String selection, String[] selectionArgs, String orderBy) {
        List<T> result = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query(table, null, selection, selectionArgs, null, null, orderBy);
            while (cursor.moveToNext()) {
                if (clazz == Restaurant.class) result.add((T) new Restaurant(cursor));
                else if (clazz == Menu.class) result.add((T) new Menu(cursor));
                else if (clazz == Service.class) result.add((T) new Service(cursor));
                else if (clazz == Dish.class) result.add((T) new Dish(cursor));
                else if (clazz == Booking.class) result.add((T) new Booking(cursor));
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return result;
    }

    private <T> T querySingle(SQLiteDatabase db, String table, Class<T> clazz, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        try {
            cursor = db.query(table, null, selection, selectionArgs, null, null, null);
            if (cursor.moveToFirst()) {
                if (clazz == User.class) return (T) new User(cursor);
                else if (clazz == ThanhToan.class) return (T) new ThanhToan(cursor);
            }
            return null;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    // --- Các lớp mô hình ---

    public static class User {
        private int id;
        private String name, phone, email, password;

        public User(Cursor cursor) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE));
            email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
        }

        public User(int id, String name, String phone, String email, String password) {
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.password = password;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
    }

    public static class Restaurant {
        private int image;
        private String title, description;

        public Restaurant(Cursor cursor) {
            image = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
            title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_TITLE));
            description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
        }

        public Restaurant(int image, String title, String description) {
            this.image = image;
            this.title = title;
            this.description = description;
        }

        public int getImage() { return image; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
    }

    public static class Menu {
        private int id;
        private String title;
        private double price;

        public Menu(Cursor cursor) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_ID));
            title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_TITLE));
            price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MENU_PRICE));
        }

        public Menu(int id, String title, double price) {
            this.id = id;
            this.title = title;
            this.price = price;
        }

        public int getId() { return id; }
        public String getTitle() { return title; }
        public double getPrice() { return price; }
    }

    public static class Service {
        private int id;
        private String title;
        private double price;

        public Service(Cursor cursor) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_ID));
            title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_TITLE));
            price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_PRICE));
        }

        public Service(int id, String title, double price) {
            this.id = id;
            this.title = title;
            this.price = price;
        }

        public int getId() { return id; }
        public String getTitle() { return title; }
        public double getPrice() { return price; }
    }

    public static class Dish {
        private int id;
        private String title;
        private double price;

        public Dish(Cursor cursor) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DISH_ID));
            title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISH_TITLE));
            price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISH_PRICE));
        }

        public Dish(int id, String title, double price) {
            this.id = id;
            this.title = title;
            this.price = price;
        }

        public int getId() { return id; }
        public String getTitle() { return title; }
        public double getPrice() { return price; }
    }

    public static class Booking {
        private int id, userId, restaurantId, tableCount, menuId;
        private String bookingDate, bookingTime, restaurantName;
        private double totalPrice;

        public Booking(Cursor cursor) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_ID));
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_USER_ID));
            restaurantId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_RESTAURANT_ID));
            tableCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_TABLE_COUNT));
            bookingDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_DATE));
            bookingTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_TIME));
            menuId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_MENU_ID));
            totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_TOTAL_PRICE));
            restaurantName = cursor.getString(cursor.getColumnIndexOrThrow("restaurant_name"));
        }

        public Booking(int id, int userId, int restaurantId, int tableCount, String bookingDate, String bookingTime, int menuId, double totalPrice, String restaurantName) {
            this.id = id;
            this.userId = userId;
            this.restaurantId = restaurantId;
            this.tableCount = tableCount;
            this.bookingDate = bookingDate;
            this.bookingTime = bookingTime;
            this.menuId = menuId;
            this.totalPrice = totalPrice;
            this.restaurantName = restaurantName;
        }

        public int getId() { return id; }
        public int getUserId() { return userId; }
        public int getRestaurantId() { return restaurantId; }
        public int getTableCount() { return tableCount; }
        public String getBookingDate() { return bookingDate; }
        public String getBookingTime() { return bookingTime; }
        public int getMenuId() { return menuId; }
        public double getTotalPrice() { return totalPrice; }
        public String getRestaurantName() { return restaurantName; }
    }

    public static class ThanhToan {
        private int maThanhToan, soLuongBan;
        private long bookingId;
        private String hoTen, email, soDienThoai, ngayDatTiec, ghiChu, phuongThucThanhToan, trangThaiThanhToan;
        private double tongTien, soTienDaThanhToan;

        public ThanhToan(Cursor cursor) {
            maThanhToan = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TT_ID));
            bookingId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TT_BOOKING_ID));
            hoTen = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_HOTEN));
            email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_EMAIL));
            soDienThoai = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_SODIENTHOAI));
            soLuongBan = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TT_SOLUONGBAN));
            ngayDatTiec = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_NGAYDATTIEC));
            ghiChu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_GHICHU));
            tongTien = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TT_TONGTIEN));
            phuongThucThanhToan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_PHUONGTHUC));
            soTienDaThanhToan = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TT_SOTIENDATHANHTOAN));
            trangThaiThanhToan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_TRANGTHAI));
        }

        public ThanhToan(int maThanhToan, long bookingId, String hoTen, String email, String soDienThoai, int soLuongBan,
                         String ngayDatTiec, String ghiChu, double tongTien, String phuongThucThanhToan,
                         double soTienDaThanhToan, String trangThaiThanhToan) {
            this.maThanhToan = maThanhToan;
            this.bookingId = bookingId;
            this.hoTen = hoTen;
            this.email = email;
            this.soDienThoai = soDienThoai;
            this.soLuongBan = soLuongBan;
            this.ngayDatTiec = ngayDatTiec;
            this.ghiChu = ghiChu;
            this.tongTien = tongTien;
            this.phuongThucThanhToan = phuongThucThanhToan;
            this.soTienDaThanhToan = soTienDaThanhToan;
            this.trangThaiThanhToan = trangThaiThanhToan;
        }

        public int getMaThanhToan() { return maThanhToan; }
        public long getBookingId() { return bookingId; }
        public String getHoTen() { return hoTen; }
        public String getEmail() { return email; }
        public String getSoDienThoai() { return soDienThoai; }
        public int getSoLuongBan() { return soLuongBan; }
        public String getNgayDatTiec() { return ngayDatTiec; }
        public String getGhiChu() { return ghiChu; }
        public double getTongTien() { return tongTien; }
        public String getPhuongThucThanhToan() { return phuongThucThanhToan; }
        public double getSoTienDaThanhToan() { return soTienDaThanhToan; }
        public String getTrangThaiThanhToan() { return trangThaiThanhToan; }
    }
}