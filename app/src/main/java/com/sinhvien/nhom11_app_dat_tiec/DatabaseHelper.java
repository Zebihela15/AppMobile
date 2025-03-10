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
    private static final int DATABASE_VERSION = 4; // Tăng từ 3 lên 4 để thêm các bảng mới

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

    // Bảng menus (mới)
    private static final String TABLE_MENUS = "menus";
    private static final String COLUMN_MENU_ID = "id";
    private static final String COLUMN_MENU_TITLE = "title";
    private static final String COLUMN_MENU_PRICE = "price";

    // Bảng services (mới)
    private static final String TABLE_SERVICES = "services";
    private static final String COLUMN_SERVICE_ID = "id";
    private static final String COLUMN_SERVICE_TITLE = "title";
    private static final String COLUMN_SERVICE_PRICE = "price";

    // Bảng bookings (mới)
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String COLUMN_BOOKING_ID = "id";
    private static final String COLUMN_BOOKING_USER_ID = "user_id";
    private static final String COLUMN_BOOKING_RESTAURANT_ID = "restaurant_id";
    private static final String COLUMN_BOOKING_TABLE_COUNT = "table_count";
    private static final String COLUMN_BOOKING_DATE = "booking_date";
    private static final String COLUMN_BOOKING_TIME = "booking_time";
    private static final String COLUMN_BOOKING_MENU_ID = "menu_id";
    private static final String COLUMN_BOOKING_TOTAL_PRICE = "total_price";

    // Bảng booking_services (mới)
    private static final String TABLE_BOOKING_SERVICES = "booking_services";
    private static final String COLUMN_BS_ID = "id";
    private static final String COLUMN_BS_BOOKING_ID = "booking_id";
    private static final String COLUMN_BS_SERVICE_ID = "service_id";

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
                COLUMN_MENU_PRICE + " REAL)"; // REAL để lưu giá tiền dạng số thực
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

        // Thêm dữ liệu ban đầu
        insertInitialRestaurants(db);
        insertInitialMenus(db);
        insertInitialServices(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            // Xóa các bảng cũ và tạo lại nếu version nhỏ hơn 4
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENUS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING_SERVICES);
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
        return bookingId; // Trả về ID của booking vừa thêm
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

    // Lớp User để lưu thông tin người dùng
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
}