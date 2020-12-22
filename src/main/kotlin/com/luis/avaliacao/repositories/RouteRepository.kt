package com.luis.avaliacao.repositories

import com.luis.avaliacao.domain.Route
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface RouteRepository:JpaRepository<Route,Int>{

    @Query(value = "SELECT * FROM Route,Vehicle,SensorGPS WHERE Route.vehicle_id = Vehicle.id AND SensorGPS.id = :id", nativeQuery = true)
    fun getRouteBySensorGPSIdFromVehicle(@Param("id") id: Int): Optional<Route>
}