package com.luis.avaliacao.domain

import javax.persistence.*

@Entity
data class Route(
        @Id
        val id: Int,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "vehicle_id",referencedColumnName = "id")
        val vehicle: Vehicle,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "employee_id",referencedColumnName = "id")
        val employee: Employee,

        @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
        @JoinColumn(name = "stop_id",referencedColumnName = "id")
        val stops: List<Stop>,

        val name: String
) {
}