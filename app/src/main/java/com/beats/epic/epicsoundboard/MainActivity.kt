package com.beats.epic.epicsoundboard

import android.graphics.Color
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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

    var mDrumSoundId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSounds()
        initSoundButtons()
    }

    private fun initSounds() {
        mDrumSoundId = mSoundPool.load(this, R.raw.drum_sound, 1)
    }

    private fun initSoundButtons() {
        for(i in 0..11) {
            val sb = layoutInflater.inflate(R.layout.sound_button, soundBoardGrid, false)
            sb.id = i
            sb.setOnTouchListener { v, event ->
                if(event.action == MotionEvent.ACTION_DOWN) {
                    mSoundPool.play(mDrumSoundId, 1f, 1f, 0, 0, 1f)
                    v.setBackgroundColor(Color.WHITE)
                } else if(event.action == MotionEvent.ACTION_UP) {
                    v.setBackgroundColor(Color.RED)
                }
                true
            }
            soundBoardGrid.addView(sb)
        }
    }
}
