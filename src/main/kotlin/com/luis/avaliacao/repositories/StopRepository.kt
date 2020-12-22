package com.luis.avaliacao.repositories

import com.luis.avaliacao.domain.Stop
import org.springframework.data.jpa.repository.JpaRepository

interface StopRepository:JpaRepository<Stop,Int>