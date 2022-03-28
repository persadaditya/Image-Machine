package app.pdg.imagemachine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class Utils {

    public static final Locale indonesia = new Locale("in", "ID");
    private static int TYPE_WIFI = 1;
    private static int TYPE_MOBILE = 2;
    private static int TYPE_NOT_CONNECTED = 0;
    static final String INDONESIAN_MONEY_FORMAT = "###,##0.00";
    static final String INDONESIAN_MONEY_FORMAT_NO_COMMA = "###,##0";
    public static final String EMPTY_STRING = "";
    public static final String DEFAULT_CURRENCY = "0";

    static final String LOCAL_DB_DATE_PATTERN = "yyyy-MM-dd";
    public static final String LOCAL_DATE_PATTERN = "dd/MM/yyyy";
    static final String LOCAL_DATE_PATTERN_ID = "dd-MM-yyyy";
    static final String LOCAL_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    static final String LOCAL_TIME_PATTERN = "HH:mm";
    static final String LOCAL_DATE_PATTERN_TEXT = "MMM dd',' yyyy";
    static final String LOCAL_DATE_PATTERN_TIME = "MMM dd',' yyyy hh:mm a";
    public static final String LOCAL_DATE_TEXT_ALL_ID = "dd MMMM yyyy";
    public static final String LOCAL_DATE_WITH_TIME = "dd MMMM yyyy, HH:mm";
    public static final String LOCAL_DATE_TEXT_PARTIAL_ID = "dd MMM yyyy";
    public static final String LOCAL_DATE_ENG = "MM/dd/yyyy";

    private static final String PREF_NAME = "ihram_mobile_pref_name";
    private static final String SERVER_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"; // DB Server
    public static final String SERVER_DATETIME_MC_PATTERN = "yyyy-MM-dd HH:mm:ss"; // DB Server
    private static final String SERVER_DATETIME_PATTERN_LOCAL = "yyyy-MM-dd'T'HH:mm:ssXXX"; // DB Local

    public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
    }

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

    public static String getDbDateFormat(String dateString) {
        DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dbDateFormat = "";
        try {
            Date date = sdf.parse(dateString);
            dbDateFormat = new SimpleDateFormat("yyyy-MM-dd").format(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return dbDateFormat;
    }

    public static SharedPreferences getDefaultPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, 0);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return format.format(date);
    }

    public static String getHeaderTime(Date headerTime) {
        SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy", Locale.US);
        return format.format(headerTime);
    }

    public static Date parseDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String ConvertBirthDateFormatToServer(String birthDate) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMMM yyyy");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date;
        String str = null;

        try {
            date = inputFormat.parse(birthDate);
            if (date != null) {
                str = outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String ConvertBirthDateFormatToLocal(String birthDate) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date;
        String str = null;

        try {
            date = inputFormat.parse(birthDate);
            if (date != null) {
                str = outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static void hideVirtualKeyboard(Context context, View v) {
        InputMethodManager keyboard = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (keyboard != null) {
            keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static String formatCurrency(String value) {

        if (TextUtils.isEmpty(value)) {
            return "Rp. 0";
        }

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return "Rp. " + formatter.format(Integer.parseInt(value));
    }

    public static String setLengthValue(String value, int maxLength) {

        String tempValue = "";
        int tempLength = value.length();
        int diffLength = maxLength - tempLength;

        if(tempLength >= maxLength){
            return value;
        }

        for(int a = 0; a< diffLength ; a++){
            tempValue = tempValue + " ";
        }

        return tempValue + value;
    }

    public static String formatCurrencyDouble(double value) {

        if (value == 0) {
            return "Rp.0";
        }

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return "Rp." + formatter.format(value);
    }

    public static String formatUniqueCode(String value) {

        if (TextUtils.isEmpty(value)) {
            return "0";
        }

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(Integer.parseInt(value));
    }

    public static String formatNumber(String value) {

        if (TextUtils.isEmpty(value)) {
            return "0 KM";
        }

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(Integer.parseInt(value)) + " KM";
    }

    public static String getCurrentTimestamp() {
        Long tsLong = System.currentTimeMillis() / 1000;
        return tsLong.toString();
    }

    public static Date getTodayDateTime() {

        Date date = new Date(System.currentTimeMillis());
        return date;
    }

    public static String getDateForToday() {

        SimpleDateFormat format = new SimpleDateFormat(LOCAL_DATE_PATTERN, Locale.getDefault());
        Date date = new Date();
        return format.format(date);
    }

    public static String getDatePlusOrMinusStr(int pDay, String formatDate) {

        //SimpleDateFormat format = new SimpleDateFormat(formatDate, Locale.getDefault());

        Date date = new Date();
        String tempDate = "";

        SimpleDateFormat sdf = new SimpleDateFormat(formatDate);

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, pDay);  // set Min Date

        tempDate = sdf.format(c.getTime());

        try {

            SimpleDateFormat formatter6 = new SimpleDateFormat(formatDate);
            date = formatter6.parse(tempDate);

        }catch (Exception e){
            System.out.println("*** Error Convert Date : " + e.getMessage());
        }

        return sdf.format(date);
    }


    public static Date convertStrToDateDDMMYYYY(String localDateString) {

        SimpleDateFormat formatLocal = new SimpleDateFormat(LOCAL_DATE_PATTERN, Locale.getDefault());

        Date localDate;

        try {
            localDate = formatLocal.parse(localDateString);
        } catch (ParseException e) {
            e.printStackTrace();
            localDate = new Date();
        }

        return localDate;
    }

    public static String getDateTimeForToday() {

        SimpleDateFormat format = new SimpleDateFormat(LOCAL_DATETIME_PATTERN, Locale.getDefault());
        Date date = new Date();
        return format.format(date);
    }

    public static String getDateTimeServerForToday() {

        SimpleDateFormat format = new SimpleDateFormat(SERVER_DATETIME_PATTERN, Locale.getDefault());
        Date date = new Date();
        return format.format(date);
    }

    public static String getCurrentDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return format.format(date);
    }

    public static String getDateLocalFromDateTimeServer(String serverDateTimeString) {

        SimpleDateFormat formatLocal = new SimpleDateFormat(LOCAL_DATE_PATTERN_TEXT, Locale.getDefault());

        Date date;

        try {
            date = parseRFC3339Date(serverDateTimeString);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
        }

        return formatLocal.format(date);
    }

    public static String getDateLocalFromDateTimeServerWithTime(String serverDateTimeString) {

        SimpleDateFormat formatLocal = new SimpleDateFormat(LOCAL_DATE_PATTERN_TIME, Locale.getDefault());

        Date date;

        try {
            date = parseRFC3339Date(serverDateTimeString);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
        }

        return formatLocal.format(date);
    }

    public static String getDate(String serverDateTimeString) {

        SimpleDateFormat formatLocal = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        Date date;

        try {
            date = parseRFC3339Date(serverDateTimeString);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
        }

        return formatLocal.format(date);
    }

    public static String getDateDoku(String serverDateTimeString) {

        SimpleDateFormat formatLocal = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());

        Date date;

        try {
            date = parseRFC3339Date(serverDateTimeString);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
        }

        return formatLocal.format(date);
    }

    public static String getDateToStrDDMMYYYY(String stringDate) {

        SimpleDateFormat formatLocal = new SimpleDateFormat(LOCAL_DATE_PATTERN, Locale.getDefault());

        Date date;

        try {
            date = parseRFC3339Date(stringDate);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
        }

        return formatLocal.format(date);
    }

    public static String convertDateServerToLocal(Date serverDate) {

        SimpleDateFormat formatLocal = new SimpleDateFormat(LOCAL_DB_DATE_PATTERN, Locale.getDefault());

        Date date;
        String tempDate;

        try {
            tempDate = formatLocal.format(serverDate);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
            tempDate = formatLocal.format(date);
        }

        return tempDate;
    }

    public static String convertDateLocalToServer(Date localDate) {

        SimpleDateFormat formatLocal = new SimpleDateFormat(SERVER_DATETIME_MC_PATTERN, Locale.getDefault());

        Date date;
        String tempDate;

        try {
            tempDate = formatLocal.format(localDate);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
            tempDate = formatLocal.format(date);
        }

        return tempDate;
    }

    public static String convertDateTimeServerToLocal(Date serverDate) {

        SimpleDateFormat formatLocal = new SimpleDateFormat(LOCAL_DATETIME_PATTERN, Locale.getDefault());

        Date date;
        String tempDate;

        try {
            tempDate = formatLocal.format(serverDate);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
            tempDate = formatLocal.format(date);
        }

        return tempDate;
    }

    public static String convertDateServerToLocalFormat(Date serverDate) {

        SimpleDateFormat formatLocal = new SimpleDateFormat(LOCAL_DATE_PATTERN, Locale.getDefault());

        Date date;
        String tempDate;

        try {
            tempDate = formatLocal.format(serverDate);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
            tempDate = formatLocal.format(date);
        }

        return tempDate;
    }

    public static String convertDateServerToLocalFormatID(Date serverDate) {

        SimpleDateFormat formatLocal = new SimpleDateFormat(LOCAL_DATE_PATTERN_ID, Locale.getDefault());

        Date date;
        String tempDate;

        try {
            tempDate = formatLocal.format(serverDate);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
            tempDate = formatLocal.format(date);
        }

        return tempDate;
    }

    /**
     * convert date from date server to local ID
     * @param serverDate server date from api
     * @param outputFormat output format what you want
     * example: server date 2021-02-23 07:19:40
     * expected: 23 February 2021
     * */
    public static String convertDateServerToLocalAllID(String serverDate, String outputFormat){
        SimpleDateFormat formatLocal = new SimpleDateFormat(outputFormat, Locale.getDefault());

        Date date;
        String tempDate;

        //2021-02-23 07:19:40 -> parsing this format
        if(serverDate==null){
            return "";
        }
        String[] datetime = serverDate.split(" ");
        String dateStr = datetime[0]; //2021-02-23 -> we got this
        if(datetime.length>1){
            String timeStr = datetime[1]; //07:19:40 -> we got this
        }

        String[] dateParse = dateStr.split("-"); //parsing this format 2021-02-23
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(dateParse[0]), Integer.parseInt(dateParse[1])-1,
                Integer.parseInt(dateParse[2]));

        Date parseServerDate = calendar.getTime();

        try {
            tempDate = formatLocal.format(parseServerDate);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
            tempDate = formatLocal.format(date);
        }

        return tempDate;
    }

    /**
     * get countdown date with by
     * @param thisTime we pass for this time
     * @param eventTime  we pass time to compare
     * given format example for date yyyy-MM-dd HH:mm:ss
     * */
    public static long getDifferentBetweenTwoDates(String thisTime, String eventTime){

        String[] thisDateTime = thisTime.split(" ");
        String[] thisDateTimeParse = thisDateTime[0].split("-");
        String[] thisTimeParse = thisDateTime[1].split(":");
        Calendar calendarThisTime = Calendar.getInstance();
        calendarThisTime.set(Integer.parseInt(thisDateTimeParse[0]), Integer.parseInt(thisDateTimeParse[1])-1,
                Integer.parseInt(thisDateTimeParse[2]),
                Integer.parseInt(thisTimeParse[0]),
                Integer.parseInt(thisTimeParse[1]),
                Integer.parseInt(thisTimeParse[2]));

        String[] eventDateTime = eventTime.split(" ");
        String[] eventDateTimeParse = eventDateTime[0].split("-");
        String[] eventTimeParse = eventDateTime[1].split(":");
        Calendar calendarEventTime = Calendar.getInstance();
        calendarEventTime.set(Integer.parseInt(eventDateTimeParse[0]), Integer.parseInt(eventDateTimeParse[1])-1,
                Integer.parseInt(eventDateTimeParse[2]),
                Integer.parseInt(eventTimeParse[0]),
                Integer.parseInt(eventTimeParse[1]),
                Integer.parseInt(eventTimeParse[2]));

        Date thisTimeDate = calendarThisTime.getTime();
        Date eventTimeDate = calendarEventTime.getTime();

        long diffInMillieSec = thisTimeDate.getTime() - eventTimeDate.getTime();
        return diffInMillieSec/(1000 * 60 * 60 * 24);
    }

    public static String convertDateLongToString(long time){
        Date date = new Date(time);

        SimpleDateFormat formatLocal = new SimpleDateFormat(LOCAL_DATE_TEXT_ALL_ID, Locale.getDefault());

        return formatLocal.format(date);
    }

    public static String convertDateLongToString(long time, String format){
        Date date = new Date(time);

        SimpleDateFormat formatLocal = new SimpleDateFormat(format, Locale.getDefault());

        return formatLocal.format(date);
    }

    public static String convertAmPm(int hour, int minute){

        if(hour==12){
            if(minute>0){
                return "pm";
            }
            return "am";
        }

        if(hour>12){
            return "pm";
        }
        return "am";
    }

    public static String convertTwoTimeStringToString(String timeStart, String timeEnd){
        if(timeStart==null){
            return "-";
        }

        if(timeEnd==null){
            return "-";
        }

        String[] timesStart = timeStart.split(":");
        String[] timesEnd = timeEnd.split(":");

        return String.format("%s.%s - %s.%s", timesStart[0], timesStart[1], timesEnd[0], timesEnd[1]);
    }

    public static String convertDateLocalToServer(String localDateTimeString) {

        SimpleDateFormat formatServer = new SimpleDateFormat(SERVER_DATETIME_MC_PATTERN, Locale.getDefault());
        SimpleDateFormat formatLocal = new SimpleDateFormat(LOCAL_DATE_PATTERN_ID, Locale.getDefault());

        Date date;

        try {
            date = formatLocal.parse(localDateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }

        return formatServer.format(date);
    }

    public static String convertDateTimeLocalToServer(String localDateTimeString) {

        SimpleDateFormat formatServer = new SimpleDateFormat(SERVER_DATETIME_PATTERN, Locale.getDefault());
        SimpleDateFormat formatLocal = new SimpleDateFormat(LOCAL_DATETIME_PATTERN, Locale.getDefault());

        Date date;

        try {
            date = formatLocal.parse(localDateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }

        return formatServer.format(date);
    }

    public static Calendar convertDateLocalToCalendar(String localDateString) {

        SimpleDateFormat formatLocal = new SimpleDateFormat(LOCAL_DATE_PATTERN, Locale.getDefault());
        Calendar localCalendar = Calendar.getInstance();
        Date localDate;

        try {
            localDate = formatLocal.parse(localDateString);
        } catch (ParseException e) {
            e.printStackTrace();
            localDate = new Date();
        }

        localCalendar.setTime(localDate);
        return localCalendar;
    }

    public static Calendar getCalendarPlusOrMinus(int pDay){

        Date dateToday = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dateToday);
        c.add(Calendar.DATE, pDay);  // set Min Date

        return c;
    }

    public static Date getDatePlusOrMinus(int pDay){
        Date date = new Date();
        String tempDate = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, pDay);  // set Min Date

        tempDate = sdf.format(c.getTime());

        try {

            SimpleDateFormat formatter6 = new SimpleDateFormat("yyyy-MM-dd");
            date = formatter6.parse(tempDate);

        }catch (Exception e){
            System.out.println("*** Error Convert Date : " + e.getMessage());
        }

        return date;
    }



    public static Bitmap convertUriToBitmap(Context context, Uri imageUri) throws Exception {

        final InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
        return BitmapFactory.decodeStream(imageStream);
    }

    public static String convertImageToBase64(Bitmap scaledBitmap) {

        if (scaledBitmap == null) {
            return "";
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.WEBP, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return "data:image/webp;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap convertbase64ToBitmap(String base64) {

        if (base64 == null) {
            return null;
        }

        base64 = base64.substring(base64.lastIndexOf(",")+1);
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedByte;
    }

    private static Date parseRFC3339Date(String dateString) throws ParseException, IndexOutOfBoundsException {

        Date d;

        //if there is no time zone, we don't need to do any special parsing.
        if (dateString.endsWith("Z")) {
            try {
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());//spec for RFC3339 with a 'Z'
                s.setTimeZone(TimeZone.getTimeZone("UTC"));
                d = s.parse(dateString);
            } catch (ParseException pe) {//try again with optional decimals
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());//spec for RFC3339 with a 'Z' and fractional seconds
                s.setTimeZone(TimeZone.getTimeZone("UTC"));
                s.setLenient(true);
                d = s.parse(dateString);
            }
            return d;
        }

        //step one, split off the timezone.
        String firstPart;
        String secondPart;
        if (dateString.lastIndexOf('+') == -1) {
            firstPart = dateString.substring(0, dateString.lastIndexOf('-'));
            secondPart = dateString.substring(dateString.lastIndexOf('-'));
        } else {
            firstPart = dateString.substring(0, dateString.lastIndexOf('+'));
            secondPart = dateString.substring(dateString.lastIndexOf('+'));
        }

        //step two, remove the colon from the timezone offset
        secondPart = secondPart.substring(0, secondPart.indexOf(':')) + secondPart.substring(secondPart.indexOf(':') + 1);
        dateString = firstPart + secondPart;
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());//spec for RFC3339
        try {
            d = s.parse(dateString);
        } catch (ParseException pe) {//try again with optional decimals
            s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ", Locale.getDefault());//spec for RFC3339 (with fractional seconds)
            s.setLenient(true);
            d = s.parse(dateString);
        }

        return d;
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            }
            else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    public static int mod(int x, int y){
        int result = x % y;
        if (result < 0)
            result += y;

        return result;
    }
    /*
    nol=Nol
    1=Satu
    2=Dua
    3=Tiga
    4=Empat
    5=Lima
    6=Enam
    7=Tujuh
    8=Delapan
    9=Sembilan

    # Puluhan
    10=Sepuluh
    11=Sebelas
    12=Dua Belas
    13=Tiga Belas
    14=Empat Belas
    15=Lima Belas
    16=Enam Belas
    17=Tujuh Belas
    18=Delapan Belas
    19=Sembilan Belas
    20=Dua Puluh
    30=Tiga Puluh
    40=Empat Puluh
    50=Lima Puluh
    60=Enam Puluh
    70=Tujuh Puluh
    80=Delapan Puluh
    90=Sembilan Puluh

    # Ratusan
    100=Seratus
    200=Dua Ratus
    300=Tiga Ratus
    400=Empat Ratus
    500=Lima Ratus
    600=Enam Ratus
    700=Tujuh Ratus
    800=Delapan Ratus
    900=Sembilan Ratus

    # Ribuan
    1000=Seribu

    # Terbilang Footer
    0=
    00=
    000=Ratus
    0000=Ribu
    0000000=Juta
    0000000000=Milyar
    0000000000000=Trilyun
     */
    public static String terbilang(double value) {
        return terbilang(Math.round(Math.abs(value)));
    }

    public static String terbilang(long value) {
        StringBuffer sb = new StringBuffer();

        if (value==0) return "nol";

        // check for triliun, 1000000000000
        int temp = (int) (value / 1000000000000l);
        if (temp > 0) {
            if (sb.length() > 1)
                sb.append(" ");
            sb.append(ratusan(temp)).append(" ").append("Trilyun");
        }
        value = value % 1000000000000l;

        // check for milyar, 1000000000
        temp = (int) (value / 1000000000l);
        if (temp > 0) {
            if (sb.length() > 1)
                sb.append(" ");
            sb.append(ratusan(temp)).append(" ").append("Milyar");
        }
        value = value % 1000000000l;

        // check for juta, 1000000
        temp = (int) (value / 1000000l);
        if (temp > 0) {
            if (sb.length() > 1)
                sb.append(" ");
            sb.append(ratusan(temp)).append(" ").append("Juta");
        }
        value = value % 1000000l;

        // check for ribu, 1000
        temp = (int) (value / 1000l);
        if (temp == 1) {
            if (sb.length() > 1)
                sb.append(" ");
            sb.append("Seribu");
        } else if (temp > 0) {
            if (sb.length() > 1)
                sb.append(" ");
            sb.append(ratusan(temp)).append(" ").append("Ribu");
        }
        value = value % 1000l;

        // check for satuan, 1
        temp = (int) value;
        if (temp > 0) {
            if (sb.length() > 1)
                sb.append(" ");
            sb.append(ratusan(temp));
        }
        return sb.toString();
    }

    private static String ratusan(int value) {
        /*
        100=Seratus
        200=Dua Ratus
        300=Tiga Ratus
        400=Empat Ratus
        500=Lima Ratus
        600=Enam Ratus
        700=Tujuh Ratus
        800=Delapan Ratus
        900=Sembilan Ratus
         */

        //String msg = messages.getMessage(String.valueOf(value));
        String msg = null;

        if(value == 100){
            msg="Seratus";
        }else if(value == 200){
            msg="Dua Ratus";
        }else if(value == 300){
            msg="Tiga Ratus";
        }else if(value == 400){
            msg="Empat Ratus";
        }else if(value == 500){
            msg="Lima Ratus";
        }else if(value == 600){
            msg="Enam Ratus";
        }else if(value == 700){
            msg="Tujuh Ratus";
        }else if(value == 800){
            msg="Delapan Ratus";
        }else if(value == 900){
            msg="Sembilan Ratus";
        }


        if (msg == null) {
            // get ratusan
            //String ratusan = messages.getMessage(String.valueOf((((int) (value / 100)) * 100)));
            String ratusan = null;
            int tempRatusan = (((int) (value / 100)) * 100);

            if(tempRatusan == 100){
                ratusan="Seratus";
            }else if(tempRatusan == 200){
                ratusan="Dua Ratus";
            }else if(tempRatusan == 300){
                ratusan="Tiga Ratus";
            }else if(tempRatusan == 400){
                ratusan="Empat Ratus";
            }else if(tempRatusan == 500){
                ratusan="Lima Ratus";
            }else if(tempRatusan == 600){
                ratusan="Enam Ratus";
            }else if(tempRatusan == 700){
                ratusan="Tujuh Ratus";
            }else if(tempRatusan == 800){
                ratusan="Delapan Ratus";
            }else if(tempRatusan == 900){
                ratusan="Sembilan Ratus";
            }
            if (ratusan!=null && ratusan.length()>0) {
                msg = ratusan + " " + puluhan(value % 100);
            } else {
                msg = puluhan(value % 100);
            }
        }
        return msg;
        // 100=Seratus
        // check in message, if found return it
        // else get in satuan plus ratusan
    }

    private static String puluhan(int value) {
        /*
        10=Sepuluh
        11=Sebelas
        12=Dua Belas
        13=Tiga Belas
        14=Empat Belas
        15=Lima Belas
        16=Enam Belas
        17=Tujuh Belas
        18=Delapan Belas
        19=Sembilan Belas
        20=Dua Puluh
        30=Tiga Puluh
        40=Empat Puluh
        50=Lima Puluh
        60=Enam Puluh
        70=Tujuh Puluh
        80=Delapan Puluh
        90=Sembilan Puluh
         */
        //String msg = messages.getMessage(String.valueOf(value));
        String msg = null;

        if(value == 10){
            msg="Sepuluh";
        }else if(value == 11){
            msg="Sebelas";
        }else if(value == 12){
            msg="Dua Belas";
        }else if(value == 13){
            msg="Tiga Belas";
        }else if(value == 14){
            msg="Empat Belas";
        }else if(value == 15){
            msg="Lima Belas";
        }else if(value == 16){
            msg="Enam Belas";
        }else if(value == 17){
            msg="Tujuh Belas";
        }else if(value == 18){
            msg="Delapan Belas";
        }else if(value == 19){
            msg="Sembilan Belas";
        }else if(value == 20){
            msg="Dua Puluh";
        }else if(value == 30){
            msg="Tiga Puluh";
        }else if(value == 40){
            msg="Empat Puluh";
        }else if(value == 50){
            msg="Lima Puluh";
        }else if(value == 60){
            msg="Enam Puluh";
        }else if(value == 70){
            msg="Tujuh Puluh";
        }else if(value == 80){
            msg="Delapan Puluh";
        }else if(value == 90){
            msg="Sembilan Puluh";
        }

        if (msg == null) {
            // get puluhan
            //String puluhan = messages.getMessage( String.valueOf((((int) (value / 10)) * 10)) );
            String puluhan = null;
            int tempPuluhan = (((int) (value / 10)) * 10);

            if(tempPuluhan == 10){
                puluhan="Sepuluh";
            }else if(tempPuluhan == 11){
                puluhan="Sebelas";
            }else if(tempPuluhan == 12){
                puluhan="Dua Belas";
            }else if(tempPuluhan == 13){
                puluhan="Tiga Belas";
            }else if(tempPuluhan == 14){
                puluhan="Empat Belas";
            }else if(tempPuluhan == 15){
                puluhan="Lima Belas";
            }else if(tempPuluhan == 16){
                puluhan="Enam Belas";
            }else if(tempPuluhan == 17){
                puluhan="Tujuh Belas";
            }else if(tempPuluhan == 18){
                puluhan="Delapan Belas";
            }else if(tempPuluhan == 19){
                puluhan="Sembilan Belas";
            }else if(tempPuluhan == 20){
                puluhan="Dua Puluh";
            }else if(tempPuluhan == 30){
                puluhan="Tiga Puluh";
            }else if(tempPuluhan == 40){
                puluhan="Empat Puluh";
            }else if(tempPuluhan == 50){
                puluhan="Lima Puluh";
            }else if(tempPuluhan == 60){
                puluhan="Enam Puluh";
            }else if(tempPuluhan == 70){
                puluhan="Tujuh Puluh";
            }else if(tempPuluhan == 80){
                puluhan="Delapan Puluh";
            }else if(tempPuluhan == 90){
                puluhan="Sembilan Puluh";
            }

            if (puluhan!=null && puluhan.length()>0) {
                msg = puluhan + " " + satuan(value % 10);
            } else {
                msg = satuan(value % 10);
            }

        }
        return msg;
		/*
		10=Sepuluh
		11=Sebelas
		12=Dua Belas
		13=Tiga Belas
		14=Empat Belas
		15=Lima Belas
		16=Enam Belas
		17=Tujuh Belas
		18=Delapan Belas
		19=Sembilan Belas
		20=Dua Puluh
		30=Tiga Puluh
		40=Empat Puluh
		50=Lima Puluh
		60=Enam Puluh
		70=Tujuh Puluh
		80=Delapan Puluh
		90=Sembilan Puluh
		*/
    }

    private static String satuan(int value) {
        String temp = "";

        if(value == 1){
            temp="Satu";
        }else if(value == 2){
            temp="Dua";
        }else if(value == 3){
            temp="Tiga";
        }else if(value == 4){
            temp="Empat";
        }else if(value == 5){
            temp="Lima";
        }else if(value == 6){
            temp="Enam";
        }else if(value == 7){
            temp="Tujuh";
        }else if(value == 8){
            temp="Delapan";
        }else if(value == 9){
            temp="Sembilan";
        }


//        return messages.getMessage(String.valueOf(value));
		/*	0=
			1=Satu
			2=Dua
			3=Tiga
			4=Empat
			5=Lima
			6=Enam
			7=Tujuh
			8=Delapan
			9=Sembilan
		*/
        return  temp;
    }

    public static String convertMonthValue(int value) {
        String temp = "";

        if(value == 0){
            temp="01";
        } else if(value == 1){
            temp="02";
        }else if(value == 2){
            temp="03";
        }else if(value == 3){
            temp="04";
        }else if(value == 4){
            temp="05";
        }else if(value == 5){
            temp="06";
        }else if(value == 6){
            temp="07";
        }else if(value == 7){
            temp="08";
        }else if(value == 8){
            temp="09";
        }else if(value == 9){
            temp="10";
        }else if(value == 10){
            temp="11";
        }else if(value == 11){
            temp="12";
        }

        return  temp;
    }

    public static String getIdGenerated() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-hhmmss");
        String formattedDate = df.format(c);

        int random = new Random().nextInt(999) + 100;
        // 1 + 14 + 3 = 18 chars
        String strId = "1-" + formattedDate + "-" + String.valueOf(random);

        Log.d("# strId = ", strId);

        return strId;
    }

    public static Bitmap getCircleCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public static String BitMapToString(Bitmap userImage, String imgDoc) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        imgDoc = Base64.encodeToString(b, Base64.DEFAULT);
        return imgDoc;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Boolean passwordValidator(String password) {
        String passwordPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$";

        if (password.trim().matches(passwordPattern) && password.length() > 6) {
            return true;
        } else {
            return false;
        }
    }

    public static <T> List<List<T>> splitList(List<T> list, final int L) {
        List<List<T>> parts = new ArrayList<>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<T>(
                    list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;
    }

    public static String joinListString(List<String> list , String replacement ) {
        StringBuilder b = new StringBuilder();
        for( String item: list ) {
            b.append( replacement ).append( item );
        }
        return b.toString().substring( replacement.length() );
    }

}
