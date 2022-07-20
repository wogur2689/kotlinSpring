package com.example.board.controller

import com.example.board.dto.BoardDto
import com.example.board.service.BoardService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class BoardController(
    private val boardService: BoardService //서비스 빈 의존성 주입
) {
    //로그 생성
    private val log = LoggerFactory.getLogger(javaClass)

    /* 게시글 리스트 페이지 */
    @GetMapping("/")
    fun list(model: Model, @RequestParam(value="page", defaultValue = "1") pageNum: Int):String {
        val boardList: List<BoardDto> = boardService.getBoardList(pageNum)
        val pageList: Array<Int?> = boardService.getPageList(pageNum)

        model.addAttribute("boardList", boardList)
        model.addAttribute("pageList", pageList)
        return "board/list.html"
    }

    /* 글쓰기 페이지 */
    @GetMapping("/post")
    fun write():String {
        return "board/write.html"
    }

    /* DB에 게시글 등록 */
    @PostMapping("/post")
    fun write(boardDto: BoardDto):String {
        boardService.savePost(boardDto)

        return "redirect:/"
    }

    /* 게시글 상세보기 */
    @GetMapping("/post/{no}")
    fun detail(@PathVariable("no") no:Long, model:Model):String {
        val boardDto:BoardDto = boardService.getPost(no)

        model.addAttribute("boardDto", boardDto)
        return "board/detail.html"
    }

    /* 게시글 수정하기*/
    @GetMapping("/post/edit/{no}")
    fun edit(@PathVariable("no") no: Long, model: Model):String {
        val boardDto:BoardDto = boardService.getPost(no)

        model.addAttribute("boardDto", boardDto)
        return "board/update.html"
    }

    /* DB에 수정한 게시글로 업데이트 */
    @PutMapping("/post/edit/{no}")
    fun update(@PathVariable no: Long, boardDto: BoardDto):String {
        boardService.savePost(boardDto)
        return "redirect:/"
    }

    /* 게시글 삭제 */
    @DeleteMapping("/post/{no}")
    fun delete(@PathVariable("no") no: Long):String {
        boardService.deletePost(no)

        return "redirect:/"
    }

    /* 게시글 검색 */
    @GetMapping("/board/search")
    fun search(@RequestParam(value="keyword") keyword:String, model: Model): String {
        val boardDtoList: List<BoardDto> = boardService.searchPosts(keyword)
        model.addAttribute("boardList", boardDtoList)

        return "board/list.html"
    }

}