// core/token/src/main/java/com/rndev/simulatorbank/core/token/SimpleTokenStore.kt
package io.rndev.core.common

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext

@Singleton
class SimpleTokenStore @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenProvider {

    private val prefs = context.getSharedPreferences("mockbank", Context.MODE_PRIVATE)

    override fun getToken(): String? = prefs.getString("token", null)

    override fun saveToken(token: String) {
        prefs.edit { putString("token", token) }
    }
}
