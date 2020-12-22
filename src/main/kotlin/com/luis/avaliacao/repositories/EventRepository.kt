package com.luis.avaliacao.repositories

import com.luis.avaliacao.domain.Event
import org.springframework.data.jpa.repository.JpaRepository

interface EventRepository: JpaRepository<Event,Int>