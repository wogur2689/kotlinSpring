package com.example.board.domain.entity

import javax.persistence.*

@Entity
@Table(name = "board")
class BoardEntity(
    id: Long?, writer: String, title:String, content:String
) : TimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = id

    @Column(length = 10, nullable = false)
    var writer: String = writer

    @Column(length = 100, nullable = false)
    var title:String = title

    @Column(columnDefinition = "TEXT", nullable = false)
    var content:String = content
}

