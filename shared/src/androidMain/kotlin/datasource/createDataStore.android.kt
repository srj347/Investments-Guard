package datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

/**
 * Gets the singleton DataStore instance, creating it if necessary.
 */
fun createDataStore(context: Context): DataStore<Preferences> =
    createDataStore {
        context.filesDir.resolve(dataStoreFileName).absolutePath
    }