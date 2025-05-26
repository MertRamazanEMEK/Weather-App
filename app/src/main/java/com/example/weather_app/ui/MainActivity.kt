package com.example.weather_app.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weather_app.BuildConfig
import com.example.weather_app.R
import com.example.weather_app.api.RetrofitInstance
import com.example.weather_app.databinding.ActivityMainBinding
import com.example.weather_app.model.WeatherResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val apiKey = BuildConfig.OPEN_WEATHER_API_KEY

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getLastLocationAndWeather()
        } else {
            Toast.makeText(this, "Konum izni reddedildi", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.btnGetWeather.setOnClickListener {
            val city = binding.etCityName.text.toString().trim()
            if (city.isNotEmpty()) {
                getWeather(city)
            } else {
                Toast.makeText(this, "Lütfen şehir adı girin", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGetLocationWeather.setOnClickListener {
            checkLocationPermissionAndFetchWeather()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.etCityName)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun checkLocationPermissionAndFetchWeather() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getLastLocationAndWeather()
            }

            else -> {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun getLastLocationAndWeather() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Konum izni verilmedi", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    getWeatherByCoordinates(location.latitude, location.longitude)
                } else {
                    Toast.makeText(this, "Konum alınamadı", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: SecurityException) {
            Toast.makeText(this, "Konum izni yok: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getWeatherByCoordinates(lat: Double, lon: Double) {
        val call = RetrofitInstance.api.getWeatherByCoordinates(lat, lon, apiKey)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    weatherResponse?.let {
                        updateUI(it)
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Hava durumu bulunamadı",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Hata: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getWeather(cityName: String) {
        val call = RetrofitInstance.api.getWeatherByCity(cityName, apiKey)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    weatherResponse?.let {
                        updateUI(it)
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Hava durumu bulunamadı",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Hata: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(data: WeatherResponse) {
        binding.tvCityName.text = data.name
        binding.tvTemperature.text = "${data.main.temp}°C"
        binding.tvDescription.text = data.weather[0].description.replaceFirstChar { it.uppercase() }
        binding.tvHumidity.text = "${data.main.humidity}%"
        binding.tvWindSpeed.text = "${data.wind.speed} m/s"
        binding.tvPressure.text = "${data.main.pressure} hPa"
    }
}