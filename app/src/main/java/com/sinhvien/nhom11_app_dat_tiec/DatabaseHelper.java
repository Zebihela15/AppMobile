package com.sinhvien.nhom11_app_dat_tiec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Userdatabase.db";
    private static final int DATABASE_VERSION = 5; // Tăng từ 4 lên 5 để thêm bảng mới

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

    // Bảng dishes (mới)
    private static final String TABLE_DISHES = "dishes";
    private static final String COLUMN_DISH_ID = "id";
    private static final String COLUMN_DISH_TITLE = "title";
    private static final String COLUMN_DISH_PRICE = "price";

    // Bảng ThanhToan (mới)
    private static final String TABLE_THANHTOAN = "thanhtoan";
    private static final String COLUMN_TT_ID = "MaThanhToan";
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
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Tạo bảng restaurants
        String CREATE_RESTAURANTS_TABLE = "CREATE TABLE " + TABLE_RESTAURANTS + " (" +
                COLUMN_RESTAURANT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RESTAURANT_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_IMAGE + " INTEGER)";
        db.execSQL(CREATE_RESTAURANTS_TABLE);

        // Tạo bảng menus
        String CREATE_MENUS_TABLE = "CREATE TABLE " + TABLE_MENUS + " (" +
                COLUMN_MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MENU_TITLE + " TEXT, " +
                COLUMN_MENU_PRICE + " REAL)";
        db.execSQL(CREATE_MENUS_TABLE);

        // Tạo bảng services
        String CREATE_SERVICES_TABLE = "CREATE TABLE " + TABLE_SERVICES + " (" +
                COLUMN_SERVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SERVICE_TITLE + " TEXT, " +
                COLUMN_SERVICE_PRICE + " REAL)";
        db.execSQL(CREATE_SERVICES_TABLE);

        // Tạo bảng bookings
        String CREATE_BOOKINGS_TABLE = "CREATE TABLE " + TABLE_BOOKINGS + " (" +
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
                "FOREIGN KEY(" + COLUMN_BOOKING_MENU_ID + ") REFERENCES " + TABLE_MENUS + "(" + COLUMN_MENU_ID + "))";
        db.execSQL(CREATE_BOOKINGS_TABLE);

        // Tạo bảng booking_services
        String CREATE_BOOKING_SERVICES_TABLE = "CREATE TABLE " + TABLE_BOOKING_SERVICES + " (" +
                COLUMN_BS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BS_BOOKING_ID + " INTEGER, " +
                COLUMN_BS_SERVICE_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_BS_BOOKING_ID + ") REFERENCES " + TABLE_BOOKINGS + "(" + COLUMN_BOOKING_ID + "), " +
                "FOREIGN KEY(" + COLUMN_BS_SERVICE_ID + ") REFERENCES " + TABLE_SERVICES + "(" + COLUMN_SERVICE_ID + "))";
        db.execSQL(CREATE_BOOKING_SERVICES_TABLE);

        // Tạo bảng dishes
        String CREATE_DISHES_TABLE = "CREATE TABLE " + TABLE_DISHES + " (" +
                COLUMN_DISH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DISH_TITLE + " TEXT, " +
                COLUMN_DISH_PRICE + " REAL)";
        db.execSQL(CREATE_DISHES_TABLE);

        // Tạo bảng ThanhToan
        String CREATE_THANHTOAN_TABLE = "CREATE TABLE " + TABLE_THANHTOAN + " (" +
                COLUMN_TT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TT_HOTEN + " TEXT, " +
                COLUMN_TT_EMAIL + " TEXT, " +
                COLUMN_TT_SODIENTHOAI + " TEXT, " +
                COLUMN_TT_SOLUONGBAN + " INTEGER, " +
                COLUMN_TT_NGAYDATTIEC + " TEXT, " +
                COLUMN_TT_GHICHU + " TEXT, " +
                COLUMN_TT_TONGTIEN + " REAL, " +
                COLUMN_TT_PHUONGTHUC + " TEXT, " +
                COLUMN_TT_SOTIENDATHANHTOAN + " REAL, " +
                COLUMN_TT_TRANGTHAI + " TEXT)";
        db.execSQL(CREATE_THANHTOAN_TABLE);

        // Thêm dữ liệu ban đầu
        insertInitialRestaurants(db);
        insertInitialMenus(db);
        insertInitialServices(db);
        insertInitialDishes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {  // Cập nhật điều kiện để bao gồm version mới
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

    // Thêm dữ liệu ban đầu cho bảng restaurants
    private void insertInitialRestaurants(SQLiteDatabase db) {
        insertRestaurant(db, "Diamond Place 1", "Không gian thoáng đãng, dịch vụ chu đáo.", R.drawable.dtc1);
        insertRestaurant(db, "Tràng An Palace", "Phong cách sang trọng, chuyên nghiệp.", R.drawable.dtc2);
        insertRestaurant(db, "New Orient Hotel", "Địa điểm lý tưởng để tổ chức tiệc cưới.", R.drawable.dtc3);
    }

    private void insertRestaurant(SQLiteDatabase db, String title, String description, int image) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESTAURANT_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_IMAGE, image);
        db.insert(TABLE_RESTAURANTS, null, values);
    }

    // Thêm dữ liệu ban đầu cho bảng menus
    private void insertInitialMenus(SQLiteDatabase db) {
        insertMenu(db, "Menu 1", 3550000);
        insertMenu(db, "Menu 2", 5000000);
    }

    private void insertMenu(SQLiteDatabase db, String title, double price) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MENU_TITLE, title);
        values.put(COLUMN_MENU_PRICE, price);
        db.insert(TABLE_MENUS, null, values);
    }

    // Thêm dữ liệu ban đầu cho bảng services
    private void insertInitialServices(SQLiteDatabase db) {
        insertService(db, "Dịch vụ 1", 100000);
        insertService(db, "Dịch vụ 2", 150000);
        insertService(db, "Dịch vụ 3", 200000);
    }

    private void insertService(SQLiteDatabase db, String title, double price) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SERVICE_TITLE, title);
        values.put(COLUMN_SERVICE_PRICE, price);
        db.insert(TABLE_SERVICES, null, values);
    }

    // Thêm dữ liệu ban đầu cho bảng dishes
    private void insertInitialDishes(SQLiteDatabase db) {
        insertDish(db, "Salad Xá Xíu Rau Mùa Sốt Mè Rang", 450000);
        insertDish(db, "Súp Gà Hầm Nấm đông cô", 550000);
        insertDish(db, "Cá Điêu Hồng Hấp Tàu Xì", 750000);
        insertDish(db, "Sườn Non Hầm Pate Pháp-Bánh Mì", 680000);
        insertDish(db, "Cải Thìa Sốt Nấm Kim Châm", 450000);
        insertDish(db, "Cơm Chiên Hạnh Phúc", 600000);
        insertDish(db, "Rau Câu Sữa", 200000);
        insertDish(db, "Gỏi Bò Mỹ Vị Chua Cay", 600000);
        insertDish(db, "Súp Bong Bóng Cá Tam Tơ", 750000);
        insertDish(db, "Bacon Cuộn Tôm Nướng Sốt BBQ-Bánh Bao", 900000);
        insertDish(db, "Bắp Bò Hầm Rượu Vang-Bánh mì", 850000);
        insertDish(db, "Lẩu Bao Tử Hầm Tiêu Xanh-Bún Tươi", 1200000);
        insertDish(db, "Chè Nha Đam Ngũ sắc", 300000);
    }

    private void insertDish(SQLiteDatabase db, String title, double price) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DISH_TITLE, title);
        values.put(COLUMN_DISH_PRICE, price);
        db.insert(TABLE_DISHES, null, values);
    }

    // Lấy tất cả nhà hàng
    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurantList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESTAURANTS, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
                restaurantList.add(new Restaurant(image, title, description));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return restaurantList;
    }

    // Tìm kiếm nhà hàng theo title
    public List<Restaurant> searchRestaurants(String query) {
        List<Restaurant> restaurantList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_RESTAURANT_TITLE + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"};
        Cursor cursor = db.query(TABLE_RESTAURANTS, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
                restaurantList.add(new Restaurant(image, title, description));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return restaurantList;
    }

    // Thêm người dùng mới
    public boolean addUser(String name, String phone, String email, String password) {
        if (checkUserExists(email)) {
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Kiểm tra email đã tồn tại
    public boolean checkUserExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Kiểm tra đăng nhập
    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean exists = false;
        try {
            String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?";
            cursor = db.rawQuery(query, new String[]{email, password});
            exists = cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e("DB_ERROR", "Lỗi truy vấn database: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return exists;
    }

    // Lấy ID người dùng
    public int getUserId(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return userId;
    }

    // Hiển thị tất cả người dùng (debug)
    public void showAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String phone = cursor.getString(2);
                String email = cursor.getString(3);
                String password = cursor.getString(4);
                Log.d("USER_DATA", "ID: " + id + ", Name: " + name + ", Phone: " + phone + ", Email: " + email + ", Password: " + password);
            } while (cursor.moveToNext());
        } else {
            Log.d("USER_DATA", "Không có tài khoản nào trong database.");
        }
        cursor.close();
        db.close();
    }

    // Phương thức lấy thông tin người dùng dựa trên email
    public User getUserInfo(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
            );
        }
        cursor.close();
        db.close();
        return user;
    }

    public User getUserInfoByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_NAME + "=?", new String[]{name});
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
            );
        }
        cursor.close();
        db.close();
        return user;
    }

    // Phương thức thêm một booking mới
    public long addBooking(int userId, int restaurantId, int tableCount, String bookingDate, String bookingTime, int menuId, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOKING_USER_ID, userId);
        values.put(COLUMN_BOOKING_RESTAURANT_ID, restaurantId);
        values.put(COLUMN_BOOKING_TABLE_COUNT, tableCount);
        values.put(COLUMN_BOOKING_DATE, bookingDate);
        values.put(COLUMN_BOOKING_TIME, bookingTime);
        values.put(COLUMN_BOOKING_MENU_ID, menuId);
        values.put(COLUMN_BOOKING_TOTAL_PRICE, totalPrice);

        long bookingId = db.insert(TABLE_BOOKINGS, null, values);
        db.close();
        return bookingId;
    }

    // Phương thức thêm dịch vụ vào booking
    public boolean addBookingService(long bookingId, int serviceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BS_BOOKING_ID, bookingId);
        values.put(COLUMN_BS_SERVICE_ID, serviceId);

        long result = db.insert(TABLE_BOOKING_SERVICES, null, values);
        db.close();
        return result != -1;
    }

    // Lấy tất cả menu
    public List<Menu> getAllMenus() {
        List<Menu> menuList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MENUS, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_TITLE));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MENU_PRICE));
                menuList.add(new Menu(id, title, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return menuList;
    }

    // Lấy tất cả dịch vụ
    public List<Service> getAllServices() {
        List<Service> serviceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SERVICES, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_TITLE));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_PRICE));
                serviceList.add(new Service(id, title, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return serviceList;
    }

    // Lấy tất cả món ăn
    public List<Dish> getAllDishes() {
        List<Dish> dishList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DISHES, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DISH_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISH_TITLE));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISH_PRICE));
                dishList.add(new Dish(id, title, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dishList;
    }

    // Thêm một thanh toán mới
    public long addThanhToan(String hoTen, String email, String soDienThoai, int soLuongBan,
                             String ngayDatTiec, String ghiChu, double tongTien,
                             String phuongThucThanhToan, double soTienDaThanhToan, String trangThaiThanhToan) {
        SQLiteDatabase db = this.getWritableDatabase();
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

        long id = db.insert(TABLE_THANHTOAN, null, values);
        db.close();
        return id;
    }

    // Lớp User
    public static class User {
        private int id;
        private String name;
        private String phone;
        private String email;
        private String password;

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

    // Lớp Restaurant
    public static class Restaurant {
        private int image;
        private String title;
        private String description;

        public Restaurant(int image, String title, String description) {
            this.image = image;
            this.title = title;
            this.description = description;
        }

        public int getImage() { return image; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
    }

    // Lớp Menu
    public static class Menu {
        private int id;
        private String title;
        private double price;

        public Menu(int id, String title, double price) {
            this.id = id;
            this.title = title;
            this.price = price;
        }

        public int getId() { return id; }
        public String getTitle() { return title; }
        public double getPrice() { return price; }
    }

    // Lớp Service
    public static class Service {
        private int id;
        private String title;
        private double price;

        public Service(int id, String title, double price) {
            this.id = id;
            this.title = title;
            this.price = price;
        }

        public int getId() { return id; }
        public String getTitle() { return title; }
        public double getPrice() { return price; }
    }

    // Lớp Dish
    public static class Dish {
        private int id;
        private String title;
        private double price;

        public Dish(int id, String title, double price) {
            this.id = id;
            this.title = title;
            this.price = price;
        }

        public int getId() { return id; }
        public String getTitle() { return title; }
        public double getPrice() { return price; }
    }

    // Lớp ThanhToan
    public static class ThanhToan {
        private int maThanhToan;
        private String hoTen;
        private String email;
        private String soDienThoai;
        private int soLuongBan;
        private String ngayDatTiec;
        private String ghiChu;
        private double tongTien;
        private String phuongThucThanhToan;
        private double soTienDaThanhToan;
        private String trangThaiThanhToan;

        public ThanhToan(int maThanhToan, String hoTen, String email, String soDienThoai,
                         int soLuongBan, String ngayDatTiec, String ghiChu, double tongTien,
                         String phuongThucThanhToan, double soTienDaThanhToan, String trangThaiThanhToan) {
            this.maThanhToan = maThanhToan;
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