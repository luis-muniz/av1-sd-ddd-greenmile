package com.luis.avaliacao.events

import com.luis.avaliacao.DTOs.NotificationDTO
import com.luis.avaliacao.domain.Event
import com.luis.avaliacao.domain.enums.EventType
import com.luis.avaliacao.repositories.EventRepository
import com.luis.avaliacao.repositories.RouteRepository
import com.luis.avaliacao.repositories.StopRepository
import com.luis.avaliacao.repositories.TelephoneRepository
import com.luis.avaliacao.utils.haversineDistance
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

const val limitDistanceFromVehicle = 0.05 // = 50m (0.1 = 100m)
const val timeLimitAwayFromVehicle = 20000 // = 20s (1000 = 1s)

@Component
class EventEndangeredEmployee(
        private val routeRepository: RouteRepository,
        private val eventRepository: EventRepository,
        private val telephoneRepository: TelephoneRepository,
) : Observer {

    private val log = LoggerFactory.getLogger(EventArrival::class.java)

    fun processCoordinate(notificationDTO: NotificationDTO) {
        val routeId = notificationDTO.routeId
        val actualCoordinateVehicle = notificationDTO.actualCoordinateSensorGPSFromVehicle
        val actualCoordinateTelephone = notificationDTO.actualCoordinateSensorGPSFromTelephone

        val distanceEmployeeFromVehicle = haversineDistance(actualCoordinateVehicle.latitude, actualCoordinateVehicle.longitude, actualCoordinateTelephone.latitude, actualCoordinateTelephone.longitude)

        val route = this.routeRepository.findById(routeId).get()

        if (distanceEmployeeFromVehicle > limitDistanceFromVehicle) {

            // verify if this employee is in the client
            for (stop in route.stops) {
                if (haversineDistance(actualCoordinateTelephone.latitude, actualCoordinateTelephone.longitude, stop.coordinate.latitude, stop.coordinate.longitude) <= geoFence) {
                    return
                }
            }

            // verify if this employee is at break time
            if (this.isWorkDay(actualCoordinateTelephone.datePing)){
                return
            }

            // verify time limit
            if (route.employee.telephone.lastTimeCoordinateAwayFromVehicle == null) {
                val updateLastTimeCoordinateAwayFromVehicle = route.employee.telephone.copy(lastTimeCoordinateAwayFromVehicle = actualCoordinateTelephone.datePing)
                this.telephoneRepository.save(updateLastTimeCoordinateAwayFromVehicle)
                return
            }

            if (actualCoordinateTelephone.datePing.getTime() - route.employee.telephone.lastTimeCoordinateAwayFromVehicle.getTime() >= timeLimitAwayFromVehicle) {
                this.log.info("++++++++++++++++++++++++++++++++++++++++")
                this.log.info("-----Event Endanger Employee--------")
                this.log.info("{} may is in danger, he is {} seconds away from the vehicle {}", route.employee.name, (actualCoordinateTelephone.datePing.getTime() - route.employee.telephone.lastTimeCoordinateAwayFromVehicle.getTime())/1000,route.vehicle.plate)
                this.log.info("++++++++++++++++++++++++++++++++++++++++")
                val newEvent = Event(eventType = EventType.ENDANGERED_EMPLOYEE, `when` = Date())
                this.eventRepository.save(newEvent)
            }
        } else {
            if (route.employee.telephone.lastTimeCoordinateAwayFromVehicle != null) {
                val resetLastTimeCoordinateAwayFromVehicleInTelephone = route.employee.telephone.copy(lastTimeCoordinateAwayFromVehicle = null)
                this.telephoneRepository.save(resetLastTimeCoordinateAwayFromVehicleInTelephone)
            }
        }
    }

    override fun update(o: Observable?, notificationDTO: Any?) {
        if (o != null && notificationDTO != null) {
            this.processCoordinate(notificationDTO as NotificationDTO)
        }
    }

    private fun isWorkDay(date: Date):Boolean{
        // WorkDay
        // Mon - Fry: 8h - 12h and 14h - 18h
        if (date.day != 6 && date.day != 5){
            if ((date.hours >= 8 && date.hours <= 12)|| (date.hours >= 14 && date.hours <= 18)){
                return true
            }
        }
        return false
    }

}