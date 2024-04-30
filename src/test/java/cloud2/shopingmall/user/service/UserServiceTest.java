//package cloud2.shopingmall.user.service;
//
//import cloud2.shopingmall.common.exception.PasswordMismatchException;
//import cloud2.shopingmall.jwt.CustomUserDetails;
//import cloud2.shopingmall.user.dto.CommonDTO;
//import cloud2.shopingmall.user.dto.UserDTO;
//import cloud2.shopingmall.user.dto.UserProfileDTO;
//
//
//import cloud2.shopingmall.user.entity.User;
//import cloud2.shopingmall.user.entity.UserProfile;
//import cloud2.shopingmall.user.mapper.UserMainMapper.UserProfileJoinMapper;
//import cloud2.shopingmall.user.mapper.UserMainMapper.UserJoinMapper;
//import cloud2.shopingmall.user.mapper.UserMainMapper.UserProfileShowMapper;
//import cloud2.shopingmall.user.mapper.UserMainMapper.UserShowMapper;
//import cloud2.shopingmall.user.repository.UserRepository;
//import cloud2.shopingmall.user.repository.UserProfileRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private UserProfileRepository userProfileRepository;
//    @Mock
//    private UserJoinMapper userJoinMapper;
//    @Mock
//    private UserProfileJoinMapper userProfileJoinMapper;
//    @Mock
//    private UserShowMapper userShowMapper;
//    @Mock
//    private UserProfileShowMapper userProfileShowMapper;
//    @Mock
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @InjectMocks
//    private UserService userService;
//
//    @Test
//    @DisplayName("회원가입 테스트")
//    void testJoinProcess() throws PasswordMismatchException {
//        // 유저 DTO 및 유저 프로필 DTO 생성
//        UserDTO.Join userDTO = new UserDTO.Join("testuser", "Test123!", "Test123!");
//        UserProfileDTO.Join userProfileDTO = new UserProfileDTO.Join("Test Name", "test@example.com", "010-1234-5678",
//                "123 Test St", UserProfileDTO.Gender.MALE, "1990-01-01");
//        // 유저 Entity 및 유저 프로필 Entity 생성
//        User user = new User(1L,"testuser", "Test123!", "Role_ADMIN", User.Status.ACTIVE, null,null);
//        UserProfile userProfile = new UserProfile(1L, "Test Name", "test@example.com", "010-1234-5678",11325,"test address", "123 Test St","Male","1990-01-01",100000,null);
//
//        // userMapper.toEntity() 메서드가 호출될 때 적절한 User 객체 반환하도록 설정
//        when(userJoinMapper.toEntity(userDTO)).thenReturn(user);
//        // userProfileMapper.toEntity() 메서드가 호출될 때 적절한 UserProfile 객체 반환하도록 설정
//        when(userProfileJoinMapper.toEntity(userProfileDTO)).thenReturn(userProfile);
//
//        // Repository 메서드 호출 시 반환 객체 설정
//        when(userRepository.save(user)).thenReturn(user);
//        when(userProfileRepository.save(userProfile)).thenReturn(userProfile);
//
//        // 테스트 대상 메서드 호출
//        boolean result = userService.joinProcess(userDTO, userProfileDTO);
//
//        // 결과 검증
//        assertTrue(result);
//
//        // 레파지토리에 저장된 값과 user 객체가 같은지 확인
//        assertEquals(user, userRepository.save(user));
//        assertEquals(userProfile, userProfileRepository.save(userProfile));
//    }
//
//    @Test
//    @DisplayName("아이디 찾기 테스트")
//    void findUserId() {
//        // 유저 프로필 DTO 생성
//        UserProfileDTO.FindUser userProfileDTO = new UserProfileDTO.FindUser("010-1234-5678", "test@exampl.com", UserProfileDTO.FindUser.Source.PHONE);
//        // 유저 Entity 생성
//        User user = new User(1L,"testuser", "Test123!", "Role_ADMIN", User.Status.ACTIVE, null,null);
//        UserProfile userProfile = new UserProfile(1L, "Test Name", "test@example.com", "010-1234-5678",11325,"test address", "123 Test St","Male","1990-01-01",100000,null);
//
//        // Repository 메서드 실행 시 적절한 객체 반환 *현재는 핸드폰으로 찾기를 사용하여 이멜로 찾기 기능은 주석 처리
//        when(userProfileRepository.existsByPhoneNumber(any(String.class))).thenReturn(true);
//        when(userProfileRepository.findByPhoneNumber(any(String.class))).thenReturn(userProfile);
////        when(userProfileRepository.findByEmail(any(String.class))).thenReturn(userProfile);
////        when(userProfileRepository.existsByEmail(any(String.class))).thenReturn(true);
//
//        // 테스트 대상 메서드 호출
//        String result = userService.findUserId(userProfileDTO);
//
//        // 테스트 결과와 비교하기
//        assertEquals(user.getUsername(),result);
//    }
//
//    @Test
//    @DisplayName("비밀번호 찾기 정보 입력 테스트")
//    void findPasswordFilter() {
//        // 유저 Profile DTO 생성
//        UserProfileDTO.FindPassword userProfileDTO = new UserProfileDTO.FindPassword("testuser", "010-1234-5678", "test@example.com", UserProfileDTO.Source.PHONE);
//
//        // 유저 Entity 및 유저 프로필 Entity 생성
//        User user = new User(1L,"testuser", "Test123!", "Role_ADMIN", User.Status.ACTIVE, null,null);
//        UserProfile userProfile = new UserProfile(1L, "Test Name", "test@example.com", "010-1234-5678",11325,"test address", "123 Test St","Male","1990-01-01",100000,null);
//
//        // Repository 메서드 호출 시 반환 객체 설정
//        when(userRepository.existsByUsername(any(String.class))).thenReturn(true);
//        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
//        when(userProfileRepository.existsByPhoneNumberAndUser(any(String.class), any(User.class))).thenReturn(true);
//
//        // 테스트 대상 메서드 호출
//        Boolean result = userService.findPasswordFilter(userProfileDTO);
//
//        // 결과 검증
//        assertTrue(result);
//
//
//    }
//
//    @Test
//    @DisplayName("비밀번호 변경 테스트")
//    void changePassword() throws PasswordMismatchException {
//        // 유저 DTO 생성 및 초기화
//        UserDTO.ChangePassword userDTO = new UserDTO.ChangePassword("test name", "1234test!", "1234test!");
//        User user = new User(1L,"testuser", "Test123!", "Role_ADMIN", User.Status.ACTIVE, null,null);
//
//        // Repository 메서드 호출 시 반환 객체 설정
//        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
//
//        // 테스트 대상 메서드 호출
//        Boolean result = userService.changePassword(userDTO);
//
//        // 결과 검증
//        assertTrue(result);
//    }
//
//    @Test
//    @DisplayName("회원정보 조회 테스트")
//    void showUser() {
//        // 유저 Entity 및 유저 프로필 Entity 생성
//        User user = new User(1L,"testuser", "Test123!", "Role_ADMIN", User.Status.ACTIVE, null,null);
//        UserProfile userProfile = new UserProfile(1L, "Test Name", "test@example.com", "010-1234-5678",11325,"test address", "123 Test St","Male","1990-01-01",100000,null);
//
//        UserDTO.Show userDTO = new UserDTO.Show(user.getUsername(), user.getPassword());
//        UserProfileDTO.Show userProfileDTO = new UserProfileDTO.Show(userProfile.getName(), userProfile.getEmail(), userProfile.getPhoneNumber(), userProfile.getAddress(), userProfile.getGender(), userProfile.getBirthDate());
//
//        // userRepository가 findByUsername 메소드를 호출할 때 가짜 사용자를 반환하도록 설정
//        when(userRepository.findByUsername("testuser")).thenReturn(user);
//
//
//        // userShowMapper가 toDto 메소드를 호출할 때 가짜 DTO를 반환하도록 설정
//        when(userShowMapper.toDto(user)).thenReturn(userDTO);
//        when(userProfileShowMapper.toDto(user.getUserProfile())).thenReturn(userProfileDTO);
//
//        // 가짜 인증 객체 생성
//        Authentication authentication = mock(Authentication.class);
//        CustomUserDetails customUserDetails = new CustomUserDetails(user);
//        when(authentication.getPrincipal()).thenReturn(customUserDetails);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // 테스트 대상 메소드 호출
//        CommonDTO.ShowResponse response = userService.showUser(customUserDetails);
//
//        // 결과 검증
//        assertNotNull(response);
//    }
//}
