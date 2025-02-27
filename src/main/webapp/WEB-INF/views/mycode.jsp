<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>
  <%@ include file="/WEB-INF/views/include/static-head.jsp" %>
  <link rel="stylesheet" href="/assets/css/header.css">
  

  <link rel="stylesheet" href="/assets/css/mainpage.css" >

  <!----><link rel="stylesheet" href="/assets/css/mycode.css">


</head>
<body>

    <%@ include file="/WEB-INF/views/include/header.jsp" %>

  <div class="MygalleryJsp" >
  <div class="mar">
    <div class="background">
    <div class="Rectangle1" >
      <button class="add-photo"><div>글쓰기</div></button>


    <div class="let2">
    <span class="abc" id="past"> <a href="/wel/endMyCode">오름차순</a></span>
      /
       <span class="new" id="recent"><a href="/wel/myCode">내림차순</a></span>
    </div>
<div class="top-section">
        <!-- 검색창 영역 -->
        <div class="search">
            <form action="/wel/myCode" method="get">

                <select class="form-select" name="type" id="search-type">
                    <option value="title">제목</option>
                    <option value="content">내용</option>
                    <option value="tc">제목+내용</option>
                    <option value="programming">언어</option>
                </select>

                <input type="text" class="form-control" name="keyword" value="${s.keyword}">

                <button class="btn btn-primary" type="submit">
                    검색
                </button>

            </form>
        </div>

    </div>

    

    <div class="Rectangle51" ></div>

    <div class="Frame20" >
      <div class="Gallery">My Code</div>
    </div>
    <div class="card-container">
        <c:forEach var="b" items="${gList}">
            <c:if test="${login.accountNumber == b.writer}">
                <div class="card-wrapper">
                    <section class="card" data-bno="${b.codeNo}">
                        <div class="card-title-wrapper ${b.programming}">
                            <h2 class="card-title">${b.shortTitle}</h2>
                            <div class="time-view-wrapper">
                                <div class="time">
                                    <i class="far fa-clock"></i>
                                    ${b.regDate}
                                </div>
                                <div class="view">
                                    <span class="view-count">${b.programming}</span>
                                </div>
                            </div>
                        </div>
                        <div class="card-content">
                            ${b.shortContent}
                        </div>
                    </section>
                    <div class="card-btn-group">
                        <button class="del-btn" data-href="/wel/codedelete">
                            <i class="fas fa-times">&times;</i>
                        </button>
                    </div>
                </div>
            </c:if>
        </c:forEach>
    </div>
 <!-- 게시글 목록 하단 영역 -->

 <div class="bottom-section">

  <!-- 페이지 버튼 영역 -->
  
  <nav aria-label="Page navigation example">
      <ul class="pagination pagination-lg pagination-custom">
          <c:if test="${maker.page.pageNo != 1}">
              <li class="page-item"><a class="page-link"
                                       href="/wel/myCode?pageNo=1&amount=${s.amount}&type=${s.type}&keyword=${s.keyword}">&lt;&lt;</a>
              </li>
          </c:if>

          <c:if test="${maker.prev}">
              <li class="page-item"><a class="page-link"
                                       href="/wel/myCode?pageNo=${maker.begin-1}&amount=${s.amount}&type=${s.type}&keyword=${s.keyword}">prev</a>
              </li>
          </c:if>
         
          <c:forEach var="i" begin="${maker.begin}" end="${maker.end}">
              <li data-page-num="${i}" class="page-item">
                  <a class="page-link"
                     href="/wel/myCode?pageNo=${i}&amount=${s.amount}&type=${s.type}&keyword=${s.keyword}">${i}</a>
              </li>
          </c:forEach>
      
          <c:if test="${maker.next}">
              <li class="page-item"><a class="page-link"
                                       href="/wel/myCode?pageNo=${maker.end+1}&amount=${s.amount}&type=${s.type}&keyword=${s.keyword}">next</a>
              </li>
          </c:if>    
          
          <c:if test="${maker.page.pageNo != maker.finalPage}">
              <li class="page-item"><a class="page-link"
                                       href="/wel/myCode?pageNo=${maker.finalPage}&amount=${s.amount}&type=${s.type}&keyword=${s.keyword}">&gt;&gt;</a>
              </li>
          </c:if>

      </ul>
  </nav>

</div>


  </div>
</div>
 
