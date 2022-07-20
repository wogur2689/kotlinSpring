package com.example.board.domain.repository

import com.example.board.domain.entity.BoardEntity
import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository : JpaRepository<BoardEntity, Long> {
    fun findByTitleContaining(keyword:String):List<BoardEntity>
}