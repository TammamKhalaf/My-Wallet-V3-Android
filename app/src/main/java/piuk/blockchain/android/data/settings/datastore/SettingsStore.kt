package piuk.blockchain.android.data.settings.datastore

import info.blockchain.wallet.api.data.Settings
import io.reactivex.Observable
import piuk.blockchain.android.utils.Optional

interface SettingsStore {

    fun getSettings(): Observable<Optional<Settings>>

}