package com.luis.avaliacao.domain

import java.util.*
import javax.persistence.*

@Entity
data class Stop (
        @Id
        val id: Int,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "coordinate_id", referencedColumnName = "id")
        val coordinate: Coordinate,

        val address: String,
        val arrivalAt: Date? = null,
        val departureAt: Date? = null,
        ){
}