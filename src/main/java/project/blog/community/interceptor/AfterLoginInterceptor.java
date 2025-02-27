package project.blog.community.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import project.blog.community.util.LoginUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static project.blog.community.util.LoginUtils.*;

@Configuration
@RequiredArgsConstructor
public class AfterLoginInterceptor implements HandlerInterceptor {

   @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

      HttpSession session = request.getSession();

      if (isLogin(session)) {

         response.setContentType("text/html; charset=UTF-8");
         PrintWriter w = response.getWriter();
         String htmlCode = "<!DOCTYPE html>\n" +
               "<html lang=\"ko\">\n" +
               "<head>\n" +
               "  <meta charset=\"UTF-8\">\n" +
               "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
               "  <title>Document</title>\n" +
               "</head>\n" +
               "<body>\n" +
               "\n" +
               "  <script>\n" +
               "    alert('이미 로그인 한 회원입니다.');\n" +
               "    location.href='/';\n" +
               "  </script>\n" +
               "  \n" +
               "</body>\n" +
               "</html>";
         w.write(htmlCode);
         w.flush();
         return false;

      }

      return true;

   }

}












































