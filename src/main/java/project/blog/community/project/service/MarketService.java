package project.blog.community.project.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.blog.community.project.common.Search;
import project.blog.community.project.dto.request.MarketModifyRequestDTO;
import project.blog.community.project.dto.request.MarketRateRequestDTO;
import project.blog.community.project.dto.request.MarketWriteRequestDTO;
import project.blog.community.project.dto.response.*;
import project.blog.community.project.entity.*;
import project.blog.community.project.mapper.MarketMapper;


import java.util.ArrayList;
import java.util.List;

import static project.blog.community.util.LoginUtils.getCurrentLoginMemberAccount;

@Service
@RequiredArgsConstructor
@Slf4j


public class MarketService {
    private final MarketMapper mapper;


    public List<MarketListResponseDTO> getList(HttpServletRequest request, Search page) {
        List<MarketListResponseDTO> dtoList = new ArrayList<>();

        HttpSession session = request.getSession();
        session.getAttribute("login");
        // 세션 유틸리티 메서드로 로그인한 유저 ID 가져오기
        String currentLoginMemberAccount = getCurrentLoginMemberAccount(session);

        // 내가 즐겨찾기한 리스트
        List<Integer> boards = mapper.checkFav(currentLoginMemberAccount);

        List<Market> marketList = mapper.findAll(page);
        for (Market market : marketList){
            MarketListResponseDTO dto = new MarketListResponseDTO(market, boards);
             dtoList.add(dto);
        }
        return dtoList;
    }




    //즐겨찾기만 호출 하는 리스트
    public List<MarketGetAddFavListResponseDTO> getAddFavListService(String currentLoginMemberAccount) {
        List<MarketGetAddFavListResponseDTO> addFavDTOList = new ArrayList<>();

        // 내가 즐겨찾기한 리스트
        List<Integer> boards = mapper.selectByAccountNumber(currentLoginMemberAccount);

        return addFavDTOList;
    }

    // 즐겨찾기 고려 안하고 최신순으로 4개의 중고 거래 게시글 불러오기
    public List<MainMarketResponseDTO> getRecentList() {
        List<Market> fourList = mapper.findFour();
        List<MainMarketResponseDTO> dtoList = new ArrayList<>();

        for (Market market : fourList){
            MainMarketResponseDTO dto = new MainMarketResponseDTO(market);
            dtoList.add(dto);
        }
        return dtoList;
    }







    public void register(MarketWriteRequestDTO dto, String filePath, String currentLoginMemberAccount) {
        Market market = new Market(dto, filePath, currentLoginMemberAccount);//DTO -> Entity
//        market.setTextWriter(MarketUtils.getCurrentLoginMemberAccount(session));
        market.setFile(filePath);
        mapper.save(market);
    }

    public void modify(MarketModifyRequestDTO dto, String filePath) {
        Market marketContent = dto.toEntity();

        marketContent.setFile(filePath);
        mapper.modify(marketContent);
    }


    public MarketDetailResponse getDetail(int boardNo) {
        mapper.updateViewCount(boardNo);
        Market market = mapper.findOne(boardNo);
        return new MarketDetailResponse(market);
    }

    public void delete(int boardNo) {
        mapper.delete(boardNo);
    }

//    public List<Board> addFavList(int boardNo, boolean addFav) {
//        return mapper.addFavList(boardNo, addFav);
//    }

    public void updateFav(int boardNo,
                          HttpSession session,
                          String isAddFav) {

        session.getAttribute("login");
        // 세션 유틸리티 메서드로 로그인한 유저 ID 가져오기
        String currentLoginMemberAccount = getCurrentLoginMemberAccount(session);

        Favorite favorite = Favorite.builder()
                .accountNumber(currentLoginMemberAccount)
                .boardNo(boardNo)
                .build();

        if (isAddFav.equals("true")) {
            mapper.addFav(favorite);
        } else {
            mapper.removeFav(favorite);
        }
    }

    public void addRate(MarketRateRequestDTO dto) {
        String textWriter = dto.getTextWriter(); //재가공

        // Market Rate 테이블(중복 검사를 위한 테이블) row 추가 -> 선택한 1~5를 저장 (insert)
        mapper.addRate(dto);

        // Market Rate 테이블에서 해당 유저의 모든 평점을 평균을 내서 값을 가져오고 (select)
        float rateAvg = mapper.rateAverage(dto.getTextWriter());

        // 평균 낸 값을 유저 테이블에 저장 (update)
        mapper.updateRateBoard(textWriter, rateAvg);

    }

    public int getRate(String textWriter) {
        return mapper.checkRate(textWriter);
    }

    public boolean isDuplication(MarketRateRequestDTO dto) {
        Rate rate = mapper.checkRateRelation(dto);
        if (rate == null) return false; //중복X
        else return true; //중복됨

    }

    public List<Rate> findUserRate(String userAccount) {
        return mapper.checkRateByUser(userAccount);
    }




    public int getCount(Search page, HttpServletRequest request) {

        HttpSession session = request.getSession();
        LoginUserResponseDTO loginDto = (LoginUserResponseDTO) session.getAttribute("login");
        log.info("dto: {}", loginDto);

        String currentLoginMemberAccount = getCurrentLoginMemberAccount(session);
        int count = mapper.getCount(page, currentLoginMemberAccount);
        log.info("count: " + count);
        return count;



    }



    public List<MarketListResponseDTO> getMarketList(Search page, HttpServletRequest request) {


        HttpSession session = request.getSession();
        LoginUserResponseDTO loginDto = (LoginUserResponseDTO) session.getAttribute("login");
        log.info("dto: {}", loginDto);

        String currentLoginMemberAccount = getCurrentLoginMemberAccount(session);

        List<MarketListResponseDTO> myList = new ArrayList<>();
        List<Market> marketList = mapper.findList(page, currentLoginMemberAccount);

        return myList;

    }


    public int getCountAll(Search page) {
        return mapper.getCountAll(page);
    }
}


//페이징 비동기 방식
//    public MarketPageListResponseDTO getList(int boardNo, Page page) {
//        List<MarketDetailResponse> dtoList = new ArrayList<>();
//
//
//        List<Market> marketList = mapper.findAllpage(boardNo,page);
//        for (Market market : marketList) {
//            dtoList.add(new MarketDetailResponse(market));
//        }
//
//        int count = mapper.count(boardNo);
//
//        return MarketPageListResponseDTO.builder()
//                .marketList(dtoList)
//                .count(count)
//                .pageInfo(new PageMaker(page, count))
//                .build();
//    }


//    //페이지 처리
//    @GetMapping("/{boardNo}/page/{pageNo}")
//    public ResponseEntity<?> list(@PathVariable int boardNo, @PathVariable int pageNo) {
//        log.info("api/v1/replies{}: GET!!!", boardNo);
//        log.info("pageNo: {}", pageNo);
//
//        Page page = new Page();
//        page.setPageNo(pageNo);
//        page.setAmount(5);
//
//        ReplyListResponseDTO replies = replyService.getList(boardNo, page);
//
//        return ResponseEntity.ok().body(replies);
//    }