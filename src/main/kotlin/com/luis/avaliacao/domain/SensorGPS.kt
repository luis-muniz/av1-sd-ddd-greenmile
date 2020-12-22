package com.luis.avaliacao.domain

import java.util.*
import javax.persistence.*

@Entity
data class SensorGPS (
        @Id
        val id: Int,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "coordinate_id", referencedColumnName = "id")
        val coordinate: Coordinate? = null,
        ){
}