package com.luis.avaliacao.DTOs

import com.luis.avaliacao.domain.SensorGPS

data class SensorsRoutesDTO(
        val coordinateSensorGPSVehicle: SensorGPS,
        val coordinateSensorGPSTelephone: SensorGPS,
) {
}