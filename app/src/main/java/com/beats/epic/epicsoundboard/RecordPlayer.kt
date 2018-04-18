package com.beats.epic.epicsoundboard

import android.os.Handler


/**
 * Created by Linda on 18/04/18.
 */
class RecordPlayer(val sounds: HashMap<Int, Long>, val soundPlayer: SoundPlayer) {

    init {
    }

    fun playSounds() {
        //TODO (WIP)
        for((key, value) in sounds) {
            Handler().postDelayed(Runnable {
                soundPlayer.playSound(key)
            }, value)
        }
    }

    fun stopPlay() {
        //TODO
    }

}