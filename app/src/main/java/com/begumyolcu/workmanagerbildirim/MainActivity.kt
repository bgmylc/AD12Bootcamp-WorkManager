package com.begumyolcu.workmanagerbildirim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import com.begumyolcu.workmanagerbildirim.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var request: WorkRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            buttonAyarla.setOnClickListener {
                if (dakikaKontrol()) {
                    val constraints = Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .build()

                    request = PeriodicWorkRequestBuilder<BildirimWorker>(
                        editTextAralik.text.toString().toLong(),
                        TimeUnit.MINUTES
                    ).setConstraints(constraints).build()

                    WorkManager.getInstance(this@MainActivity)
                        .enqueueUniquePeriodicWork(
                            "com.begumyolcu.workmanagerbildirim.BildirimWorker",
                            ExistingPeriodicWorkPolicy.KEEP, request as PeriodicWorkRequest
                        )
                }
            }
        }
    }


    private fun dakikaKontrol(): Boolean {
        var dogruMu = false
        binding.apply {
            val dakikaText = editTextAralik.text
            if (dakikaText.isEmpty()) {
                Snackbar.make(constraintLayout, "Aralık boş olamaz!", Snackbar.LENGTH_LONG).show()
            } else if (dakikaText.toString().toInt() < 15) {
                Snackbar.make(
                    constraintLayout,
                    "Aralık 15 dakikadan kısa olamaz!",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                dogruMu = true
            }
        }
        return dogruMu
    }
}