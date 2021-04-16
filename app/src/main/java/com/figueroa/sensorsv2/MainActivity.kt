package com.figueroa.sensorsv2

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class MainActivity :
    AppCompatActivity(),
    SensorEventListener,
    View.OnClickListener,
    PopupMenu.OnMenuItemClickListener {

    // Sensor stuff
    private lateinit var sensorManager: SensorManager

    private lateinit var gyroscope: Sensor
    private lateinit var accelerometer: Sensor
    private lateinit var light: Sensor

    // Graph stuff
    private lateinit var graph: GraphView

    // Accelerometer stuff
    private lateinit var accelerometerXValues: BarGraphSeries<DataPoint>
    private lateinit var accelerometerYValues: BarGraphSeries<DataPoint>
    private lateinit var accelerometerZValues: BarGraphSeries<DataPoint>

    // Gyroscope stuff
    private lateinit var gyroscopeXValues: BarGraphSeries<DataPoint>
    private lateinit var gyroscopeYValues: BarGraphSeries<DataPoint>
    private lateinit var gyroscopeZValues: BarGraphSeries<DataPoint>

    // Light sensor stuff
    private lateinit var lightValues: LineGraphSeries<DataPoint>
    private var i: Double = .0

    // Sensor details stuff
    private lateinit var sensor: TextView
    private lateinit var name: TextView
    private lateinit var version: TextView
    private lateinit var manufacturer: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialising button
        val button: Button = findViewById(R.id.dropdown_menu_button)
        button.setOnClickListener(this)

        // Initialising sensors
        initSensorManager()
        initSensors()

        // Initialising graph and text views
        graph = findViewById(R.id.sensor_graph)
        sensor = findViewById(R.id.selected_sensor_text)
        name = findViewById(R.id.sensor_name)
        version = findViewById(R.id.sensor_version)
        manufacturer = findViewById(R.id.sensor_manufacturer)

        // Initialising accelerometer data
        initAccGraph()
    }

    // Functions
    private fun initSensorManager() {
        if (!this::sensorManager.isInitialized)
            sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
    }

    private fun initSensors() {
        if (!this::gyroscope.isInitialized)
            gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        if(!this::accelerometer.isInitialized)
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if(!this::light.isInitialized)
            light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    private fun initAccGraph() {
        if (graph.series.size > 0) graph.removeAllSeries()

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)

        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinX(0.0)
        graph.viewport.setMaxX(4.0)
        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.setMinY(-10.0)
        graph.viewport.setMaxY(10.0)

        name.text = accelerometer.name
        version.text = "Version " + accelerometer.version.toString()
        manufacturer.text = accelerometer.vendor

        accelerometerXValues = BarGraphSeries(arrayOf(
            DataPoint(1.0,.0)
        ))
        accelerometerXValues.color = Color.RED
        accelerometerXValues.isAnimated = true
        accelerometerXValues.title = "X Axis"

        accelerometerYValues = BarGraphSeries(arrayOf(
            DataPoint(2.0,.0)
        ))
        accelerometerYValues.color = Color.GREEN
        accelerometerYValues.isAnimated = true
        accelerometerYValues.title = "Y Axis"

        accelerometerZValues = BarGraphSeries(arrayOf(
            DataPoint(3.0,.0)
        ))
        accelerometerZValues.color = Color.BLUE
        accelerometerZValues.isAnimated = true
        accelerometerZValues.title = "Z Axis"

        graph.legendRenderer.isVisible = true
        graph.legendRenderer.align = LegendRenderer.LegendAlign.BOTTOM

        graph.addSeries(accelerometerXValues)
        graph.addSeries(accelerometerYValues)
        graph.addSeries(accelerometerZValues)
    }

    private fun initGyrGraph() {
        if (graph.series.size > 0) graph.removeAllSeries()

        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)

        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinX(0.0)
        graph.viewport.setMaxX(4.0)
        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.setMinY(-10.0)
        graph.viewport.setMaxY(10.0)

        name.text = gyroscope.name
        version.text = "Version " + gyroscope.version.toString()
        manufacturer.text = gyroscope.vendor

        gyroscopeXValues = BarGraphSeries(arrayOf(
            DataPoint(1.0,.0)
        ))
        gyroscopeXValues.color = Color.RED
        gyroscopeXValues.isAnimated = true
        gyroscopeXValues.title = "X Axis"
        gyroscopeXValues.isDrawValuesOnTop = true

        gyroscopeYValues = BarGraphSeries(arrayOf(
            DataPoint(2.0,.0)
        ))
        gyroscopeYValues.color = Color.GREEN
        gyroscopeYValues.isAnimated = true
        gyroscopeYValues.title = "Y Axis"
        gyroscopeYValues.isDrawValuesOnTop = true

        gyroscopeZValues = BarGraphSeries(arrayOf(
            DataPoint(3.0,.0)
        ))
        gyroscopeZValues.color = Color.BLUE
        gyroscopeZValues.isAnimated = true
        gyroscopeZValues.title = "Z Axis"
        gyroscopeZValues.isDrawValuesOnTop = true

        graph.legendRenderer.isVisible = true
        graph.legendRenderer.align = LegendRenderer.LegendAlign.BOTTOM

        graph.addSeries(gyroscopeXValues)
        graph.addSeries(gyroscopeYValues)
        graph.addSeries(gyroscopeZValues)
    }

    private fun initLuxGraph() {
        if (graph.series.size > 0) graph.removeAllSeries()

        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)

        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinX(.0)
        graph.viewport.setMaxX(40.0)

        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.setMinY(.0)
        graph.viewport.setMaxY(40000.0)

        name.text = light.name
        version.text = "Version " + light.version.toString()
        manufacturer.text = light.vendor

        lightValues = LineGraphSeries(arrayOf(
            DataPoint(.0,.0)
        ))
        lightValues.title = "Light"

        graph.addSeries(lightValues)
    }

    private fun unregisterAllSensorListeners() {
        sensorManager.unregisterListener(this, accelerometer)
        sensorManager.unregisterListener(this, gyroscope)
        sensorManager.unregisterListener(this, light)
        i = .0
    }

    // Sensor data handling stuff
    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_GYROSCOPE -> {
                val pointX = DataPoint(1.0, event.values[0].toDouble())
                val pointY = DataPoint(2.0, event.values[1].toDouble())
                val pointZ = DataPoint(3.0, event.values[2].toDouble())

                gyroscopeXValues.resetData(arrayOf(pointX))
                gyroscopeYValues.resetData(arrayOf(pointY))
                gyroscopeZValues.resetData(arrayOf(pointZ))
            }
            Sensor.TYPE_ACCELEROMETER -> {
                val pointX = DataPoint(1.0, event.values[0].toDouble())
                val pointY = DataPoint(2.0, event.values[1].toDouble())
                val pointZ = DataPoint(3.0, event.values[2].toDouble())

                accelerometerXValues.resetData(arrayOf(pointX))
                accelerometerYValues.resetData(arrayOf(pointY))
                accelerometerZValues.resetData(arrayOf(pointZ))
            }
            Sensor.TYPE_LIGHT -> {
                val lux = DataPoint(i++, event.values[0].toDouble())
                lightValues.appendData(lux, true, 40)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    // Button and menu options clicking stuff
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        unregisterAllSensorListeners()

        when (item?.itemId) {
            R.id.accelerometer_menu_item -> {
                sensor.text = "Accelerometer"
                initAccGraph()
            }
            R.id.gyroscope_menu_item -> {
                sensor.text = "Gyroscope"
                initGyrGraph()
            }
            R.id.light_sensor_menu_item -> {
                sensor.text = "Light sensor"
                initLuxGraph()
            }
        }
        return true
    }

    override fun onClick(v: View?) {
        val popupMenu = PopupMenu(this, v)
        popupMenu.setOnMenuItemClickListener(this)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.sensor_selection_menu, popupMenu.menu)
        popupMenu.show()
    }
}