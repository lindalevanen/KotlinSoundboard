package com.beats.epic.epicsoundboard

import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Handler
import android.view.View

/**
 * Created by Linda on 18/04/18.
 */
class SoundPlayer(private val context: Context) {

    private val mAttributes: AudioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
            .setUsage(AudioAttributes.USAGE_GAME)
            .build()

    private val mSoundPool: SoundPool =
            SoundPool.Builder()
                    .setAudioAttributes(mAttributes)
                    .setMaxStreams(10)
                    .build()

    private val mSoundArray: ArrayList<Sound> = ArrayList()

    lateinit private var mParentView: View

    fun getSounds(): ArrayList<Sound> {
        return mSoundArray
    }

    fun initsounds() {
        val sounds = arrayOf("bass_bling", "bass_drum", "closed_hi_hat", "drum_sound",
                "horn", "lo_crash", "needle_scratch", "open_hi_hat", "photo", "rapsnare",
                "rimshot", "scratch")

        for (sound in sounds) {
            val resourceId = context.resources.getIdentifier(sound, "raw", context.packageName)
            val soundId = mSoundPool.load(context, resourceId, 1)
            println(soundId)
            mSoundArray.add(Sound(sound, soundId))
        }
    }

    fun changeSound(path: String): Int {
        val id = mSoundPool.load(path, 1)
        println("new id: "+id)
        return id
    }

    fun addView(view: View) {
        mParentView = view
    }

    fun playSound(id: Int, recorded: Boolean) {
        val mgr = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC)
        val streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val volume = streamVolumeCurrent.toFloat() / streamVolumeMax
        val soundBView = mParentView.findViewById<View>(id)
        if(recorded) brieflyChangeBGColor(soundBView)
        val lol = mSoundPool.play(id, volume, volume, 0, 0, 1f)
        println(lol)

    }

    fun brieflyChangeBGColor(view: View) {
        view.setBackgroundColor(Color.WHITE)
        Handler().postDelayed({
            view.setBackgroundColor(Color.RED)
        }, 200)
    }

}