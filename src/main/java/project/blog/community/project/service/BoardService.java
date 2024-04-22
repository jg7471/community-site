package project.blog.community.project.service;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;
import project.blog.community.project.dto.request.LikeRequestDTO;
import project.blog.community.project.dto.response.BoardDetailResponseDTO;
import project.blog.community.project.dto.response.BoardListResponseDTO;
import project.blog.community.project.entity.Board;
import project.blog.community.project.entity.Category;
import project.blog.community.project.entity.User;
import project.blog.community.project.mapper.BoardMapper;
import project.blog.community.project.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

import static project.blog.community.util.LoginUtils.getCurrentLoginMemberAccount;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardMapper boardMapper;
    private final UserMapper userMapper;

    // 게시판 목록을 조회
    public List<BoardListResponseDTO> getList() {
        List<BoardListResponseDTO> dtoList = new ArrayList<>();
        List<Board> boardList = boardMapper.findAll("recent",20);

        for (Board board : boardList) {
            String nickname = findNickname(board.getWriter()); // writer(account)를 nickname으로 바꾸기
            BoardListResponseDTO dto = new BoardListResponseDTO(board, nickname);
            dtoList.add(dto);
        }

        return dtoList;

    }

    // 메인화면에서 목록을 조회
    public List<BoardListResponseDTO> getHotList(String option) {
        List<BoardListResponseDTO> dtoList = new ArrayList<>();

        // 전체 게시판과 달리 좋아요 순으로 6개 게시물만 정렬
        String type = option;
        int amount = 6;
        List<Board> boardList = boardMapper.findAll(type, amount);

        for (Board board : boardList) {
            String nickname = findNickname(board.getWriter()); // writer(account)를 nickname으로 바꾸기
            BoardListResponseDTO dto = new BoardListResponseDTO(board, nickname);
            dtoList.add(dto);
        }

        return dtoList;
    }

    // 카테고리에 따라 다른 게시판 목록을 보여주는 메서드
    public List<BoardListResponseDTO> getCategoryList(String category) {
        List<BoardListResponseDTO> dtoList = new ArrayList<>();
        List<Board> boardList = boardMapper.findCategory(category);
        for (Board board : boardList) {
            String nickname = findNickname(board.getWriter()); // writer(account)를 nickname으로 바꾸기
            BoardListResponseDTO dto = new BoardListResponseDTO(board, nickname);
            dtoList.add(dto);
        }

        return dtoList;

    }

    public BoardDetailResponseDTO getDetail(int bno) {
        boardMapper.updateViewCount(bno);

        Board board = boardMapper.findOne(bno);
        String nickname = findNickname(board.getWriter());

        return new BoardDetailResponseDTO(board, nickname);

    }

    // account를 주면 nickname을 반환하는 메서드
    private String findNickname(String account) {

        try {
            User user = userMapper.findUser(account);
            return user.getNickname();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return account;
        }
    }


    // 게시물의 좋아요 수 바꾸기
    public int changeLike(LikeRequestDTO dto, HttpServletRequest request, HttpServletResponse response) {
        int bno = dto.getBno();
        int number = dto.getNumber();
        boardMapper.updateLikeCount(bno, number);

        HttpSession session = request.getSession();
        session.getAttribute("login");
        // 세션 유틸리티 메서드로 로그인한 유저 ID 가져오기
        String currentLoginMemberAccount = getCurrentLoginMemberAccount(session);

        // 좋아요를 눌렀다면 해당 게시글에 대한 쿠키 만들기
        if (number > 0) {
            // 쿠키에 게시글 번호와 로그인 유저 ID 저장

            Cookie cookie = new Cookie("like" + bno, currentLoginMemberAccount); // ex) "like125", "tjtkdvl"
            cookie.setMaxAge(60);
            cookie.setPath("/");
            response.addCookie(cookie); // 클라이언트에 전송

            return 1;

        } else { // 좋아요를 안 눌렀다면 해당 게시글에 대한 쿠키 삭제하기
            Cookie cookie = WebUtils.getCookie(request, "like" + bno);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);

            return 0;
        }
    }

    // 게시글 업로드
    public void saveBoard(String category, String title, String content, String filePath, String writer) {


        Board uploadedBoard = Board.builder()
                .category(stringToCategory(category))
                .title(title)
                .content(content)
                .writer(writer)
                .postImg(filePath)
                .build();


        boardMapper.save(uploadedBoard);
    }

    // 문자열을 Category 타입으로 바꾸는 메서드
    private Category stringToCategory(String str) {
        String upperCategory = str.toUpperCase();
        return Category.valueOf(upperCategory);

    }

    public String stringToCategoryDescription(String str) { // str = "movie"
        String upperCategory = str.toUpperCase(); // upperCategory = "MOVIE"
        Category category = Category.valueOf(upperCategory); // category = Category.MOVIE
        return category.getDescription(); // return value = "영화글"
    }


}
