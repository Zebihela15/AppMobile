package com.sinhvien.nhom11_app_dat_tiec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Userdatabase.db";
    private static final int DATABASE_VERSION = 9;

    // Bảng users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_EMAIL = "email";

    // Bảng restaurants
    private static final String TABLE_RESTAURANTS = "restaurants";
    private static final String COLUMN_RESTAURANT_ID = "id";
    private static final String COLUMN_RESTAURANT_TITLE = "title";
    private static final String COLUMN_RESTAURANT_DESCRIPTION = "restaurant_description";
    private static final String COLUMN_IMAGE = "image";

    // Bảng menus
    private static final String TABLE_MENUS = "menus";
    private static final String COLUMN_MENU_ID = "id";
    private static final String COLUMN_MENU_TITLE = "title";
    private static final String COLUMN_MENU_PRICE = "price";
    private static final String COLUMN_MENU_DESCRIPTION="menu_description";

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
    private static final String COLUMN_BS_BOOKING_ID = "booking_id";
    private static final String COLUMN_BS_SERVICE_ID = "service_id";

    // Bảng ThanhToan
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
    private static final String COLUMN_TT_BOOKING_ID = "booking_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " TEXT UNIQUE, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE)";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_RESTAURANTS_TABLE = "CREATE TABLE " + TABLE_RESTAURANTS + " (" +
                COLUMN_RESTAURANT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RESTAURANT_TITLE + " TEXT, " +
                COLUMN_RESTAURANT_DESCRIPTION + " TEXT, " +
                COLUMN_IMAGE + " TEXT)";
        db.execSQL(CREATE_RESTAURANTS_TABLE);

        String CREATE_MENUS_TABLE = "CREATE TABLE " + TABLE_MENUS + " (" +
                COLUMN_MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MENU_TITLE + " TEXT, " +
                COLUMN_MENU_PRICE + " REAL, "+
                COLUMN_MENU_DESCRIPTION+" TEXT)";
        db.execSQL(CREATE_MENUS_TABLE);

        String CREATE_SERVICES_TABLE = "CREATE TABLE " + TABLE_SERVICES + " (" +
                COLUMN_SERVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SERVICE_TITLE + " TEXT, " +
                COLUMN_SERVICE_PRICE + " REAL)";
        db.execSQL(CREATE_SERVICES_TABLE);

        String CREATE_BOOKINGS_TABLE = "CREATE TABLE " + TABLE_BOOKINGS + " (" +
                COLUMN_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BOOKING_USER_ID + " TEXT, " +
                COLUMN_BOOKING_RESTAURANT_ID + " INTEGER, " +
                COLUMN_BOOKING_TABLE_COUNT + " INTEGER, " +
                COLUMN_BOOKING_DATE + " TEXT, " +
                COLUMN_BOOKING_TIME + " TEXT, " +
                COLUMN_BOOKING_MENU_ID + " INTEGER, " +
                COLUMN_BOOKING_TOTAL_PRICE + " REAL, " +
                "FOREIGN KEY(" + COLUMN_BOOKING_RESTAURANT_ID + ") REFERENCES " + TABLE_RESTAURANTS + "(" + COLUMN_RESTAURANT_ID + "), " +
                "FOREIGN KEY(" + COLUMN_BOOKING_MENU_ID + ") REFERENCES " + TABLE_MENUS + "(" + COLUMN_MENU_ID + "))";
        db.execSQL(CREATE_BOOKINGS_TABLE);

        String CREATE_BOOKING_SERVICES_TABLE = "CREATE TABLE " + TABLE_BOOKING_SERVICES + " (" +
                COLUMN_BS_BOOKING_ID + " INTEGER, " +
                COLUMN_BS_SERVICE_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_BS_BOOKING_ID + ") REFERENCES " + TABLE_BOOKINGS + "(" + COLUMN_BOOKING_ID + "), " +
                "FOREIGN KEY(" + COLUMN_BS_SERVICE_ID + ") REFERENCES " + TABLE_SERVICES + "(" + COLUMN_SERVICE_ID + "))";
        db.execSQL(CREATE_BOOKING_SERVICES_TABLE);

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
                COLUMN_TT_TRANGTHAI + " TEXT, " +
                COLUMN_TT_BOOKING_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_TT_BOOKING_ID + ") REFERENCES " + TABLE_BOOKINGS + "(" + COLUMN_BOOKING_ID + "))";
        db.execSQL(CREATE_THANHTOAN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENUS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING_SERVICES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_THANHTOAN);
            onCreate(db);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public boolean addUser(String userId, String name, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_EMAIL, email);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean isPhoneExists(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_PHONE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{phone});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean checkUserExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?",
                new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public User getUserInfo(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String userId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE));
            String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            user = new User(id, userId, name, phone, userEmail);
        }
        cursor.close();
        db.close();
        return user;
    }

    public User getUserByUserId(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_ID + " = ?", new String[]{userId});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String firebaseUserId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            user = new User(id, firebaseUserId, name, phone, email);
        }
        cursor.close();
        db.close();
        return user;
    }

    public int addBooking(String userId, int restaurantId, int tableCount, String date, String time, int menuId, List<Integer> serviceIds, SQLiteDatabase db) {
        if (tableCount < 5 || tableCount > 50) return -1;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.DAY_OF_MONTH, 14);
        try {
            Date selectedDate = sdf.parse(date);
            if (selectedDate.before(minDate.getTime())) return -1;
        } catch (ParseException e) {
            return -1;
        }

        List<String> validTimes = Arrays.asList("15:00", "16:00", "17:00", "18:00", "19:00", "20:00");
        if (!validTimes.contains(time)) return -1;

        double menuPrice = 0;
        Cursor menuCursor = db.rawQuery("SELECT " + COLUMN_MENU_PRICE + " FROM " + TABLE_MENUS + " WHERE " + COLUMN_MENU_ID + " = ?", new String[]{String.valueOf(menuId)});
        if (menuCursor.moveToFirst()) {
            menuPrice = menuCursor.getDouble(0);
        }
        menuCursor.close();

        double serviceTotal = 0;
        for (int serviceId : serviceIds) {
            Cursor serviceCursor = db.rawQuery("SELECT " + COLUMN_SERVICE_PRICE + " FROM " + TABLE_SERVICES + " WHERE " + COLUMN_SERVICE_ID + " = ?", new String[]{String.valueOf(serviceId)});
            if (serviceCursor.moveToFirst()) {
                serviceTotal += serviceCursor.getDouble(0);
            }
            serviceCursor.close();
        }

        double totalPrice = (menuPrice * tableCount) + serviceTotal;

        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOKING_USER_ID, userId);
        values.put(COLUMN_BOOKING_RESTAURANT_ID, restaurantId);
        values.put(COLUMN_BOOKING_TABLE_COUNT, tableCount);
        values.put(COLUMN_BOOKING_DATE, date);
        values.put(COLUMN_BOOKING_TIME, time);
        values.put(COLUMN_BOOKING_MENU_ID, menuId);
        values.put(COLUMN_BOOKING_TOTAL_PRICE, totalPrice);

        long bookingId = db.insert(TABLE_BOOKINGS, null, values);
        if (bookingId == -1) return -1;

        for (int serviceId : serviceIds) {
            ContentValues serviceValues = new ContentValues();
            serviceValues.put(COLUMN_BS_BOOKING_ID, bookingId);
            serviceValues.put(COLUMN_BS_SERVICE_ID, serviceId);
            db.insert(TABLE_BOOKING_SERVICES, null, serviceValues);
        }

        return (int) bookingId;
    }

    public int addThanhToan(String hoTen, String email, String soDienThoai, int soLuongBan,
                            String ngayDatTiec, String ghiChu, double tongTien,
                            String phuongThucThanhToan, double soTienDaThanhToan,
                            String trangThaiThanhToan, int bookingId, SQLiteDatabase db) {
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
        values.put(COLUMN_TT_BOOKING_ID, bookingId);

        long id = db.insert(TABLE_THANHTOAN, null, values);
        return (int) id;
    }

    public List<Restaurant> searchRestaurants(String query) {
        List<Restaurant> restaurantList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESTAURANTS + " WHERE " + COLUMN_RESTAURANT_TITLE + " LIKE ?",
                new String[]{"%" + query + "%"});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_DESCRIPTION));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
                restaurantList.add(new Restaurant(id, title, description, image));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return restaurantList;
    }
    public List<Order> getOrderHistory(String userId) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT b." + COLUMN_BOOKING_ID + ", b." + COLUMN_BOOKING_TABLE_COUNT + ", b." + COLUMN_BOOKING_DATE + ", b." + COLUMN_BOOKING_TIME + ", " +
                "b." + COLUMN_BOOKING_TOTAL_PRICE + ", t." + COLUMN_TT_ID + ", t." + COLUMN_TT_PHUONGTHUC + ", t." + COLUMN_TT_SOTIENDATHANHTOAN + ", " +
                "t." + COLUMN_TT_TRANGTHAI + ", r." + COLUMN_RESTAURANT_TITLE + ", m." + COLUMN_MENU_TITLE +
                " FROM " + TABLE_BOOKINGS + " b" +
                " LEFT JOIN " + TABLE_THANHTOAN + " t ON b." + COLUMN_BOOKING_ID + " = t." + COLUMN_TT_BOOKING_ID +
                " LEFT JOIN " + TABLE_RESTAURANTS + " r ON b." + COLUMN_BOOKING_RESTAURANT_ID + " = r." + COLUMN_RESTAURANT_ID +
                " LEFT JOIN " + TABLE_MENUS + " m ON b." + COLUMN_BOOKING_MENU_ID + " = m." + COLUMN_MENU_ID +
                " WHERE b." + COLUMN_BOOKING_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userId});
        if (cursor.moveToFirst()) {
            do {
                int bookingId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_ID));
                int tableCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_TABLE_COUNT));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_TIME));
                double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_TOTAL_PRICE));
                int paymentId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TT_ID));
                String paymentMethod = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_PHUONGTHUC));
                double amountPaid = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TT_SOTIENDATHANHTOAN));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_TRANGTHAI));
                String restaurantTitle = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_TITLE));
                String menuTitle = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_TITLE));

                orderList.add(new Order(bookingId, tableCount, date, time, totalPrice, paymentId, paymentMethod, amountPaid, status, restaurantTitle, menuTitle));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orderList;
    }

    public boolean updateBooking(int bookingId, int tableCount, String date, String time, int menuId, List<Integer> serviceIds) {
        SQLiteDatabase db = this.getWritableDatabase();

        double menuPrice = 0;
        Cursor menuCursor = db.rawQuery("SELECT " + COLUMN_MENU_PRICE + " FROM " + TABLE_MENUS + " WHERE " + COLUMN_MENU_ID + " = ?", new String[]{String.valueOf(menuId)});
        if (menuCursor.moveToFirst()) {
            menuPrice = menuCursor.getDouble(0);
        }
        menuCursor.close();

        double serviceTotal = 0;
        if (serviceIds != null) {
            for (int serviceId : serviceIds) {
                Cursor serviceCursor = db.rawQuery("SELECT " + COLUMN_SERVICE_PRICE + " FROM " + TABLE_SERVICES + " WHERE " + COLUMN_SERVICE_ID + " = ?", new String[]{String.valueOf(serviceId)});
                if (serviceCursor.moveToFirst()) {
                    serviceTotal += serviceCursor.getDouble(0);
                }
                serviceCursor.close();
            }
        }

        double totalPrice = (menuPrice * tableCount) + serviceTotal;

        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOKING_TABLE_COUNT, tableCount);
        values.put(COLUMN_BOOKING_DATE, date);
        values.put(COLUMN_BOOKING_TIME, time);
        values.put(COLUMN_BOOKING_MENU_ID, menuId);
        values.put(COLUMN_BOOKING_TOTAL_PRICE, totalPrice);

        int rowsAffected = db.update(TABLE_BOOKINGS, values, COLUMN_BOOKING_ID + " = ?", new String[]{String.valueOf(bookingId)});
        if (rowsAffected > 0 && serviceIds != null) {
            db.delete(TABLE_BOOKING_SERVICES, COLUMN_BS_BOOKING_ID + " = ?", new String[]{String.valueOf(bookingId)});
            for (int serviceId : serviceIds) {
                ContentValues serviceValues = new ContentValues();
                serviceValues.put(COLUMN_BS_BOOKING_ID, bookingId);
                serviceValues.put(COLUMN_BS_SERVICE_ID, serviceId);
                db.insert(TABLE_BOOKING_SERVICES, null, serviceValues);
            }
        }
        db.close();
        return rowsAffected > 0;
    }

    public boolean updateThanhToan(int paymentId, String hoTen, String email, String soDienThoai, int soLuongBan,
                                   String ngayDatTiec, String ghiChu, double tongTien, String phuongThucThanhToan,
                                   double soTienDaThanhToan, String trangThaiThanhToan) {
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

        int rowsAffected = db.update(TABLE_THANHTOAN, values, COLUMN_TT_ID + " = ?", new String[]{String.valueOf(paymentId)});
        db.close();
        return rowsAffected > 0;
    }

    public List<Booking> getUserBookings(String userId) {
        List<Booking> bookingList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKINGS + " WHERE " + COLUMN_BOOKING_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userId});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_ID));
                int restaurantId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_RESTAURANT_ID));
                int tableCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_TABLE_COUNT));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_TIME));
                int menuId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_MENU_ID));
                double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_TOTAL_PRICE));
                bookingList.add(new Booking(id, userId, restaurantId, tableCount, date, time, menuId, totalPrice));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookingList;
    }

    public List<Integer> getServiceIdsForBooking(int bookingId) {
        List<Integer> serviceIds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_BS_SERVICE_ID + " FROM " + TABLE_BOOKING_SERVICES +
                        " WHERE " + COLUMN_BS_BOOKING_ID + " = ?",
                new String[]{String.valueOf(bookingId)});
        if (cursor.moveToFirst()) {
            do {
                serviceIds.add(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BS_SERVICE_ID)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return serviceIds;
    }

    public ThanhToan getThanhToan(int maThanhToan) {
        SQLiteDatabase db = this.getReadableDatabase();
        ThanhToan thanhToan = null;

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_THANHTOAN + " WHERE " + COLUMN_TT_ID + " = ?",
                new String[]{String.valueOf(maThanhToan)});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TT_ID));
            String hoTen = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_HOTEN));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_EMAIL));
            String soDienThoai = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_SODIENTHOAI));
            int soLuongBan = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TT_SOLUONGBAN));
            String ngayDatTiec = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_NGAYDATTIEC));
            String ghiChu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_GHICHU));
            double tongTien = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TT_TONGTIEN));
            String phuongThucThanhToan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_PHUONGTHUC));
            double soTienDaThanhToan = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TT_SOTIENDATHANHTOAN));
            String trangThaiThanhToan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_TRANGTHAI));
            int bookingId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TT_BOOKING_ID));

            thanhToan = new ThanhToan(id, hoTen, email, soDienThoai, soLuongBan, ngayDatTiec, ghiChu,
                    tongTien, phuongThucThanhToan, soTienDaThanhToan, trangThaiThanhToan, bookingId);
        }

        cursor.close();
        db.close();
        return thanhToan;
    }

    public List<ThanhToan> getAllThanhToan() {
        List<ThanhToan> thanhToanList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_THANHTOAN, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TT_ID));
                String hoTen = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_HOTEN));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_EMAIL));
                String soDienThoai = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_SODIENTHOAI));
                int soLuongBan = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TT_SOLUONGBAN));
                String ngayDatTiec = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_NGAYDATTIEC));
                String ghiChu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_GHICHU));
                double tongTien = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TT_TONGTIEN));
                String phuongThucThanhToan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_PHUONGTHUC));
                double soTienDaThanhToan = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TT_SOTIENDATHANHTOAN));
                String trangThaiThanhToan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_TRANGTHAI));
                int bookingId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TT_BOOKING_ID));

                ThanhToan thanhToan = new ThanhToan(id, hoTen, email, soDienThoai, soLuongBan, ngayDatTiec, ghiChu,
                        tongTien, phuongThucThanhToan, soTienDaThanhToan, trangThaiThanhToan, bookingId);
                thanhToanList.add(thanhToan);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return thanhToanList;
    }

    public Booking getBookingById(int bookingId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Booking booking = null;

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKINGS + " WHERE " + COLUMN_BOOKING_ID + " = ?",
                new String[]{String.valueOf(bookingId)});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_ID));
            String userId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_USER_ID));
            int restaurantId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_RESTAURANT_ID));
            int tableCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_TABLE_COUNT));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_DATE));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_TIME));
            int menuId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_MENU_ID));
            double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_TOTAL_PRICE));

            booking = new Booking(id, userId, restaurantId, tableCount, date, time, menuId, totalPrice);
        }

        cursor.close();
        db.close();
        return booking;
    }

    public ThanhToan getThanhToanByBookingId(int bookingId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ThanhToan thanhToan = null;

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_THANHTOAN + " WHERE " + COLUMN_TT_BOOKING_ID + " = ?",
                new String[]{String.valueOf(bookingId)});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TT_ID));
            String hoTen = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_HOTEN));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_EMAIL));
            String soDienThoai = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_SODIENTHOAI));
            int soLuongBan = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TT_SOLUONGBAN));
            String ngayDatTiec = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_NGAYDATTIEC));
            String ghiChu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_GHICHU));
            double tongTien = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TT_TONGTIEN));
            String phuongThucThanhToan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_PHUONGTHUC));
            double soTienDaThanhToan = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TT_SOTIENDATHANHTOAN));
            String trangThaiThanhToan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TT_TRANGTHAI));
            int bookingIdFromDb = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TT_BOOKING_ID));

            thanhToan = new ThanhToan(id, hoTen, email, soDienThoai, soLuongBan, ngayDatTiec, ghiChu,
                    tongTien, phuongThucThanhToan, soTienDaThanhToan, trangThaiThanhToan, bookingIdFromDb);
        }

        cursor.close();
        db.close();
        return thanhToan;
    }
    public static class User {
        private int id;
        private String userId;
        private String name;
        private String phone;
        private String email;

        public User(int id, String userId, String name, String phone, String email) {
            this.id = id;
            this.userId = userId;
            this.name = name;
            this.phone = phone;
            this.email = email;
        }

        public int getId() { return id; }
        public String getUserId() { return userId; }
        public String getName() { return name; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }
    }

    public static class Order {
        private int bookingId;
        private int tableCount;
        private String date;
        private String time;
        private double totalPrice;
        private int paymentId;
        private String paymentMethod;
        private double amountPaid;
        private String status;
        private String restaurantTitle;
        private String menuTitle;

        public Order(int bookingId, int tableCount, String date, String time, double totalPrice,
                     int paymentId, String paymentMethod, double amountPaid, String status,
                     String restaurantTitle, String menuTitle) {
            this.bookingId = bookingId;
            this.tableCount = tableCount;
            this.date = date;
            this.time = time;
            this.totalPrice = totalPrice;
            this.paymentId = paymentId;
            this.paymentMethod = paymentMethod;
            this.amountPaid = amountPaid;
            this.status = status;
            this.restaurantTitle = restaurantTitle;
            this.menuTitle = menuTitle;
        }

        public int getBookingId() { return bookingId; }
        public int getTableCount() { return tableCount; }
        public String getDate() { return date; }
        public String getTime() { return time; }
        public double getTotalPrice() { return totalPrice; }
        public int getPaymentId() { return paymentId; }
        public String getPaymentMethod() { return paymentMethod; }
        public double getAmountPaid() { return amountPaid; }
        public String getStatus() { return status; }
        public String getRestaurantTitle() { return restaurantTitle; }
        public String getMenuTitle() { return menuTitle; }
    }

    public static class Booking {
        private int id;
        private String userId;
        private int restaurantId;
        private int tableCount;
        private String date;
        private String time;
        private int menuId;
        private double totalPrice;

        public Booking(int id, String userId, int restaurantId, int tableCount, String date, String time, int menuId, double totalPrice) {
            this.id = id;
            this.userId = userId;
            this.restaurantId = restaurantId;
            this.tableCount = tableCount;
            this.date = date;
            this.time = time;
            this.menuId = menuId;
            this.totalPrice = totalPrice;
        }

        public int getId() { return id; }
        public String getUserId() { return userId; }
        public int getRestaurantId() { return restaurantId; }
        public int getTableCount() { return tableCount; }
        public String getDate() { return date; }
        public String getTime() { return time; }
        public int getMenuId() { return menuId; }
        public double getTotalPrice() { return totalPrice; }
    }

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
        private int bookingId;

        public ThanhToan(int maThanhToan, String hoTen, String email, String soDienThoai, int soLuongBan,
                         String ngayDatTiec, String ghiChu, double tongTien, String phuongThucThanhToan,
                         double soTienDaThanhToan, String trangThaiThanhToan, int bookingId) {
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
            this.bookingId = bookingId;
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
        public int getBookingId() { return bookingId; }
    }

    public boolean updateUser(String userId, String name, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_EMAIL, email);

        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_USER_ID + " = ?", new String[]{userId});
        db.close();
        return rowsAffected > 0;
    }
    // Thêm các hằng số cho tên cột


    // Phương thức thêm nhà hàng
    public long addRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_RESTAURANT_TITLE, restaurant.getTitle());
        values.put(COLUMN_RESTAURANT_DESCRIPTION, restaurant.getDescription());
        values.put(COLUMN_IMAGE, restaurant.getImageResource());

        long result = db.insert(TABLE_RESTAURANTS, null, values);
        db.close();
        return result;
    }

    // Phương thức lấy tất cả nhà hàng
    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurantList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RESTAURANTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Restaurant restaurant = new Restaurant();
                restaurant.setRestaurantId(cursor.getInt(0));
                restaurant.setTitle(cursor.getString(1));
                restaurant.setDescription(cursor.getString(2));
                restaurant.setImageResource(cursor.getString(3));
                restaurantList.add(restaurant);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return restaurantList;
    }

    // Phương thức cập nhật nhà hàng
    public int updateRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_RESTAURANT_TITLE, restaurant.getTitle());
        values.put(COLUMN_RESTAURANT_DESCRIPTION, restaurant.getDescription());
        values.put(COLUMN_IMAGE, restaurant.getImageResource());

        int rowsAffected = db.update(TABLE_RESTAURANTS, values,
                COLUMN_RESTAURANT_ID + " = ?",
                new String[]{String.valueOf(restaurant.getRestaurantId())});
        db.close();
        return rowsAffected;
    }
    // Phương thức xóa nhà hàng
    public int deleteRestaurant(int restaurantId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_RESTAURANTS,
                COLUMN_RESTAURANT_ID + " = ?",
                new String[]{String.valueOf(restaurantId)});
        db.close();
        return rowsDeleted;
    }
    public long addService(Service service) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SERVICE_TITLE, service.getTitle());
        values.put(COLUMN_SERVICE_PRICE, service.getPrice());
        long id = db.insert(TABLE_SERVICES, null, values);
        db.close();
        return id;
    }

    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SERVICES,
                new String[]{COLUMN_SERVICE_ID, COLUMN_SERVICE_TITLE, COLUMN_SERVICE_PRICE},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Service service = new Service();
                service.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_ID)));
                service.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_TITLE)));
                service.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_PRICE)));
                services.add(service);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return services;
    }

    public int updateService(Service service) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SERVICE_TITLE, service.getTitle());
        values.put(COLUMN_SERVICE_PRICE, service.getPrice());
        return db.update(TABLE_SERVICES, values,
                COLUMN_SERVICE_ID + " = ?",
                new String[]{String.valueOf(service.getId())});
    }

    public void deleteService(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SERVICES, COLUMN_SERVICE_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
    public long addMenu(MenuItem menuItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MENU_TITLE, menuItem.getTitle());
        values.put(COLUMN_MENU_PRICE, menuItem.getPrice());
        values.put(COLUMN_MENU_DESCRIPTION, menuItem.getDescription());
        long id = db.insert(TABLE_MENUS, null, values);
        db.close();
        return id;
    }

    public List<MenuItem> getAllMenus() {
        List<MenuItem> menuItems = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MENUS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MenuItem item = new MenuItem();
                item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_ID)));
                item.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_TITLE)));
                item.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MENU_PRICE)));
                item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_DESCRIPTION))); // Thêm dòng này
                menuItems.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return menuItems;
    }

    public int updateMenu(MenuItem menuItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MENU_TITLE, menuItem.getTitle());
        values.put(COLUMN_MENU_PRICE, menuItem.getPrice());
        values.put(COLUMN_MENU_DESCRIPTION, menuItem.getDescription());
        return db.update(TABLE_MENUS, values, COLUMN_MENU_ID + " = ?",
                new String[]{String.valueOf(menuItem.getId())});
    }
    public void deleteMenu(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MENUS, COLUMN_MENU_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
    public boolean isTimeSlotBooked(String date, String time, int restaurantId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKINGS +
                " WHERE " + COLUMN_BOOKING_DATE + " = ?" +
                " AND " + COLUMN_BOOKING_TIME + " = ?" +
                " AND " + COLUMN_BOOKING_RESTAURANT_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{date, time, String.valueOf(restaurantId)});
        boolean isBooked = cursor.getCount() > 0;
        cursor.close();
        return isBooked;
    }

}