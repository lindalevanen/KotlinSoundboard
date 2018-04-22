package com.beats.epic.epicsoundboard

import android.os.Handler


/**
 * Created by Linda on 18/04/18.
 */
class RecordPlayer(val soundPlayer: SoundPlayer) {

    init {
    }

    fun playSounds(sounds: HashMap<Long, Int>) {
        for((key, value) in sounds) {
            Handler().postDelayed(Runnable {
                soundPlayer.playSound(value)
            }, key)
        }
    }

    fun stopPlay() {
        //TODO
    }

}