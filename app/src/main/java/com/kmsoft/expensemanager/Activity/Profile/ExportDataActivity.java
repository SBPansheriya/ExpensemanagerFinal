package com.kmsoft.expensemanager.Activity.Profile;

import static com.kmsoft.expensemanager.Constant.incomeAndExpenseArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ExportDataActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 101;


    ImageView back;
    LinearLayout export;
    Spinner whatDataExport, whenDateRange, whatFormat;
    ArrayList<IncomeAndExpense> incomeList = new ArrayList<>();
    ArrayList<IncomeAndExpense> expenseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_data);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        init();

        incomeList = filterCategories(incomeAndExpenseArrayList, "Income");
        expenseList = filterCategories(incomeAndExpenseArrayList, "Expense");

        ArrayAdapter<String> whatDataExportAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, this.getResources().getStringArray(R.array.whatDataExport));
        whatDataExportAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        whatDataExport.setAdapter(whatDataExportAdapter);

        ArrayAdapter<String> whenDateAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, this.getResources().getStringArray(R.array.whenDate));
        whenDateAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        whenDateRange.setAdapter(whenDateAdapter);

        ArrayAdapter<String> whatFormatAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, this.getResources().getStringArray(R.array.whatFormat));
        whatFormatAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        whatFormat.setAdapter(whatFormatAdapter);

        whatDataExport.setSelection(0);
        whenDateRange.setSelection(0);

        filterCategories1(incomeAndExpenseArrayList, whenDateRange.getSelectedItem().toString());

        whatDataExport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        whenDateRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (whatDataExport.getSelectedItem().toString().equals("All")) {
                    incomeAndExpenseArrayList = filterCategories1(incomeAndExpenseArrayList, whenDateRange.getSelectedItem().toString());
                } else if (whatDataExport.getSelectedItem().toString().equals("Income")) {
                    incomeList = filterCategories1(incomeList, whenDateRange.getSelectedItem().toString());
                } else if (whatDataExport.getSelectedItem().toString().equals("Expense")) {
                    expenseList = filterCategories1(expenseList, whenDateRange.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        back.setOnClickListener(v -> onBackPressed());

        export.setOnClickListener(v -> {
            if (whatFormat.getSelectedItem().toString().equals("PDF")) {
                if (whatDataExport.getSelectedItem().toString().equals("All")) {
                    generatePDF(incomeAndExpenseArrayList, "Income and Expense Report");
                } else if (whatDataExport.getSelectedItem().toString().equals("Income")) {
                    generatePDF(incomeList, "Income Report");
                } else if (whatDataExport.getSelectedItem().toString().equals("Expense")) {
                    generatePDF(expenseList, "Expense Report");
                }
            } else {
                if (whatDataExport.getSelectedItem().toString().equals("All")) {
                    generateCsvFile(incomeAndExpenseArrayList, "Income and Expense Report");
                } else if (whatDataExport.getSelectedItem().toString().equals("Income")) {
                    generateCsvFile(incomeList, "Income Report");
                } else if (whatDataExport.getSelectedItem().toString().equals("Expense")) {
                    generateCsvFile(expenseList, "Expense Report");
                }
            }
        });
    }

    private ArrayList<IncomeAndExpense> filterCategories(ArrayList<IncomeAndExpense> incomeAndExpenses, String type) {
        ArrayList<IncomeAndExpense> filteredList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        for (IncomeAndExpense incomeAndExpense : incomeAndExpenses) {
            if (incomeAndExpense.getTag().equals(type)) {
                filteredList.add(incomeAndExpense);
            }
        }
        return filteredList;
    }

    private ArrayList<IncomeAndExpense> filterCategories1(ArrayList<IncomeAndExpense> incomeAndExpenses, String timePeriod) {
        ArrayList<IncomeAndExpense> filteredList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        switch (timePeriod) {
            case "All":
                return incomeAndExpenses;
            case "Last 30 days":
                calendar.add(Calendar.DAY_OF_MONTH, -30);
                break;
            case "Year":
                calendar.set(Calendar.MONTH, Calendar.JANUARY);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
//                calendar.add(Calendar.YEAR, -1);
                break;
            case "Week":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
//                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                break;
            default:
                break;
        }

        Date startDate = calendar.getTime();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (IncomeAndExpense incomeAndExpense : incomeAndExpenses) {
            String entryDateString = incomeAndExpense.getDate();
            try {
                Date entryDate = sdf.parse(entryDateString);
                assert entryDate != null;
                if (isWithinPeriod(entryDate, startDate)) {
                    filteredList.add(incomeAndExpense);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return filteredList;
    }

    private boolean isWithinPeriod(Date transactionDate, Date startDate) {
        return !transactionDate.before(startDate);
    }

//    public String getDefaultStorageLocation(Context context) {
//        File dir = context.getFilesDir();
//        if (!dir.exists()) {
//            boolean isDirectoryCreated = dir.mkdir();
//            if (!isDirectoryCreated) {
//                Log.e("Error", "Directory could not be created");
//            }
//        }
//        return dir.getAbsolutePath() + "/Expense Manager/";
//    }
//
//    public File getOrCreatePdfDirectory() {
//        File folder = new File(getDefaultStorageLocation(this));
//        if (!folder.exists())
//            folder.mkdir();
//        return folder;
//    }

    private String Path(String head) {
        String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Expense Manager";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String random = head + new Random().nextInt(100000) + ".pdf";
        String pdfPath = folderPath + "/" + random;
        return pdfPath;
    }

    private void generatePDF(ArrayList<IncomeAndExpense> incomeAndExpenseArrayList, String head) {

        String pdfPath = Path(head);
        Uri fileUri = FileProvider.getUriForFile(this, "com.kmsoft.expensemanager.fileprovider", new File(pdfPath));

        try {
            PdfWriter writer = new PdfWriter(getContentResolver().openOutputStream(fileUri));
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            document.add(new Paragraph(head)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(20));

            // Add table
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1, 1, 1}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.setTextAlignment(TextAlignment.CENTER);

            // Add headers
            table.addCell("Type");
            table.addCell("Category");
            table.addCell("CategoryImage");
            table.addCell("Amount");
            table.addCell("Date");
            table.addCell("Description");

            // Add data
            for (IncomeAndExpense item : incomeAndExpenseArrayList) {
                table.addCell(item.getTag());
                table.addCell(item.getCategoryName());
                if (item.getAddAttachment() != null) {
                    String filePath = getPathFromUri(this, Uri.parse(item.getAddAttachment()));
                    Image img = new Image(ImageDataFactory.create(filePath));
                    table.addCell(img.setAutoScale(true));
                } else {
                    table.addCell("No image found");
                }
                table.addCell(String.valueOf(item.getAmount()));
                table.addCell(item.getDate());
                if (!TextUtils.isEmpty(item.getDescription())) {
                    table.addCell(item.getDescription());
                } else {
                    table.addCell("No description");
                }
            }
            document.add(table);
            Toast.makeText(this, "Pdf create successfully", Toast.LENGTH_SHORT).show();
            document.close();

            openPdfFile(pdfPath);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void openPdfFile(String filePath) {
        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(this, "com.kmsoft.expensemanager.fileprovider", file);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    public void generateCsvFile(ArrayList<IncomeAndExpense> incomeAndExpenseArrayList, String head) {

        String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Expense Manager";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String random = head + new Random().nextInt(100000) + ".csv";
        String pdfPath = folderPath + "/" + random;

        try (CSVWriter writer = new CSVWriter(new FileWriter(pdfPath))) {
            String[] header = {"Type", "Category", "Amount", "Date", "Description"};
            writer.writeNext(header);


            for (IncomeAndExpense item : incomeAndExpenseArrayList) {
                String[] data = {
                        item.getTag(),
                        item.getCategoryName(),
                        String.valueOf(item.getAmount()),
                        item.getDate(),
                        TextUtils.isEmpty(item.getDescription()) ? item.getDescription() : "No description",
                };
                writer.writeNext(data);
            }
            Toast.makeText(this, "CSV File created successfully", Toast.LENGTH_SHORT).show();
            openCsvFile(pdfPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openCsvFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            Uri uri = FileProvider.getUriForFile(this, "com.kmsoft.expensemanager.fileprovider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "text/csv");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } else {
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
        }
    }

    public static String encodeImageToBase64(String imagePath) {
        String encodedString = null;
        try {
            File imageFile = new File(imagePath);
            FileInputStream fileInputStreamReader = new FileInputStream(imageFile);
            byte[] imageData = new byte[(int) imageFile.length()];
            fileInputStreamReader.read(imageData);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                encodedString = Base64.getEncoder().encodeToString(imageData);
            }
            fileInputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedString;
    }

    public static String getPathFromUri(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return filePath;
    }

    private void init() {
        back = findViewById(R.id.back);
        export = findViewById(R.id.export);
        whatDataExport = findViewById(R.id.what_data_export);
        whenDateRange = findViewById(R.id.when_date_range);
        whatFormat = findViewById(R.id.what_format);
    }
}