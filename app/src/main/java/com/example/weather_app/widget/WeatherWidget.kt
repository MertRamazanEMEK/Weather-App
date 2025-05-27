package com.example.weather_app.widget
import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.weather_app.R

class WeatherWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId,
                weatherId = 800,
                temperature = 23.5,
                cityName = "İstanbul"
            )
        }
    }

    companion object {
        @SuppressLint("RemoteViewLayout")
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            weatherId: Int,
            temperature: Double? = null,
            cityName: String? = null
        ) {
            val views = RemoteViews(context.packageName, R.layout.weather_widget)

            val iconRes = getWeatherIconResource(weatherId)
            views.setImageViewResource(R.id.imgWeatherIcon, iconRes) // ID XML'e göre güncellendi

            val tempText = temperature?.let { "%.1f°C".format(it) } ?: "--°C"
            views.setTextViewText(R.id.tvWidgetTemp, tempText)
            views.setTextViewText(R.id.tvWidgetCity, cityName ?: "Bilinmiyor")

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun getWeatherIconResource(weatherId: Int): Int {
            return when (weatherId) {
                in 200..232 -> R.drawable.ic_weather_storm
                in 300..531 -> R.drawable.ic_weather_rainy
                in 600..622 -> R.drawable.ic_weather_snowy
                in 701..781 -> R.drawable.ic_weather_cloudy
                800 -> R.drawable.ic_weather_sunny
                in 801..804 -> R.drawable.ic_weather_cloudy
                else -> R.drawable.ic_weather_sunny
            }
        }
    }
}
