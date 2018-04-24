package com.beats.epic.epicsoundboard

import android.os.Handler

/**
 * Created by Linda on 18/04/18.
 */
class RecordPlayer(private val soundPlayer: SoundPlayer) {

    private var mKeepPlaying = false;
    private var mAllSounds: HashMap<Long, Int> = HashMap()
    private var mSoundGroups: HashMap<Int, HashMap<Long,Int>> = HashMap()
    private var mPlayCount = 0
    private var mStoppedPlayCount = 0
    private var mStartTimeStamp: Long? = null

    fun isPlaying(): Boolean { return mKeepPlaying }

    fun getStartTimeStamp(): Long? { return mStartTimeStamp }

    fun playSounds() {
        val last: Long? = mAllSounds.keys.max()
        val pc = mPlayCount
        mStartTimeStamp = System.currentTimeMillis()
        for((key, value) in mAllSounds) {
            Handler().postDelayed({
                if(pc > mStoppedPlayCount && mKeepPlaying) {
                    soundPlayer.playSound(value, true)
                }
                if (pc > mStoppedPlayCount && key == last && mKeepPlaying) {
                    playSounds()
                }
            }, key)
        }
    }

    fun addSounds(sounds: HashMap<Long, Int>) {
        mAllSounds.putAll(sounds)
        mSoundGroups.put(mSoundGroups.size, sounds)
    }

    fun clearSounds() {
        mAllSounds = HashMap()
        mSoundGroups = HashMap()
        mStartTimeStamp = null
    }

    fun getSoundGroups(): HashMap<Int, HashMap<Long,Int>> {
        return mSoundGroups
    }

    fun startLoop() {
        mPlayCount++
        mKeepPlaying = true
    }

    fun stopLoop() {
        mStoppedPlayCount = mPlayCount
        mStartTimeStamp = null
        mKeepPlaying = false
    }

}