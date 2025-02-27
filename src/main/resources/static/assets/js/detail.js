const $writer = document.getElementById('writer');
const $writerInformation = document.querySelector('.writer-info');
const bno = document.querySelector('.board-info').dataset.bno; // 현재 페이지 글번호
const writer = document.querySelector('.board-info').dataset.writer; // 현재 페이지 작성자

const $likeLabel = document.querySelector('.like .like-label'); // 좋아요 텍스트
const $heartBtn = document.querySelector('.like>i.fa-heart'); // 좋아요 하트 아이콘
const writerAccount = document.querySelector('.board-info').dataset.writeraccount; // 현재 페이지 작성자 계정


// URL 복사하기
const $copyUrl = document.querySelector('h2#copy-url>a');
$copyUrl.onclick = () => {

  console.log('url 복사!');
  var currentPageUrl = window.location.href;
  console.log(currentPageUrl);

  var tempInput = document.createElement("input");
  tempInput.setAttribute("value", currentPageUrl);
  document.body.appendChild(tempInput);

  tempInput.select();

  document.execCommand("copy");

  document.body.removeChild(tempInput);

  alert("페이지 URL이 복사되었습니다.: " + currentPageUrl);

};




// 좋아요 누르면 게시물 좋아요 수 1 증가시키고 취소하면 1 감소시키기
function likeCountUpDown(n) {
  console.log('좋아요 수를 변경합니다.');

  var formData = {
    bno: bno, // 게시글 번호
    number: n // likeCount에 더할 값
  };

  fetch("/home/detail/like", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(formData)
    })
    .then(response => response.json())
    .then(data => {
      console.log("Form submission successful:", data);
    })
    .catch(error => {
      console.error("Error submitting form:", error);
    });

}

let likeFlag;

// 페이지 진입 부터 좋아요 하트 체크여부 부여 (과거 이미 체크했다면 부여)
function alreadyLike() {
  console.log('alreadyLike 실행!!');

  // DB에 이미 저장됨 (이미 클릭) -> likeFlag = 1
  likeFlag = document.getElementById('like-data').dataset.likeornot;
  likeFlag = Number(likeFlag);
  console.log('이미 좋아요를 눌렀나요?', likeFlag);

  if (likeFlag === 1) { // 과거에 체크를 이미 했다면
    $heartBtn.classList.remove('fa-regular');
    $heartBtn.classList.add('fa-solid'); // 체크 부여
  } else { // 체크를 한 적이 없다면
    $heartBtn.classList.remove('fa-solid');
    $heartBtn.classList.add('fa-regular'); // 체크 해제
  }

}



// 좋아요 누르면 하트 색깔 바뀌기
function clickHeartBtn() {
  console.log(likeFlag);

  if (likeFlag === 0) {
    console.log('하트버튼 클릭!');
    if ($heartBtn.classList.contains('fa-solid')) { // 체크가 된 상태 라면
      likeCountUpDown(-1);
      $heartBtn.classList.remove('fa-solid');
      $heartBtn.classList.add('fa-regular'); // 체크 해제
      $likeLabel.textContent = '좋아요 ' + $likeLabel.dataset.likeCount;

    } else { // 체크가 안 된 상태라면
      likeCountUpDown(1);
      $heartBtn.classList.remove('fa-regular');
      $heartBtn.classList.add('fa-solid'); // 체크 부여

      // 클릭할 때마다 likeCount 실시간 반영
      const realTimeLike = Number($likeLabel.dataset.likeCount) + 1
      $likeLabel.textContent = '좋아요 ' + realTimeLike;
    }

  }

  if (likeFlag === 1) {

    if ($heartBtn.classList.contains('fa-solid')) { // 체크가 된 상태 라면
      likeCountUpDown(-1);
      $heartBtn.classList.remove('fa-solid');
      $heartBtn.classList.add('fa-regular'); // 체크 해제
      const realTimeLike = Number($likeLabel.dataset.likeCount) - 1
      $likeLabel.textContent = '좋아요 ' + realTimeLike

    } else { // 체크가 안 된 상태라면
      likeCountUpDown(1);
      $heartBtn.classList.remove('fa-regular');
      $heartBtn.classList.add('fa-solid'); // 체크 부여
      $likeLabel.textContent = '좋아요 ' + $likeLabel.dataset.likeCount;
    }

  }




};


