package com.samcenter.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class FilePathTest {

    // Lấy thư mục gốc của dự án
    public static final String BASE_DIR = System.getProperty("user.dir");

    // Đường dẫn thư mục chứa kết quả test
    public static final String TEST_RESULT_FOLDER = BASE_DIR + File.separator + "test-results";

    // Đường dẫn file Excel kết quả test
    public static final String EXCEL_FILE_PATH = TEST_RESULT_FOLDER + File.separator + "RESULT_TEST_USER_DAO.xlsx";
    public static final String EXCEL_FILE_PATH_UI_LOGIN = TEST_RESULT_FOLDER + File.separator + "RESULT_TEST_LOGIN.xlsx";
    public static final String EXCEL_FILE_PATH_UI_REGISTER = TEST_RESULT_FOLDER + File.separator + "RESULT_TEST_SIGN_IN.xlsx";


    // Thư mục chứa ảnh test
    public static final String IMAGE_DIR = TEST_RESULT_FOLDER + File.separator + "images";

    // Đường dẫn tương đối đến thư mục chứa Excel trong resources
    public static final String EXCEL_RESOURCE_PATH = "excel/";

    static {
        createDirectory(TEST_RESULT_FOLDER);
        createDirectory(IMAGE_DIR);
    }

    private static void createDirectory(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    /**
     * Đọc dữ liệu Excel từ resources/excel
     *
     * @param fileName  Tên file (VD: "User_TestCases_DAO.xlsx")
     * @param sheetName Tên sheet trong file Excel
     * @return Map dữ liệu
     */
    public static Map<String, String[]> readUserTestData(String fileName, String sheetName) throws IOException {
        String resourcePath = EXCEL_RESOURCE_PATH + fileName;

        // Dùng ClassLoader để đọc file từ resources
        try (InputStream is = FilePathTest.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Không tìm thấy file: " + resourcePath);
            }

            return ExcelHelper.readDataFromInputStream(is, sheetName);
        }
    }
    public static Map<String, String[]> readLoginTestData(String fileName, String sheetName) throws IOException {
        String resourcePath = EXCEL_RESOURCE_PATH + fileName;

        // Dùng ClassLoader để đọc file từ resources
        try (InputStream is = FilePathTest.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Không tìm thấy file: " + resourcePath);
            }

            return ExcelHelper.readDataLoginFromInputStream(is, sheetName);
        }
    }
    public static Map<String, String[]> readSignInTestData(String fileName, String sheetName) throws IOException {
        String resourcePath = EXCEL_RESOURCE_PATH + fileName;

        // Dùng ClassLoader để đọc file từ resources
        try (InputStream is = FilePathTest.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Không tìm thấy file: " + resourcePath);
            }

            return ExcelHelper.readDataSignInFromInputStream(is, sheetName);
        }
    }
}
