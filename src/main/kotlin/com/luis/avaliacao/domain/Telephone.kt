package com.luis.avaliacao.domain

import java.util.*
import javax.persistence.*

@Entity
data class Telephone (
        @Id
        val id: Int,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "sensorGPS_id", referencedColumnName = "id")
        val sensorGPS: SensorGPS,

        val lastTimeCoordinateAwayFromVehicle: Date? = null,

        val brand: String,
        val model: String,
        val imei: String,
        ){
}