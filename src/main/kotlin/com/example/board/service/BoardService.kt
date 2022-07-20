package com.example.board.service

import com.example.board.domain.entity.BoardEntity
import com.example.board.domain.repository.BoardRepository
import com.example.board.dto.BoardDto
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.ceil


@Service
class BoardService(
    private val boardRepository: BoardRepository
) {
    private val blockPageNumCount : Int = 5 //블럭에 존재하는 페이지 번호 수
    private val pagePostCount : Int = 4 //한 페이지에 존재하는 게시글 수
    //로그 생성
    private val log = LoggerFactory.getLogger(javaClass)

    /* 페이징 및 게시글 가져오기 */
    @Transactional(readOnly = true)
    fun getBoardList(pageNum: Int):List<BoardDto> {
        val page: Page<BoardEntity> = boardRepository.findAll(PageRequest.of(pageNum - 1, pagePostCount, Sort.by(Sort.Direction.ASC, "createdDate")))

        val boardEntities:List<BoardEntity> = page.content
        val boardDtoList = arrayListOf<BoardDto>()

        for(boardEntity in boardEntities) {
            boardDtoList.add(boardEntity.convertEntityToDto())
        }
        return boardDtoList
    }

    //게시글 갯수 세기
    @Transactional(readOnly = true)
    fun getBoardCount():Long {
        return boardRepository.count()
    }

    /* 페이지 반환*/
    fun getPageList(curPageNum : Int) : Array<Int?> {
        //페이지시작번호 저장
        var pageNumber = curPageNum

        val pageList = arrayOfNulls<Int>(blockPageNumCount)
        //총 게시글 갯수
        val postsTotalCount:Double = getBoardCount().toDouble()
        //총 게시글 기준으로 계산한 마지막 페이지 번호 계산 (올림으로 계산)
        val totalLastPageNum:Int = ceil(postsTotalCount/pagePostCount).toInt()
        //현재 페이지를 기준으로 블럭의 마지막 페이지 번호 계산
        val blockLastPageNum:Int = if(totalLastPageNum > curPageNum + blockPageNumCount) curPageNum + blockPageNumCount else totalLastPageNum
        //페이지 시작 번호 조정
        pageNumber = if(curPageNum <= 3) 1 else curPageNum - 2

        var idx:Int = 0
        //페이지 번호 할당
        for(s in pageNumber..blockLastPageNum) {
            pageList[idx] = s
            idx++
        }

        return pageList
    }

    /* 수정할 게시글 가져오기 */
    @Transactional(readOnly = true)
    fun getPost(id: Long): BoardDto {
        val boardEntity: BoardEntity = boardRepository.findById(id).get()

        return boardEntity.convertEntityToDto()
    }

    /* 게시글 저장 */
    @Transactional
    fun savePost(boardDto: BoardDto): Long? {
        return boardRepository.save(boardDto.toEntity()).id
    }

    /* 게시글 삭제 */
    @Transactional
    fun deletePost(id: Long) {
        boardRepository.deleteById(id)
    }

    /* 게시글 검색 */
    @Transactional(readOnly = true)
    fun searchPosts(keyword: String):List<BoardDto> {
        val boardEntities:List<BoardEntity> = boardRepository.findByTitleContaining(keyword)
        val boardDtoList = arrayListOf<BoardDto>()

        if(boardEntities.isEmpty()) return boardDtoList

        for(boardEntity in boardEntities) {
            boardDtoList.add(boardEntity.convertEntityToDto())
        }
        return boardDtoList
    }

    /**
     * Entity -> Dto 변환 Util
     */
    // (Java-style) Function
    /*fun convertEntityToDto(boardEntity: BoardEntity): BoardDto {
       return BoardDto(
           id = boardEntity.id,
           title = boardEntity.title,
           content = boardEntity.content,
           writer = boardEntity.writer,
           createdDate = boardEntity.createdDate
       )
   }*/
    // (kotlin-style) Top-level Function
    fun BoardEntity.convertEntityToDto(): BoardDto = BoardDto(
        id = this.id,
        title = this.title,
        content = this.content,
        writer = this.writer,
        createdDate = this.createdDate
    )

}