package project.blog.community.project.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.blog.community.project.entity.Follow;
import project.blog.community.project.entity.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {

   @Autowired
   UserMapper userMapper;


   @Test
   @DisplayName("회원가입 성공해야 한다.")
   void save() {
      /*
      User user = User.builder()
            .accountNumber("test999")
            .password("test999!")
            .name("테스트9")
            .email("test99@gmail.com")
            .birthday(2000)
            .gender(FEMALE)
            .nickname("테스트")
            .build();

      userMapper.save(user);

       */
   }


   @Test
   @DisplayName("아이디가 test333인 계정을 조회하면 그 회원의 이름은 테스트이어야 한다.")
   void findUser() {
      /*
      String id = "test333";

      User user = userMapper.findUser(id);

      assertEquals(user.getName(), "테스트3");

       */

   }

   @Test
   @DisplayName("이메일이 test122@gmail.com일 경우 중복확인 false여야 한다.")
   void isDuplicate() {
      /*
      String email = "test122@gmail.com";

      boolean emailFlag = userMapper.isDuplicate("email", email);

      assertFalse(emailFlag);

       */
   }

   /*@Test
   @DisplayName("팔로우 추가하기")
   void addFollower() {
      // given
      String myAccount = "tjtkdvl";
      String yourAccount = "jg1234";

      // when
      userMapper.addFollower(myAccount, yourAccount);

      // then
   }*/


/*   @Test
   @DisplayName("팔로우 확인하기")
   void checkFollower() {
       // given
      String myAccount = "tjtkdvl";
       
       // when
      List<String> followers = userMapper.findUserByFollower(myAccount);
      for (String follower : followers) {
         System.out.println("팔로워:" + follower);
      }

      // then
   }*/
}















































