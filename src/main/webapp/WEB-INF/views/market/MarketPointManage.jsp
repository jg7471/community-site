<!DOCTYPE html>
<html lang="ko">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>

  <!-- 폰트 -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Dongle&display=swap" rel="stylesheet">

  <!-- common css -->
  <link rel="stylesheet" href="/assets/css/market.css">

  <!-- 부트스트랩 -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
    integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

  <!-- Awesome -->
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">

  <!-- linear icons -->
  <link rel="stylesheet" href="https://cdn.linearicons.com/free/1.0.0/icon-font.min.css">


  <style>
    /* common css 가져오기 */
    @import url('/assets/css/market.css');

  </style>

</head>

<body>


  <aside>
    <nav class="left-snb">
      <!-- 왼쪽 메뉴 사이드바 -->
      <ul>
        <li id="home"><a href="#">Home</a></li>
        <li id="game"><a href="#">게임</a></li>
        <li id="second-hand"><a href="#">중고 장터</a></li>
        <li id="sports"><a href="#">스포츠 예측</a></li>
        <li id="all"><a href="#">
            전체 게시판
            <ul>
              <li id="game-board"><a href="#">게임 게시판</a></li>
              <li id="movie"><a href="#">영화 게시판</a></li>
              <li id="trip"><a href="#">여행 게시판</a></li>
            </ul>
          </a></li>
      </ul>
    </nav>

    <nav class="right-snb">
      <!-- 포인트 : aside>nav.right-snb>.point -->
      <div class="point">
        <p>10000P</p>
      </div>


      <!-- 날씨 정보 : aside>nav.right-snb>.weather-->
      <div class="weather">
        <h2>weather area</h2>
      </div>



      <!-- 친구 목록 : aside>nav.right-snb>.friends -->
      <div class="friends">

        <div class="friends">
          <h2> 친구 목록 </h2>
          <ul>
            <li>
              <div class="profile-box">
                <img src="#" alt="프로필 사진">
              </div>
              <a class="friend" href="#">asfasgas123</a>
            </li>
            <li>
              <div class="profile-box">
                <img src="#" alt="프로필 사진">
              </div>
              <a class="friend" href="#">fontfont3</a>
            </li>
            <li>
              <div class="profile-box">
                <img src="#" alt="프로필 사진">
              </div>
              <a class="friend" href="#">hello99</a>
            </li>
            <li>
              <div class="profile-box">
                <img src="#" alt="프로필 사진">
              </div>
              <a class="friend" href="#">panda423aa</a>
            </li>
          </ul>
        </div>


        <div id="user-information">
          <p>

            sdfa99asdf 님<span id="x-btn"></span>
          </p>
          <ul>
            <li id="my-page"></li>
            <li id="chatting"></li>
            <li id="ban"></li>
          </ul>
        </div>

        <div id="chatting">

        </div>


      </div>

    </nav>

    <!-- 가운데 Content -->
    <div class="wrapper">
      <div class="content-box">
        <!-- ContentBox 내용 제일 위-->


        <div class="head">
          <!-- Head 내용 -->
          <span>안녕하세요 </span><span>#코리안네이마루</span><span>님</span><span>#나의
            포인트</span><span>#100원</span><span>#포인트</span><span>충전하기</span>

        </div>


        <div class="title">
          <!-- Title 내용 -->
            <div class="titleContent">
              <span>코리안네이마루</span><span>(인증완료)</span><span>님의 즐겨찾기</span>
          </div>
        </div>





        <div class="content">

          <!-- Content 내용 -->
          <div class="contentHead">
            <div class="#content-Write">#글쓰기</div>
            <div class="content-Del">#삭제</div>
            <div class="content-Rev">#수정</div>
            <div class="content-AddFav">#상품 즐겨찾기</div>
            <div class="content-Otherproduct">#판매자의 다른상품</div>
          </div>
          <br>
          <br>


          <section class="pointBox">
            <ul class="pointGroup">
              <li>
                <span class="pointUserName">#코리안네이마루</span><span>님의 포인트</span><br>
                <span class="pointCharge">1.000.000</span> <span>원</span>
                <input type="text">
                <br><button class="sendButton">송금하기</button><button class="refundButton">환불하기</button>
              </li>

            </ul>
          </section>


        </div>






      </div>


    </div>


  </aside>


  <script>
    /* 친구 정보 */
    const $friends = document.querySelector('.friends');
    const $userInformation = document.getElementById('user-information');
    const $xBtn = document.getElementById('x-btn');

    $friends.onclick = e => {
      if (!e.target.matches('.friend')) return;
      e.preventDefault();

      // #user-information 의 p태그가 누른 대상의 닉네임이 되어야 한다.
      $userInformation.style.display = 'block';

    };

    $xBtn.onclick = e => {
      console.log('x버튼 클릭');
      $userInformation.style.display = 'none';
    }
  </script>

</body>

</html>