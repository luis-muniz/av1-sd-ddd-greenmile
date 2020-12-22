package com.luis.avaliacao.repositories

import com.luis.avaliacao.domain.Telephone
import org.springframework.data.jpa.repository.JpaRepository

interface TelephoneRepository: JpaRepository<Telephone,Int>