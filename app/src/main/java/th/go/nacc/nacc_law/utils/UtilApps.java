package th.go.nacc.nacc_law.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;


/**
 * Created by error on 4/4/2016 AD.
 */
public class UtilApps {


    public static String getMD5EncryptedString(String encTarget) {
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while (md5.length() < 32) {
            md5 = "0" + md5;
        }
        return md5;
    }

    public static ProgressDialog progressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        return progressDialog;
    }

    public static void saveToken(Context context, String a) {
        SPUtils.set(context, "token", a);
    }

    public static String getToken(Context context) {
        return SPUtils.getString(context, "token");
    }

    public static void saveTokenHipfat(Context context, String a) {
        SPUtils.set(context, "TokenHipfat", a);
    }

    public static String getTokenHipfat(Context context) {
        return SPUtils.getString(context, "TokenHipfat");
    }

    public static void setLogin(Context context, boolean a) {
        SPUtils.set(context, "Login", a);
    }

    public static boolean getLogin(Context context) {
        return SPUtils.getBoolean(context, "Login");
    }

    public static void saveJsonProfile(Context context, String a) {
        SPUtils.set(context, "profile", a);
    }




    public static void setProfileCat(Context context, int a) {
        SPUtils.set(context, "profile_cat", a);
    }

    public static int getProfileCat(Context context) {
        return SPUtils.getInt(context, "profile_cat");
    }

    public static void setisNewsClose(Context context, boolean a) {
        SPUtils.set(context, "isNewsClose", a);
    }

    public static boolean isNewsClose(Context context) {
        return SPUtils.getBoolean(context, "isNewsClose");
    }


    public static void setLang(Context context, String a) {
        SPUtils.set(context, "lang", a);
    }

    public static String getLang(Context context) {
        return SPUtils.getString(context, "lang");
    }

    public static void setUserID(Context context, String a) {
        SPUtils.set(context, "UserID", a);
    }

    public static String getUserID(Context context) {
        return SPUtils.getString(context, "UserID");
    }

    public static void alerDialog(Context c, String message) {
        new AlertDialog.Builder(c)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // continue with delete
                            }
                        }).show();
    }

    public static void alerDialogTitle(Context c, String title, String message) {
        new AlertDialog.Builder(c)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // continue with delete
                            }
                        }).show();
    }

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    public static void language(Context cx, String a) {
        // String a = getLanguage(cx);
        Resources res = cx.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = new Locale(a);
        res.updateConfiguration(conf, dm);
        setLang(cx, a);


    }

}
