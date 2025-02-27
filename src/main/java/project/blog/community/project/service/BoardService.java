package project.blog.community.project.service;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;
import project.blog.community.project.common.Search;
import project.blog.community.project.dto.request.BoardModifyRequestDTO;
import project.blog.community.project.dto.request.LikeRequestDTO;
import project.blog.community.project.dto.response.BoardDetailResponseDTO;
import project.blog.community.project.dto.response.BoardListResponseDTO;
import project.blog.community.project.dto.response.BoardMyListResponseDTO;
import project.blog.community.project.dto.response.LoginUserResponseDTO;
import project.blog.community.project.entity.Board;
import project.blog.community.project.entity.Category;
import project.blog.community.project.entity.User;
import project.blog.community.project.mapper.BoardMapper;
import project.blog.community.project.mapper.LikeMapper;
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
   private final LikeMapper likeMapper;

   // 게시판 목록을 조회 (all page)
   public List<BoardListResponseDTO> getList(Search page) {
      List<BoardListResponseDTO> dtoList = new ArrayList<>();
      List<Board> boardList = boardMapper.findAll("recent", page);

      for (Board board : boardList) {
         String nickname = findNickname(board.getWriter()); // writer(account)를 nickname으로 바꾸기

         int numberOfReply = boardMapper.calcNumberOfReply(board.getBno());

         BoardListResponseDTO dto = new BoardListResponseDTO(board, nickname, numberOfReply);
         dtoList.add(dto);
      }

      return dtoList;

   }

   // 메인화면에서 목록을 조회 (main page)
   public List<BoardListResponseDTO> getHotList(String sort) {
      List<BoardListResponseDTO> dtoList = new ArrayList<>();

      // 전체 게시판과 달리 좋아요 순으로 6개 게시물만 정렬
      int amount = 6;
      List<Board> boardList = boardMapper.findHot(sort, amount);

      for (Board board : boardList) {
         String nickname = findNickname(board.getWriter()); // writer(account)를 nickname으로 바꾸기
         BoardListResponseDTO dto = new BoardListResponseDTO(board, nickname);
         dtoList.add(dto);
      }

      return dtoList;
   }

   // 카테고리에 따라 다른 게시판 목록을 보여주는 메서드
   public List<BoardListResponseDTO> getCategoryList(String category, Search page) {

      List<BoardListResponseDTO> dtoList = new ArrayList<>();
      List<Board> boardList = boardMapper.findCategory(category, page);

      for (Board board : boardList) {
         String nickname = findNickname(board.getWriter()); // writer(account)를 nickname으로 바꾸기
         int numberOfReply = boardMapper.calcNumberOfReply(board.getBno());

         BoardListResponseDTO dto = new BoardListResponseDTO(board, nickname, numberOfReply);
         dtoList.add(dto);
      }

      return dtoList;

   }

   // 게시글 정보 가져오기
   public BoardDetailResponseDTO getDetail(int bno) {
      boardMapper.updateViewCount(bno);

      Board board = boardMapper.findOne(bno);
      String nickname = findNickname(board.getWriter());

      return new BoardDetailResponseDTO(board, nickname);

   }

   // 수정 페이지에서 게시글 정보 가져오기
   public BoardDetailResponseDTO getModifyDetail(int bno) {
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


     //게시물의 좋아요 수 바꾸기
//    public int changeLike(LikeRequestDTO dto, HttpServletRequest request) {
//        int bno = dto.getBno();
//        int number = dto.getNumber();
//
//        // 게시물 테이블의 좋아요 수 업데이트
//        boardMapper.updateLikeCount(bno, number);
//
//        HttpSession session = request.getSession();
//        session.getAttribute("login");
//        // 세션 유틸리티 메서드로 로그인한 유저 ID 가져오기
//
//        // 좋아요를 눌렀다면 Like 테이블에 insert
//        if (number > 0) {
//
//            Cookie cookie = new Cookie("like", Integer.toString(bno));
//            cookie.setMaxAge(60);
//            cookie.setPath("/");
//            response.addCookie(cookie);
//
//            return 1;
//
//        } else { // 좋아요를 안 눌렀다면 해당 게시글에 대한 쿠키 삭제하기
//            Cookie cookie = WebUtils.getCookie(request, "like");
//            cookie.setMaxAge(0);
//            cookie.setPath("/");
//            response.addCookie(cookie);*/
//
//            likeMapper.deleteLike(currentLoginMemberAccount, bno);
//
//            return 0;
//        }
//    }
//
   // 게시물의 좋아요 수 바꾸기
   public int changeLike(LikeRequestDTO dto, HttpServletRequest request) {
      int bno = dto.getBno();
      int number = dto.getNumber();

      // 게시물 테이블의 좋아요 수 업데이트
      boardMapper.updateLikeCount(bno, number);

      HttpSession session = request.getSession();
      session.getAttribute("login");
      // 세션 유틸리티 메서드로 로그인한 유저 ID 가져오기
      String currentLoginMemberAccount = getCurrentLoginMemberAccount(session);

      // 좋아요를 눌렀다면 Like 테이블에 insert
      if (number > 0) {

         // 쿠키에 게시글 번호와 로그인 유저 ID 저장
/*            Cookie cookie = new Cookie("like" + bno, currentLoginMemberAccount); // ex) "like125", "tjtkdvl"
            cookie.setMaxAge(60);
            cookie.setPath("/");
            response.addCookie(cookie); // 클라이언트에 전송*/

         // Like 테이블에 로그인 유저 ID와 좋아요 누른 게시글 번호 저장
         likeMapper.addLike(currentLoginMemberAccount, bno);


         return 1;

      } else { // 좋아요를 안 눌렀다면 Like 테이블로부터 delete

            /*Cookie cookie = WebUtils.getCookie(request, "like" + bno);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);*/

         likeMapper.deleteLike(currentLoginMemberAccount, bno);

         return 0;
      }
   }


   // 좋아요를 이전에 눌렀는지 확인 (from tbl_like)
   public int checkLike(HttpServletRequest request, int bno) {
      HttpSession session = request.getSession();
      session.getAttribute("login");
      // 세션 유틸리티 메서드로 로그인한 유저 ID 가져오기
      String myAccount = getCurrentLoginMemberAccount(session);

      int likeCount = likeMapper.checkLike(myAccount, bno);
      return likeCount;
   }


/*   public List<BoardMyListResponseDTO> getMyList(HttpServletRequest request, Search page) {
      List<BoardMyListResponseDTO> myList = new ArrayList<>();
      List<Board> boardList = boardMapper.findAll(page);

      for (Board board : boardList) {
         BoardMyListResponseDTO dto = new BoardMyListResponseDTO(board);
         myList.add(dto);
      }

      return myList;

   }*/

   // 전체 게시글 수
   public int getCountAll(Search page) {
      return boardMapper.getCountAll(page);
   }

   // 내 게시글 수
   public int getCount(Search page, HttpServletRequest request) {
      HttpSession session = request.getSession();
      LoginUserResponseDTO loginDto = (LoginUserResponseDTO) session.getAttribute("login");
      log.info("dto: {}", loginDto);

      String currentLoginMemberAccount = getCurrentLoginMemberAccount(session);
      int count = boardMapper.getCount(page, currentLoginMemberAccount);
      log.info("count: " + count);
      return count;
   }

   // 카테고리별 게시글 수
   public int getCountCategory(String category, Search page) {
      return boardMapper.getCountCategory(category, page);
   }

   // 나의 게시글 불러오기
   public List<BoardMyListResponseDTO> getMyList(Search page, HttpServletRequest request) {

      HttpSession session = request.getSession();
      LoginUserResponseDTO loginDto = (LoginUserResponseDTO) session.getAttribute("login");
      log.info("dto: {}", loginDto);

      String currentLoginMemberAccount = getCurrentLoginMemberAccount(session);

      List<BoardMyListResponseDTO> myList = new ArrayList<>();
      List<Board> boardList = boardMapper.findMine(page, currentLoginMemberAccount);
      for (Board board : boardList) {
         BoardMyListResponseDTO dto = new BoardMyListResponseDTO(board);

         // 하트 체크 여부 불러오기
         int i = checkLike(request, dto.getBno());
         if (i > 0) {
            dto.setIsHeart(1);
         } else {
            dto.setIsHeart(0);
         }
         myList.add(dto);
      }
      return myList;
   }

   // 특정 인물의 게시글 가져오기
   public List<BoardMyListResponseDTO> getUserList(String account) {

      Search page = new Search();

      List<BoardMyListResponseDTO> hisList = new ArrayList<>();
      List<Board> boardList = boardMapper.findMyHot(page, account);

      for (Board board : boardList) {
         BoardMyListResponseDTO dto = new BoardMyListResponseDTO(board);
         hisList.add(dto);
      }
      return hisList;

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

   //public void saveTodo(String content, )


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


   // 자기소개 삽입 or 수정
   public void modifyMyIntro(String myAccount, String introduction) {
      boardMapper.modifyIntro(myAccount, introduction);
   }

   public void delete(int bno) {
      boardMapper.delete(bno);
   }

   public List<BoardListResponseDTO> getListUserSearch(Search page, String account) {

      List<BoardListResponseDTO> dtoList = new ArrayList<>();
      List<Board> boardList = boardMapper.findSearch(page, account);

      for (Board board : boardList) {
         String nickname = findNickname(board.getWriter()); // writer(account)를 nickname으로 바꾸기
         BoardListResponseDTO dto = new BoardListResponseDTO(board, nickname);
         dtoList.add(dto);
      }

      return dtoList;
   }

   // 검색한 글 개수
   public int getCountSearch(Search page, String account) {
      return boardMapper.findSearchCount(page, account);
   }



   // 게시글 수정하기
   public void modifyBoard(BoardModifyRequestDTO dto, String savePath) {

      Board modifyBoard = Board.builder()
              .bno(dto.getBno())
              .category(stringToCategory(dto.getCategory()))
              .title(dto.getTitle())
              .content(dto.getContent())
              .postImg(savePath)
              .build();

      // 수정할 bno 정보만 필요. writer는 수정할 필요가 없고 bno로 찾을 수 있으므로 필요가 없다.
      boardMapper.updateBoard(modifyBoard);
   }


}
