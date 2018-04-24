package com.beats.epic.epicsoundboard

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.view.View

class MainActivity : AppCompatActivity() {

    private var recording = false
    private var startTime: Long = 0
    private var recordedSounds: HashMap<Long, Int> = HashMap()
    private var editMode = false

    lateinit private var mSoundPlayer: SoundPlayer
    lateinit var mRecordPlayer: RecordPlayer
    lateinit var editTarget: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            if (recording) {
                recButton.text = getString(R.string.rec_button_start)
                stopRecording()
            } else {
                recButton.text = getString(R.string.rec_button_stop)
                startRecording()
            }
        }
        clearButton.setOnClickListener {
            if (mRecordPlayer.isPlaying()) {
                mRecordPlayer.stopLoop()
                clearButton.text = resources.getText(R.string.empty_button)
            }
        }
        editButton.setOnClickListener {
            editMode = !editMode
        }
    }

    private fun stopRecording() {
        recording = false
        mRecordPlayer.init()
        mRecordPlayer.playSounds(recordedSounds)
    }

    private fun startRecording() {
        recording = true
        recordedSounds = HashMap()
        startTime = System.currentTimeMillis()
        clearButton.text = resources.getText(R.string.clear_button)
    }

    private fun initSoundButtons() {

        mSoundPlayer.addView(soundBoardGrid)
        for(i in 0..11) {
            val sb = layoutInflater.inflate(R.layout.sound_button, soundBoardGrid, false)
            if (i >= mSoundPlayer.getSounds().size) {
                sb.id = mSoundPlayer.getSounds()[0].id
            } else {
                sb.id = mSoundPlayer.getSounds()[i].id
            }

            sb.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    if (editMode) {
                        editTarget = v
                        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivityForResult(intent, 0)
                        }
                    } else {
                        mSoundPlayer.playSound(v.id, false)
                        if(recording) {
                            val time = System.currentTimeMillis() - startTime
                            recordedSounds.put(time, v.id)
                        }
                    }
                    v.setBackgroundColor(Color.WHITE)
                } else if (event.action == MotionEvent.ACTION_UP) {
                    v.setBackgroundColor(Color.RED)
                }
                true
            }
            soundBoardGrid.addView(sb)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            val result = data?.data
            if (result != null) {
                editTarget.id = mSoundPlayer.changeSound(result.path)
            }
        }
    }

    /*
    * Ref: https://developer.android.com/guide/topics/media/mediarecorder.html#example,
    * for recording audio
    * */
}
