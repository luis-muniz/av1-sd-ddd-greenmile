package com.luis.avaliacao.domain
import com.luis.avaliacao.domain.enums.EventType
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Event (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id:Int? = null,
        val eventType: EventType,
        val `when`: Date = Date(),
        ){
}