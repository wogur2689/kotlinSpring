package com.example.board.dto

import com.example.board.domain.entity.BoardEntity
import java.time.LocalDateTime

data class BoardDto(
    var id: Long?,
    var writer: String,
    var title: String,
    var content: String,
    var createdDate: LocalDateTime?,
    var modifiedDate: LocalDateTime? = LocalDateTime.now()
) {
    //Entity 변환
    fun toEntity() : BoardEntity {
        return BoardEntity(
            id = id,
            writer = writer,
            title = title,
            content = content
        )
    }
}


