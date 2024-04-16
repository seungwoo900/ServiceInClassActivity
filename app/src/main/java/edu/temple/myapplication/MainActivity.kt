package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    val handler = Handler(Looper.getMainLooper()) {

        true
    }

    var timerBinder: TimerService.TimerBinder? = null
//    var isConnected = false

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder = service as TimerService.TimerBinder
//            isConnected = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
//            isConnected = false
            timerBinder = null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )

        findViewById<Button>(R.id.startButton).setOnClickListener {
            timerBinder?.start(100)
//            if (isConnected) timerBinder.start(100)
        }

        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            timerBinder?.pause()
//            if (isConnected) timerBinder.pause()
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            timerBinder?.stop()
//            if (isConnected) timerBinder.stop()
        }
    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_start_timer -> Toast.makeText(this, "Starting timer", Toast.LENGTH_SHORT).show()

            R.id.action_pause_timer -> Toast.makeText(this, "Pausing timer", Toast.LENGTH_SHORT).show()

            R.id.action_stop_timer -> {
                AlertDialog.Builder(this)
                    .setTitle("Stop confirmation")
                    .setMessage("Are you sure you would like to stop this timer")
                    .setPositiveButton("Yes"){dialog,_ -> Toast.makeText(this, "Stopping...", Toast.LENGTH_SHORT).show(); dialog.dismiss()}
                    .setNegativeButton("Never mind"){dialog,_ -> dialog.cancel()}
                    .show()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}