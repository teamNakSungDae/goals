package nexters.hashgoals.models;

/**
 * Created by kwongiho on 2017. 4. 1..
 */
import android.content.Context;
import android.content.SharedPreferences;

public class CustomPreference {

    public static CustomPreference getInstance(Context context) {
        mContext = context;
        return customPreference;
    }
    static {
        customPreference = new CustomPreference();
    }
    private static CustomPreference customPreference;
    private final String PREF_NAME = "nexters.hashgoals";
    public final static String PREF_INTRO_USER_AGREEMENT = "PREF_USER_AGREEMENT";
    public final static String PREF_MAIN_VALUE = "PREF_MAIN_VALUE";

    static Context mContext;

    private CustomPreference() {}

    public void remove(String key) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.clear();
        editor.commit();
    }

    public void put(String key, String value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,mContext.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public void put(String key, Boolean value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                mContext.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }

    public void put(String key, long value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                mContext.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putLong(key, value);
        editor.commit();
    }

    public void put(String key,int value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                mContext.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(key, value);
        editor.commit();
    }

    public String getValue(String key, String dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                mContext.MODE_PRIVATE);
        try {
            return pref.getString(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public long getValue(String key, long dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                mContext.MODE_PRIVATE);
        try {
            return pref.getLong(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }


    public boolean getValue(String key, boolean dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                mContext.MODE_PRIVATE);
        try {
            return pref.getBoolean(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }
    public int getValue(String key,int dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                mContext.MODE_PRIVATE);
        try {
            return pref.getInt(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }
}