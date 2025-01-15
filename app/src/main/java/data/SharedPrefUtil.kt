package data

import android.content.Context

object SharedPrefUtil {
    private const val PREF_NAME = "bmr_prefs"

    fun saveData(context: Context, data: Map<String, Any>) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .apply {
                data.forEach { (key, value) ->
                    when (value) {
                        is String -> putString(key, value)
                        is Boolean -> putBoolean(key, value)
                        is Float -> putFloat(key, value)
                    }
                }
                apply()
            }
    }
}