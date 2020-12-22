package com.luis.avaliacao.domain

import javax.persistence.*

@Entity
data class Vehicle (
        @Id
        val id: Int,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "sensorGPS_id", referencedColumnName = "id")
        val sensorGPS: SensorGPS,

        val brand: String,
        val model: String,
        val plate: String,
        ){
}