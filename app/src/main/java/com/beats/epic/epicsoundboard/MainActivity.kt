package com.beats.epic.epicsoundboard

import android.graphics.Color
import android.media.AudioManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private var recording = false
    private var startTime: Long = 0
    private var recordedSounds: HashMap<Long, Int> = HashMap()

    lateinit private var mSoundPlayer: SoundPlayer
    lateinit var mRecordPlayer: RecordPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        initPlayers()
        initActionButtons()
        initSoundButtons()
    }

    private fun initPlayers() {
        mSoundPlayer = SoundPlayer(this)
        mSoundPlayer.initsounds()
        mRecordPlayer = RecordPlayer(mSoundPlayer)
    }

    private fun initActionButtons() {
        recButton.setOnClickListener {
            recButton.visibility = View.GONE
            okButton.visibility = View.VISIBLE
            cancelButton.visibility = View.VISIBLE
            clearButton.visibility = View.GONE
            playButton.visibility = View.GONE
            stopButton.visibility = View.GONE
            startRecording()
        }
        cancelButton.setOnClickListener {
            recButton.visibility = View.VISIBLE
            okButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
            if(mRecordPlayer.getSoundGroups().size > 0) {
                stopButton.visibility = View.VISIBLE
                clearButton.visibility = View.VISIBLE
            }
            cancelRecording()
        }
        okButton.setOnClickListener {
            recButton.visibility = View.VISIBLE
            okButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
            stopButton.visibility = View.VISIBLE
            clearButton.visibility = View.VISIBLE
            stopRecording()
        }
        stopButton.setOnClickListener {
            stopButton.visibility = View.GONE
            playButton.visibility = View.VISIBLE
            mRecordPlayer.stopLoop()
        }
        playButton.setOnClickListener {
            playButton.visibility = View.GONE
            stopButton.visibility = View.VISIBLE
            mRecordPlayer.startLoop()
            mRecordPlayer.playSounds()
        }
        clearButton.setOnClickListener {
            clearButton.visibility = View.GONE
            stopButton.visibility = View.GONE
            playButton.visibility = View.GONE
            mRecordPlayer.stopLoop()
            mRecordPlayer.clearSounds()
        }
    }

    private fun cancelRecording() {
        recording = false
    }

    private fun stopRecording() {
        recording = false
        mRecordPlayer.addSounds(recordedSounds)
        if(!mRecordPlayer.isPlaying()) {
            mRecordPlayer.startLoop()
            mRecordPlayer.playSounds()
        }
    }

    private fun startRecording() {
        recording = true
        recordedSounds = HashMap()
        val sts = mRecordPlayer.getStartTimeStamp()
        if(sts != null) {
            startTime = sts
        } else {
            startTime = System.currentTimeMillis()
        }
        clearButton.text = resources.getText(R.string.clear_button)
    }

    private fun initSoundButtons() {

        mSoundPlayer.addView(soundBoardGrid)
        for(i in 0..11) {
            val sb = layoutInflater.inflate(R.layout.sound_button, soundBoardGrid, false)
            if(i >= mSoundPlayer.getSounds().size) {
                sb.id = mSoundPlayer.getSounds()[0].id
            } else {
                sb.id = mSoundPlayer.getSounds()[i].id
            }

            sb.setOnTouchListener { v, event ->
                if(event.action == MotionEvent.ACTION_DOWN) {
                    mSoundPlayer.playSound(v.id, false)
                    if(recording) {
                        val sts = mRecordPlayer.getStartTimeStamp()
                        if(sts != null && sts > startTime) {
                            startTime = sts
                        }
                        val time = System.currentTimeMillis() - startTime
                        recordedSounds.put(time, v.id)
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