// 클릭 이벤트 발생
document.querySelector('.wrapper').onclick = e => {
  console.log(e.target);

  if (e.target.matches('#writer-a')) { // 작성자를 누르면

    if ($writerInformation.style.display === 'none') {
      $writerInformation.style.display = 'block'; // 창이 뜬다
    } else {
      $writerInformation.style.display = 'none'
    }

    return;
  }

  if (e.target.matches('.writer-info a')) { // 선택지를 누르면
    return; // 창을 닫지 않는다.
  }

  if (e.target.matches('.like>i.fa-heart')) { // 좋아요를 누르면
    clickHeartBtn();
    return;
  }

  if (e.target.matches('#delete')) { // 삭제 버튼을 누르면
    clickDeleteButton();
    returnl
  }

  // 그 외의 영역을 누르면
  console.log('창 닫기');
  $writerInformation.style.display = 'none'; // 창이 없어진다

};






// 신고버튼 누르기
const $clameWriter = document.getElementById('report-writer');
$clameWriter.onclick = () => {
  console.log('신고!!');
  document.getElementById('clame-btn').click();
};




// 신고 form 제출
document.getElementById('report-form').addEventListener("submit", function (e) {

  console.log('신고 제출!');
  e.preventDefault(); // form 기능 없애기

  // true or false checkbox 배열
  var interests = [];
  var checkboxes = document.querySelectorAll('input[name="clame-reason"]:checked');
  console.log(checkboxes); // nodelist

  checkboxes.forEach(function (checkbox) { // 배열 고차 함수
    interests.push(checkbox.value); // 체크한 항목만 모아둔 배열 (정제된 형태의 배열)
  });
  console.log(interests);

  var message = document.getElementById("reportText").value;

  // *********************** AJAX : 서버에 유저 신고 요청 보내기 ***********************

  var formData = {
    bno: bno, // 게시글 번호
    writer: writerAccount, // 게시글 작성자 (계정)
    message: message, // text area 기타 사유 메세지
    interests: interests // 체크된 박스만 모아둔 배열
  };

  // Send the form data to the server
  fetch("/home/detail/report", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(formData)
    })
    .then(response => response.text())
    .then(data => {
      console.log('신고 여부 1 or -1 : ', data);
      if (data === '1') {
        alert('정상적으로 신고되었습니다.');
      } else {
        alert('오늘 이미 신고 하셨습니다.')
      }
    })
    .catch(error => {
      console.error("Error submitting form:", error);
    });

 
  document.querySelector('.modal-content .modal-footer .btn-secondary').click();
  document.getElementById("reportText").value = '';
  document.getElementById("advertisement").checked = false;
  document.getElementById("bad").checked = false;
  document.getElementById("dispute").checked = false;
  document.getElementById("plaster").checked = false;



}); // 신고 영역 end




// 팔로잉 누르기
const $addFollowing = document.getElementById('add-following');
$addFollowing.onclick = () => {
  console.log('팔로잉!!');
  const writerAccount = $addFollowing.dataset.writeraccount;
  console.log(writerAccount);

  var formData = {
    writerAccount: writerAccount
  };

  fetch('/api/v1/follow/add', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(formData)
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      // Parse the response body as JSON
      return response.json();
    })
    .then(data => {
      console.log('1인 경우에만 정상 팔로우 처리 완료: ', data);

      if (data === 1) {
        alert('정상적으로 팔로잉 했습니다.');
        location.reload(); // 새로고침
      } else if (data === 2) {
        alert('자기 자신은 팔로잉할 수 없습니다.');
      } else if (data === 3) {
        alert('이미 팔로잉한 회원입니다.');
      }


    });

};

// 수정 버튼 누르기

// 삭제 버튼 누르기
function clickDeleteButton() {
  if (confirm('정말로 삭제하시겠습니까?')) {
    // bno = Number(bno);
    // console.log(typeof bno);

    fetch('/home/remove/' + bno)
    .then(response =>  {
      console.log(response)
      window.location.href = '/home/board/all';
    });

  } else {
    return;
  }
}
  





// 즉시 실행 함수
(() => {
  console.log('즉시 실행함수~');

  alreadyLike();


})();