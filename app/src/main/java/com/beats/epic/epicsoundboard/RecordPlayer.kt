package com.beats.epic.epicsoundboard

import android.os.Handler

/**
 * Created by Linda on 18/04/18.
 */
class RecordPlayer(private val soundPlayer: SoundPlayer) {

    private var keepPlaying = false;

    fun init() {
        startLoop()
    }

    fun isPlaying(): Boolean { return keepPlaying }

    fun playSounds(sounds: HashMap<Long, Int>) {
        val last: Long? = sounds.keys.max()
        for((key, value) in sounds) {
            Handler().postDelayed({
                soundPlayer.playSound(value, true)
                if (key == last && keepPlaying) {
                    playSounds(sounds)
                }
            }, key)
        }
    }

    fun startLoop() {
        keepPlaying = true
    }

    fun stopLoop() {
        keepPlaying = false
    }

}