<div class="modal" id="modal">
    <div class="modal-content">
        <p>정말로 삭제할까요?</p>
        <div class="modal-buttons">
            <button class="confirm" id="confirmDelete"><i class="fas fa-check"></i> 예</button>
            <button class="cancel" id="cancelDelete"><i class="fas fa-times"></i> 아니오</button>
        </div>
    </div>
</div>




</body>

<script>

 
 const $cardContainer = document.querySelector('.card-container');
const $modal = document.getElementById('modal'); 
const $confirmDelete = document.getElementById('confirmDelete'); 
const $cancelDelete = document.getElementById('cancelDelete'); 




$cardContainer.addEventListener('click', e => {
    console.log('이벤트 타겟: ', e.target);
    if (e.target.matches('.card-container')) return;

    
    if (e.target.matches('.card-btn-group *')) {
        console.log('삭제 버튼 클릭됨!');
        $modal.style.display = 'flex'; 


        
        const deleteLocation = e.target.closest('.del-btn').dataset.href;
        const bno = e.target.closest('div.card-btn-group').previousElementSibling.dataset.bno;
        console.log('deleteLocation: ', deleteLocation);
        console.log('bno: ', bno);

        
        $confirmDelete.onclick = e => { 
          
            location.href = deleteLocation + '?bno=' + bno;

           
            $modal.style.display = 'none';
        }

        
        $cancelDelete.onclick = () => {
            $modal.style.display = 'none'; 
        }

    } else { 

       
      
        const bno = e.target.closest('section.card').dataset.bno;
        console.log('bno: ' + bno);

       
        location.href='/wel/codedetail'+ '?bno=' + bno ;
    }

});







    //========== 게시물 목록 스크립트 ============//

      function removeDown(e) {
        if (!e.target.matches('.card-container *')) return;
        const $targetCard = e.target.closest('.card-wrapper');
        $targetCard?.removeAttribute('id', 'card-down');
      }

      function removeHover(e) {
        if (!e.target.matches('.card-container *')) return;
        const $targetCard = e.target.closest('.card');
        $targetCard?.classList.remove('card-hover');

        const $delBtn = e.target.closest('.card-wrapper')?.querySelector('.del-btn');
        if ($delBtn) $delBtn.style.opacity = '1';
      }


      $cardContainer.onmouseover = e => {

        if (!e.target.matches('.card-container *')) return;

        const $targetCard = e.target.closest('.card');
        $targetCard?.classList.add('card-hover');

        const $delBtn = e.target.closest('.card-wrapper')?.querySelector('.del-btn');
        if ($delBtn) $delBtn.style.opacity = '1';
      }

      $cardContainer.onmousedown = e => {

        if (e.target.matches('.card-container .card-btn-group *')) return;

        const $targetCard = e.target.closest('.card-wrapper');
        $targetCard?.setAttribute('id', 'card-down');
      };

      $cardContainer.onmouseup = removeDown;

      $cardContainer.addEventListener('mouseout', removeDown);
      $cardContainer.addEventListener('mouseout', removeHover);

  
      const $addWriteBtn = document.querySelector('.add-photo');
      if ($addWriteBtn) {
        $addWriteBtn.onclick = e => {
        window.location.href = '/wel/write';
      };
      }
      const $post = document.getElementById('post');
      $post.onclick = e=>{
        window.location.href = '/wel/endMyCode';
      }

  // 사용자가 현재 머물고 있는 페이지 버튼에 active 스타일 부여
  function appendPageActive() {

// 현재 서버에서 넘겨준 페이지 번호
const currPage = '${maker.page.pageNo}';

// li 태그들을 전부 확인해서
// 현재 페이지 번호와 일치하는 li를 찾은 후 active 클래스 이름 붙이기
const $ul = document.querySelector('.pagination');
const $liList = [...$ul.children];

$liList.forEach($li => {
    if (currPage === $li.dataset.pageNum) {
        $li.classList.add('active');
    }
});
}
 // 검색조건 셀렉트박스 옵션타입 고정하기
  function fixSearchOption() {
    const $select = document.getElementById('search-type');

    const $options = [...$select.children];

    $options.forEach($opt => {
        if ($opt.value === '${s.type}') {

            $opt.setAttribute('selected', 'selected');
        }
    });

  }
  document.addEventListener('DOMContentLoaded', function () {
        var programmingValue = "${b.programming}";
        if (programmingValue === "HTML") {
            var cardTitles = document.querySelectorAll('.card-title-wrapper');
            cardTitles.forEach(function (cardTitle) {
                cardTitle.classList.add('HTML');
            });
        }
    });



appendPageActive();
  fixSearchOption();





</script>







</body>
</html>