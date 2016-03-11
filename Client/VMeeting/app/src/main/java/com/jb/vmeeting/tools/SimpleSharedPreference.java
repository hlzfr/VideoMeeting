package com.jb.vmeeting.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.jb.vmeeting.app.App;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class SimpleSharedPreference {
	private static final Map<String, SimpleSharedPreference> sCacheSimpleSharedPreference = new HashMap<String, SimpleSharedPreference>();
	
	public static String DEFAULT_NAME = "default";
	
	public static String DEFAULT_STRING = "";
	public static int DEFAULT_INT = 0;
	public static long DEFAULT_LONG = 0L;
	public static float DEFAULT_FLOAT = 0F;
	public static boolean DEFAULT_BOOLEAN = false;
	
	private String mName = DEFAULT_NAME;
	private SharedPreferences mPref;

	private SimpleSharedPreference(Context ctx, String name) {
		SharedPreferences pref = null;
		if (DEFAULT_NAME.equals(name) || TextUtils.isEmpty(name)) {
			pref = PreferenceManager.getDefaultSharedPreferences(ctx);
		} else {
			pref = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
			mName = name;
		}
		mPref = pref;
    }

	public static SimpleSharedPreference getInstance(Context ctx, String name) {
        SimpleSharedPreference simpleSharedPreference = null;
        synchronized (sCacheSimpleSharedPreference) {
            simpleSharedPreference = sCacheSimpleSharedPreference.get(name);
            if (simpleSharedPreference == null) {
                simpleSharedPreference = new SimpleSharedPreference(ctx, name);
                sCacheSimpleSharedPreference.put(name, simpleSharedPreference);
            }
        }
		return simpleSharedPreference;
	}

	public static SimpleSharedPreference getInstance() {
		return getInstance(App.getInstance());
	}

	public static SimpleSharedPreference getInstance(String name) {
		return getInstance(App.getInstance(), name);
	}

	public static SimpleSharedPreference getInstance(Context ctx) {
		return getInstance(ctx, DEFAULT_NAME);
	}

	/**
	 * <b>Attention : </b>only boolean, string, int, float, character, long.
	 *         if string set wanted, use {@link #putStringSet(String, Set)}
	 * @param key
	 * @param value only boolean, string, int, float, character, long
	 */
	public void put(String key, Object value) {
		if (value instanceof Boolean) {
			mPref.edit().putBoolean(key, (Boolean) value).apply();
		} else if (value instanceof String) {
			mPref.edit().putString(key, ((String) value)).apply();
		} else if (value instanceof Integer) {
			mPref.edit().putInt(key, (Integer) value).apply();
		} else if (value instanceof Float) {
			mPref.edit().putFloat(key, (Float) value).apply();
		} else if (value instanceof Character) {
			String strValue = (Character.toString((Character) value));
			mPref.edit().putString(key, strValue)
					.apply();
		} else if (value instanceof Long) {
			mPref.edit().putLong(key, (Long) value).apply();
		} else {
            throw new RuntimeException("value should only be bool, string, int, float, character and long");
        }
	}
	
	public void putStringSet(String key, Set<String> value) {
		mPref.edit().putStringSet(key, value).apply();
	}
	
	public SharedPreferences getSharedPreferences() {
		return mPref;
	}
	
	public String getName() {
		return mName;
	}
	
	public String getString(String key) {
		return mPref.getString(key, DEFAULT_STRING);
	}

	public int getInt(String key) {
		return mPref.getInt(key, DEFAULT_INT);
	}

	public boolean getBoolean(String key) {
		return mPref.getBoolean(key, DEFAULT_BOOLEAN);
	}

	public float getFloat(String key) {
		return mPref.getFloat(key, DEFAULT_FLOAT);
	}
	
	public long getLong(String key) {
		return mPref.getLong(key, DEFAULT_LONG);
	}

	public String getString(String key, String defaultValue) {
		return mPref.getString(key, defaultValue);
	}

	public int getInt(String key, int defaultValue) {
		return mPref.getInt(key, defaultValue);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return mPref.getBoolean(key, defaultValue);
	}

	public float getFloat(String key, float defaultValue) {
		return mPref.getFloat(key, defaultValue);
	}
	
	public long getLong(String key, long defaultValue) {
		return mPref.getLong(key, defaultValue);
	}

	public Set<String> getStringSet(String key, Set<String> defaultValue) {
		return mPref.getStringSet(key, defaultValue);
	}
	
	public Map<String, ?> getAll() {
		return mPref.getAll();
	}
	
	public boolean contains(String name) {
		return mPref.contains(name);
	}

	public void remove(String key) {
		Editor editor = mPref.edit();
		editor.remove(key);
		editor.apply();
	}

	public void clear() {
		Editor editor = mPref.edit();
		editor.clear();
        editor.apply();
	}
	
	@Override
	public boolean equals(Object o) {
		return o != null && o instanceof SimpleSharedPreference && ((SimpleSharedPreference) o).getName().equals(this.getName());
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}
}
