package com.luis.avaliacao.events

import com.luis.avaliacao.DTOs.NotificationDTO
import com.luis.avaliacao.domain.Event
import com.luis.avaliacao.domain.enums.EventType
import com.luis.avaliacao.repositories.EventRepository
import com.luis.avaliacao.repositories.RouteRepository
import com.luis.avaliacao.repositories.StopRepository
import com.luis.avaliacao.utils.haversineDistance
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

const val geoFence = 0.03 // 30 meters

@Component
class EventArrival(
        private val routeRepository: RouteRepository,
        private val stopRepository: StopRepository,
        private val eventRepository: EventRepository
):Observer{

    private val log = LoggerFactory.getLogger(EventArrival::class.java)

    fun processCoordinate(notificationDTO: NotificationDTO){
        val routeId = notificationDTO.routeId
        val lastCoordinateVehicle = notificationDTO.lastCoordinateSensorGPSFromVehicle
        val actualCoordinateVehicle = notificationDTO.actualCoordinateSensorGPSFromVehicle

        val isSamePlace = (lastCoordinateVehicle.longitude == actualCoordinateVehicle.longitude && lastCoordinateVehicle.latitude == actualCoordinateVehicle.latitude)

        if (isSamePlace){
            val route = this.routeRepository.findById(routeId).get()
            route.stops.filter { stop ->
                stop.arrivalAt == null && haversineDistance(actualCoordinateVehicle.latitude,actualCoordinateVehicle.longitude,stop.coordinate.latitude,stop.coordinate.longitude) <= geoFence
            }.map { stop ->
                val updateStop = stop.copy(arrivalAt = Date())
                this.stopRepository.save(updateStop)
                this.log.info("++++++++++++++++++++++++++++++++++++++++")
                this.log.info("-----Event Arrival--------")
                this.log.info("{} arrive at stop in client: {}", route.vehicle.plate, stop.address)
                this.log.info("++++++++++++++++++++++++++++++++++++++++")
                val newEvent = Event(eventType = EventType.ARRIVE, `when` = Date())
                this.eventRepository.save(newEvent)
            }
        }
    }

    override fun update(o: Observable?, notificationDTO: Any?) {
        if (o != null && notificationDTO != null){
            this.processCoordinate(notificationDTO as NotificationDTO)
        }
    }

}