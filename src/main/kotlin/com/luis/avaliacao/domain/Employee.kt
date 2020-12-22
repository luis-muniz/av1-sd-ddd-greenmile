package com.luis.avaliacao.domain

import javax.persistence.*

@Entity
data class Employee(
        @Id
        val id: Int,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "telephone_id", referencedColumnName = "id")
        val telephone: Telephone,

        val name: String,
        val cpf: String,
) {
}