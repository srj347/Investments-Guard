package datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first

object LocalPreference {
    private lateinit var dataStore: DataStore<Preferences>

    fun init(dataStore: DataStore<Preferences>){
        this.dataStore = dataStore
    }

//    suspend fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
//
//    }

    suspend fun <T> getFirstPreference(key: Preferences.Key<T>,defaultValue: T): T {
        return dataStore.data.first()[key] ?: defaultValue
    }

    suspend fun <T> putPreference(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}