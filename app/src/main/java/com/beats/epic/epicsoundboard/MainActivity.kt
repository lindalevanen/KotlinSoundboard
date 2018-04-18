package com.beats.epic.epicsoundboard

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var recording = false
    var startTime: Long = 0
    val recordedSounds: HashMap<Int, Long> = HashMap()

    lateinit var mSoundPlayer: SoundPlayer
    //lateinit var mRecordPlayer: RecordPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSoundPlayer()
        initActionButtons()
        initSoundButtons()
    }

    private fun initSoundPlayer() {
        mSoundPlayer = SoundPlayer(this)
        mSoundPlayer.initsounds()
    }

    private fun initActionButtons() {
        recButton.setOnClickListener {
            if (recording) {
                recButton.setText("REC")
                stopRecording()
            } else {
                recButton.setText("STOP")
                startRecording()
            }
        }
    }

    private fun stopRecording() {
        recording = false
        //val player = RecordPlayer(recordedSounds, mSoundPlayer)
    }

    private fun startRecording() {
        recording = true
        startTime = System.currentTimeMillis();
    }

    private fun initSoundButtons() {

        for(i in 0..11) {
            val sb = layoutInflater.inflate(R.layout.sound_button, soundBoardGrid, false)
            if(i >= mSoundPlayer.getSounds().size) {
                sb.id = mSoundPlayer.getSounds()[0].id
            } else {
                sb.id = mSoundPlayer.getSounds()[i].id
            }

            sb.setOnTouchListener { v, event ->
                if(event.action == MotionEvent.ACTION_DOWN) {
                    mSoundPlayer.playSound(v.id)
                    if(recording) {
                        recordedSounds.put(v.id, System.currentTimeMillis() - startTime)
                    }
                    v.setBackgroundColor(Color.WHITE)
                } else if(event.action == MotionEvent.ACTION_UP) {
                    v.setBackgroundColor(Color.RED)
                }
                true
            }
            soundBoardGrid.addView(sb)
        }
    }

    /*
    * Ref: https://developer.android.com/guide/topics/media/mediarecorder.html#example,
    * for recording audio
    * */
}
