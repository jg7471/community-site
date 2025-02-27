package project.blog.community.project.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.blog.community.project.dto.request.LoginRequestDTO;
import project.blog.community.project.dto.request.SignUpRequestDto;
import project.blog.community.project.dto.request.UserRequestDTO;
import project.blog.community.project.entity.User;
import project.blog.community.project.mapper.UserMapper;
import project.blog.community.project.service.LoginResult;
import project.blog.community.project.service.UserService;
import project.blog.community.util.LoginUtils;
import project.blog.community.util.MailSenderService;
import project.blog.community.util.upload.FileUtils;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

   private final UserService userService;
   private final MailSenderService mailSenderService;
   private final UserMapper userMapper;

   @GetMapping("/sign-up")
   public void signUp() {
      log.info("/users/sign-up: GET!!");
   }

   // 아이디, 이메일 중복확인 메서드
   @GetMapping("/check/{type}/{keyword}")
   @ResponseBody
   public ResponseEntity<?> check(@PathVariable String type,
                                  @PathVariable String keyword) {
      log.info("/users/check: async GET!");
      log.info("type = {}", type);
      log.info("keyword = {}", keyword);

      boolean flag = userService.checkDuplicateValue(type, keyword);

      return ResponseEntity.ok().body(flag);
   }

   @PostMapping("/sign-up")
   public String signUp(SignUpRequestDto dto) {
      log.info("/users/sign-up: POST!");
      log.info("dto = {}", dto);

      // 사진 업로드, 나중에 수정해야함
      String rootPath = null;

      String savePath = null;
//      String savePath = FileUtils.uploadFile(dto.getProfilePicture(), rootPath);

      // 일반 방식 (사이트) 회원가입
      dto.setLoginMethod(User.LoginMethod.COMMON);

      userService.join(dto, savePath);
      return "redirect:/home/main";

   }

   // 로그인 페이지
   @GetMapping("/sign-in")
   public void signIn() {
   }

   //로그인 검증 요청
   @PostMapping("/sign-in")
   public String signIn(LoginRequestDTO dto,
                        RedirectAttributes ra,
                        HttpServletResponse response,
                        HttpServletRequest request) {
      log.info("/users/sign-in: POST!!");
      log.info("dto = {}", dto);


      LoginResult result = userService.authenticate(dto, request.getSession(), response);
      log.info("result = {}", result);

      ra.addFlashAttribute("result", result);

      if (result == LoginResult.SUCCESS) {
         // makeLoginCookie(dto, response);
         userService.maintainLoginState(request.getSession(), dto.getAccountNumber());
         return "redirect:/home/main";
      }

      return "redirect:/users/sign-in";

   }

   private void makeLoginCookie(LoginRequestDTO dto, HttpServletResponse response) {
      //쿠키 로그인 기록을 저장
      Cookie cookie = new Cookie("login", dto.getAccountNumber());

      cookie.setMaxAge(86400);
      cookie.setPath("/");

      // 쿠키가 완성 됐다면 응답
      response.addCookie(cookie);
   }

   // 로그아웃 요청 처리
   @GetMapping("/sign-out")
   public String signOut(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
      log.info("/users/sign-out: GET!!");

      if (LoginUtils.isAutoLogin(request)) {
         userService.autoLoginClear(request, response);
      }

      // 세션에서 로그인 정보 기록 삭제
      session.removeAttribute("login");

      // 세션 전체 무효화 (초기화)
      session.invalidate();

      return "redirect:/home/main";

   }

   // 이메일 인증
   @PostMapping("/email")
   @ResponseBody
   public ResponseEntity<String> mailCheck(@RequestBody String email) {
      log.info("이메일 인증 요청 들어옴: {}", email);
      try {
         String authNum = mailSenderService.joinEmail(email);
         return ResponseEntity.ok().body(authNum);
      } catch (Exception e) {
         e.printStackTrace();
         return ResponseEntity.internalServerError().body("이메일 전송 과정에서 에러 발생!");
      }
   }

   // 회원정보수정창 페이지 요청
   @GetMapping("/info")
   public void userInfoForm () {}

   @PostMapping("/info")
   public String userInfo (UserRequestDTO dto, HttpSession session) {
      log.info("modify 요청 들어옴 : {}", dto.toString());

      String account = LoginUtils.getCurrentLoginMemberAccount(session);

      if (userService.checkDuplicateValue("account", account)) {
         userService.modifyInfo(dto);
      }

      return "redirect:/home/main";
   }

   @GetMapping("/delete")
   public String delete (HttpSession session) {
      log.info("delete요청 들어옴!");

      String account = LoginUtils.getCurrentLoginMemberAccount(session);

      log.info("account: {}", account);

      if (userService.checkDuplicateValue("account", account)) {
         userService.deleteUser(account);
      }

      return "redirect:/users/sign-out";

   }







}












































