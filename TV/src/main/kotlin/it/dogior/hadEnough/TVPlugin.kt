package it.dogior.hadEnough

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.lagradost.cloudstream3.CommonActivity.activity

@CloudstreamPlugin
class TVPlugin : Plugin() {
    private val sharedPref = activity?.getSharedPreferences("TV", Context.MODE_PRIVATE)
    private val playlistsToLang = mapOf(
        "playlist_mexico.m3u8" to "mx",
        "playlist_spain.m3u8" to "es",
        "playlist_usa.m3u8" to "us"
         )

    override fun load(context: Context) {
        val playlistSettings = playlistsToLang.keys.associateWith {
            sharedPref?.getBoolean(it, false) ?: false
        }
        val selectedPlaylists = playlistSettings.filter { it.value }.keys
        val selectedLanguages = selectedPlaylists.map { playlistsToLang[it] }
        val lang = if(selectedLanguages.isNotEmpty()){
            if(selectedLanguages.all { it == selectedLanguages.first() && it != null }){
                selectedLanguages.first()!! } else{ "un" }
        } else{ "un" }

        registerMainAPI(TV(selectedPlaylists.toList(), lang, sharedPref))

        val activity = context as AppCompatActivity
        openSettings = {
            val frag = Settings(this, sharedPref, playlistsToLang.keys.toList())
            frag.show(activity.supportFragmentManager, "Frag")
        }
    }
}
