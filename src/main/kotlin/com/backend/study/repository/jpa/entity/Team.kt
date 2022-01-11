package com.backend.study.repository.jpa.entity

import javax.persistence.*

@Entity
@Table(name = "team")
data class Team(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    var name: String,

    @OneToMany(mappedBy = "team")
    var members: List<Member> = ArrayList()


) {
    override fun toString(): String {
        return "Team(id:$id, name:$name)"
    }
}
