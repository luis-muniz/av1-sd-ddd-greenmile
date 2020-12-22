package com.luis.avaliacao.controllers

import com.luis.avaliacao.domain.Route
import com.luis.avaliacao.repositories.RouteRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/route")
class RouteController (
        private val routeRepository: RouteRepository
        ){

    @PostMapping
    fun addRoute(@RequestBody route: Route) {
        routeRepository.save(route)
    }

    @GetMapping
    fun getRoutes(): ResponseEntity<List<Route>> {
        return ResponseEntity(routeRepository.findAll(), HttpStatus.OK)
    }

}