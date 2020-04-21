package dev.iteco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.Animation
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.*
import com.yandex.runtime.Error
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Session.RouteListener,
    com.yandex.mapkit.transport.bicycle.Session.RouteListener,
    DrivingSession.DrivingRouteListener {
    private val requestPoints by lazy {
        listOf(
            RequestPoint(Point(55.7361607, 37.59334030000001), RequestPointType.WAYPOINT, null),
            RequestPoint(Point(55.6116874, 37.6861486), RequestPointType.WAYPOINT, null),
            RequestPoint(Point(55.7563014, 37.61712989999999), RequestPointType.WAYPOINT, null),
            RequestPoint(Point(55.76954499999999, 37.710808), RequestPointType.WAYPOINT, null),
            RequestPoint(Point(55.7415369, 37.5987299), RequestPointType.WAYPOINT, null),
            RequestPoint(Point(55.7632314, 37.57659789999999), RequestPointType.WAYPOINT, null),
            RequestPoint(Point(55.75234409999999, 37.6269611), RequestPointType.WAYPOINT, null),
            RequestPoint(Point(55.75074900000001, 37.5900571), RequestPointType.WAYPOINT, null),
            RequestPoint(Point(55.8229932, 37.6398367), RequestPointType.WAYPOINT, null),
            RequestPoint(Point(55.6906804, 37.56144889999999), RequestPointType.WAYPOINT, null),
            RequestPoint(Point(55.777078, 37.613616), RequestPointType.WAYPOINT, null),
            RequestPoint(Point(55.8232979, 37.6319489), RequestPointType.WAYPOINT, null),
            RequestPoint(Point(55.7471415, 37.600247), RequestPointType.WAYPOINT, null),
            RequestPoint(Point(55.763479, 37.6153195), RequestPointType.WAYPOINT, null),
            RequestPoint(Point(55.72836479999999, 37.6012908), RequestPointType.WAYPOINT, null),
            RequestPoint(Point(55.7670851, 37.614201), RequestPointType.WAYPOINT, null)
        )
    }

    private val masstransitRouter by lazy {
        TransportFactory.getInstance().createMasstransitRouter()
    }

    private val pedestrianRouter by lazy {
        TransportFactory.getInstance().createPedestrianRouter()
    }

    private val bicycleRouter by lazy {
        TransportFactory.getInstance().createBicycleRouter()
    }

    private val drivingRouter by lazy {
        DirectionsFactory.getInstance().createDrivingRouter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey("474624db-adc8-4fd8-8862-0fd14f00dd17")
        MapKitFactory.initialize(this)
        TransportFactory.initialize(this)
        DirectionsFactory.initialize(this)

        setContentView(R.layout.activity_main)

        mapview.map.move(
            CameraPosition(Point(55.751244, 37.618423), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )

        for (requestPoint in requestPoints) {
            val mapObjects = mapview.map.mapObjects
            mapObjects.addPlacemark(requestPoint.point)
        }

        buttonBuildMasstransit.setOnClickListener {
            masstransitRouter.requestRoutes(
                requestPoints,
                MasstransitOptions(listOf(), listOf(), TimeOptions()),
                this
            )
        }

        buttonBuildBicycle.setOnClickListener {
            bicycleRouter.requestRoutes(requestPoints, this)
        }

        buttonBuildCar.setOnClickListener {
            drivingRouter.requestRoutes(requestPoints, DrivingOptions(), this)
        }

        buttonBuildPedestrian.setOnClickListener {
            pedestrianRouter.requestRoutes(requestPoints, TimeOptions(), this)
        }
    }

    override fun onStop() {
        super.onStop()
        mapview.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        mapview.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onMasstransitRoutesError(error: Error) {
        Log.e("MainActivity", "An error has arose: $error")
    }

    override fun onMasstransitRoutes(routes: MutableList<Route>) {
        Log.i("MainActivity", "Masstransit has result")
    }

    override fun onBicycleRoutesError(error: Error) {
        Log.e("MainActivity", "An error has arose: $error")
    }

    override fun onBicycleRoutes(routes: MutableList<com.yandex.mapkit.transport.bicycle.Route>) {
        Log.i("MainActivity", "Bicycle has result")
    }

    override fun onDrivingRoutesError(error: Error) {
        Log.e("MainActivity", "An error has arose: $error")
    }

    override fun onDrivingRoutes(routes: MutableList<DrivingRoute>) {
        Log.i("MainActivity", "Driving has result")
    }
}
