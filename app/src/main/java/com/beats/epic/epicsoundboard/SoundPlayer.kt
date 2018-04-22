package com.beats.epic.epicsoundboard

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool

/**
 * Created by Linda on 18/04/18.
 */
class SoundPlayer(val context: Context) {

    val mAttributes: AudioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
            .setUsage(AudioAttributes.USAGE_GAME)
            .build()

    val mSoundPool: SoundPool =
            SoundPool.Builder()
                    .setAudioAttributes(mAttributes)
                    .setMaxStreams(10)
                    .build()

    private val mSoundArray: ArrayList<Sound> = ArrayList()

    fun getSounds(): ArrayList<Sound> {
        return mSoundArray
    }

    fun initsounds() {
        val sounds = arrayOf("bass_bling", "bass_drum", "closed_hi_hat", "drum_sound",
                "horn", "lo_crash", "needle_scratch", "open_hi_hat", "photo", "rapsnare",
                "rimshot", "scratch")

        for (sound in sounds) {
            val resId = context.getResources().getIdentifier(sound, "raw", context.packageName);
            val soundId = mSoundPool.load(context, resId, 1)
            mSoundArray.add(Sound(sound, soundId))
        }
    }

    fun playSound(id: Int) {
        val mgr = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC)
        val streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val volume = streamVolumeCurrent.toFloat() / streamVolumeMax
        mSoundPool.play(id, volume, volume, 0, 0, 1f)

    }

}