package cloud2.shopingmall.user.service;


import cloud2.shopingmall.common.exception.PasswordMismatchException;
import cloud2.shopingmall.jwt.CustomUserDetails;
import cloud2.shopingmall.jwt.JWTUtil;
import cloud2.shopingmall.user.dto.CommonDTO;
import cloud2.shopingmall.user.dto.UserDTO;
import cloud2.shopingmall.user.dto.UserProfileDTO;
import cloud2.shopingmall.user.entity.User;
import cloud2.shopingmall.user.entity.UserProfile;
import cloud2.shopingmall.user.mapper.UserMainMapper.UserProfileJoinMapper;
import cloud2.shopingmall.user.mapper.UserMainMapper.UserProfileShowMapper;
import cloud2.shopingmall.user.mapper.UserMainMapper.UserProfileChangeMapper;
import cloud2.shopingmall.user.repository.UserProfileRepository;
import cloud2.shopingmall.user.mapper.UserMainMapper.UserJoinMapper;
import cloud2.shopingmall.user.mapper.UserMainMapper.UserShowMapper;
import cloud2.shopingmall.user.mapper.UserMainMapper.UserShowAllMapper;
import cloud2.shopingmall.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserShowMapper userShowMapper;
    private final UserShowAllMapper userShowAllMapper;
    private final UserProfileShowMapper userProfileShowMapper;
    private final UserJoinMapper userJoinMapper;
    private final UserProfileJoinMapper userProfileJoinMapper;
    private final UserProfileChangeMapper userProfileChangeMapper;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;


    @Autowired
    public UserService(UserJoinMapper userJoinMapper, UserProfileJoinMapper userProfileJoinMapper,
                       UserRepository userRepository, UserProfileRepository userProfileRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder, UserShowMapper userShowMapper,
                       UserProfileShowMapper userProfileShowMapper, UserProfileChangeMapper userProfileChangeMapper,
                       UserShowAllMapper userShowAllMapper,
                       AuthenticationManager authenticationManager, JWTUtil jwtUtil) {

        this.userProfileJoinMapper = userProfileJoinMapper;
        this.userJoinMapper = userJoinMapper;
        this.userProfileShowMapper = userProfileShowMapper;
        this.userProfileChangeMapper = userProfileChangeMapper;
        this.userShowMapper = userShowMapper;
        this.userRepository = userRepository;
        this.userShowAllMapper = userShowAllMapper;
        this.userProfileRepository = userProfileRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

    }

    public void createAdminIfNotExists() {
        if (!userRepository.existsByUsername("admin")) {
            // 운영자 계정 생성 로직
            User user = new User();
            UserProfile userProfile = new UserProfile();

            userProfile.setName("ADMIN");
            user.setUsername("admin");
            user.setUserRole("ROLE_ADMIN");
            user.setPassword(bCryptPasswordEncoder.encode("1234")); // 패스워드 암호화는 필요한 경우에만
            userRepository.save(user);

            userProfile.setUser(user);
            userProfileRepository.save(userProfile);
        }
    }

    // 회원 로그인 기능
    public String login(String username, String password) throws UsernameNotFoundException, PasswordMismatchException {

        // 1. 사용자 DB에서 사용자 조회
        // 주어진 사용자의 이름으로 DB에서 사용자 정보 조회
        User userData = userRepository.findByUsername(username);

        if (userData == null) {
            throw new UsernameNotFoundException("아이디를 찾을 수 없습니다.");
        }

        if (userData.getStatus() == User.Status.DELETED ){
            throw new UsernameNotFoundException("삭제된 계정입니다.");
        }

        // DB에 사용자가 존재해 데이터가 있을 경우 '사용자의 인증 및 권한 정보를 제공하는 역할'을 하는 UserDetails 객체 반환
        CustomUserDetails userDetails = new CustomUserDetails(userData);

        // 입력한 비밀번호가 DB에 저장된 비밀번호와 같은지 확인하기
        if (!bCryptPasswordEncoder.matches(password, userDetails.getPassword())) {
            throw new PasswordMismatchException("입력한 비밀번호가 틀립니다.");
        }

        // Authentication의 구현체, 사용자 이름과 비밀번호를 저장
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // AuthenticationManager를 사용하여 사용자를 인증
        // 주어진 Authentication 객체를 사용하여 사용자를 인증하고, 성공하면 Authentication 객체를 반환, 인증이 실패하면 AuthenticationException이 발생합니다.
        Authentication authentication = authenticationManager.authenticate(authToken);

        // 사용자의 권한 목록을 Collection 형태로 바꾼 후, 권한을 반복자를 통해 가져온다
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();
        String name = userData.getUserProfile().getName();
        String email = userData.getUserProfile().getEmail();

        String token = jwtUtil.createJwt(username, role, name, email, 6000*6000*1L);

        return token;
    }


    // 회원 가입 기능
    @Transactional
    public boolean joinProcess(UserDTO.Join userDTO, UserProfileDTO.Join userProfileDTO) throws PasswordMismatchException {

        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        String secondPassword = userDTO.getSecondPassword();
        String email = userProfileDTO.getEmail();
        String phoneNumber = userProfileDTO.getPhoneNumber();

        // 비밀번호를 제대로 두 번 입력했는지 확인
        if(!password.equals(secondPassword)){
            throw new PasswordMismatchException("입력한 비밀번호가 일치하지 않습니다.");
        }

        // repository에 유저 정보가 존재하는지 체크 존재하는 경우 true 없으면 false
        if (userRepository.existsByUsername(username)) {
            // 현재 존재하는 경우 예외 처리
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // repository에 유저 이메일이 존재하는지 체크 존재하는 경우 true 없으면 false
        if (userProfileRepository.existsByEmail(email)) {
            // 현재 존재하는 경우 예외 처리
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // repository에 유저 핸드폰 번호가 존재하는지 체크 존재하는 경우 true 없으면 false
        if (userProfileRepository.existsByPhoneNumber(phoneNumber)) {
            // 현재 존재하는 경우 예외 처리
            throw new IllegalArgumentException("이미 존재하는 전화번호입니다.");
        }

        // 모두 통과한 경우 다음 로직 실행
        // DTO -> Entity 변환
        User user = userJoinMapper.toEntity(userDTO);
        UserProfile userProfile = userProfileJoinMapper.toEntity(userProfileDTO);

        // User DB에 생성
        user.setStatus(User.Status.ACTIVE); // 처음 가입시 계정상태 활성화 상태
        user.setPassword(bCryptPasswordEncoder.encode(password)); // 비밀번호를 암호화하여 저장
        user.setUserRole("ROLE_USER"); // Role 부여
        user.setLoginType("Basic");
        User savedUser = userRepository.save(user); // DB에 저장

        // UserProfile DB에 생성
        userProfile.setUser(savedUser); // user Entity 맵핑
        userProfile.setGender(userProfileDTO.getGender());
        userProfile.setPoint(100000); // 처음 가입 시 10만 포인트 증정
        userProfileRepository.save(userProfile);

        return true;
    }

    // 아이디 찾기 기능
    @Transactional
    public String findUserId(UserProfileDTO.FindUser findIdUserDTO) {

        UserProfile userProfile = new UserProfile();
        String name = findIdUserDTO.getName();
        String email = findIdUserDTO.getEmail();
        String phoneNumber = findIdUserDTO.getPhoneNumber();
        String source = findIdUserDTO.getSource().getKey();

        if (source.equals("phone")) {
            if (phoneNumber == null) {
                throw new IllegalArgumentException("전화번호를 입력해 주세요.");
            }
            if (!userProfileRepository.existsByPhoneNumber(phoneNumber)) {
                throw new IllegalArgumentException("존재하는 전화번호가 없습니다.");
            }
            userProfile = userProfileRepository.findByPhoneNumber(findIdUserDTO.getPhoneNumber());
        }

        if (source.equals("email")) {
            if (email == null) {
                throw new IllegalArgumentException("이메일을 입력해 주세요.");

            }
            if (!userProfileRepository.existsByEmail(email)) {
                throw new IllegalArgumentException("존재하는 이메일이 없습니다.");
            }
            userProfile = userProfileRepository.findByEmail(findIdUserDTO.getEmail());
        }

        if (!userProfile.getName().equals(name)){
            throw new IllegalArgumentException("이름을 정확히 입력해주세요");
        }


        String findID = userProfile.getUser().getUsername();

        return findID;
    }

    // 비밀번호 찾기 기능
    public Boolean findPasswordFilter(UserProfileDTO.FindPassword findPasswordDTO) {

        String username = findPasswordDTO.getUsername();
        String email = findPasswordDTO.getEmail();
        String phoneNumber = findPasswordDTO.getPhoneNumber();
        String source = findPasswordDTO.getSource().getKey();

        // 아이디가 있는지 확인
        if (!userRepository.existsByUsername(username)) {
            // 존재하지 않을 경우 예외처리
            throw new IllegalArgumentException("존재하는 아이디가 없습니다.");
        }

        User target = userRepository.findByUsername(username);

        // 아이디에 해당하는 이메일을 입력했는지 확인
        if (source.equals("email")) {
            if (email == null) {
                throw new IllegalArgumentException("이메일을 입력해 주세요.");
            }
            if (!userProfileRepository.existsByEmailAndUser(email,target)) {
                throw new IllegalArgumentException("아이디에 해당하는 이메일이 없습니다.");
            }
        }

        // 아이디에 해당하는 전화번호를 입력했는지 확인
        if (source.equals("phone")) {
            if (phoneNumber == null) {
                throw new IllegalArgumentException("전화번호를 입력해 주세요.");
            }
            if (!userProfileRepository.existsByPhoneNumberAndUser(phoneNumber,target)) {
                throw new IllegalArgumentException("아이디에 해당하는 전화번호가 없습니다.");
            }
        }

        // -> 비밀번호 변경 페이지로 이동하기
        return true;
    }

    @Transactional
    // 비밀번호 변경 기능 (비 로그인 상태에서 진행 / 비밀번호 찾기 기능과 관련됨)
    public Boolean changePassword(UserDTO.ChangePassword changePasswordDTO) throws PasswordMismatchException {

        User target = userRepository.findByUsername(changePasswordDTO.getUsername());
        String password = changePasswordDTO.getPassword();
        String secondPassword = changePasswordDTO.getSecondPassword();

        // 비밀번호를 제대로 두 번 입력했는지 확인
        if(!password.equals(secondPassword)){
            throw new PasswordMismatchException("입력한 비밀번호가 일치하지 않습니다.");
        }

        // 입력한 비밀번호가 DB에 저장된 비밀번호와 같은지 확인하기
        if (bCryptPasswordEncoder.matches(password, target.getPassword())) {
            throw new PasswordMismatchException("입력한 비밀번호가 현재 비밀번호와 같습니다");
        }

        target.setPassword(bCryptPasswordEncoder.encode(password)); // 비밀번호를 암호화하여 저장

        userRepository.save(target);

        return true;
    }

    // 로그인한 사용자 정보 조회 기능
    public CommonDTO.ShowResponse showUser(CustomUserDetails userInfo) {

        if(userInfo == null){
            throw new AuthenticationCredentialsNotFoundException("Token is null");
        }


        // 현재 인증된 사용자의 정보 가져오기 (클라이언트쪽에서 JWT 토큰을 넣어줘야 인증이 됨)
        String username = userInfo.getUsername();

        // 현재 인증된 사용자의 정보 담기
        User user = userRepository.findByUsername(username);

        // user Entity -> Dto 변환
        UserDTO.Show userDTO = userShowMapper.toDto(user);
        UserProfileDTO.Show userProfileDTO = userProfileShowMapper.toDto(user.getUserProfile());

        CommonDTO.ShowResponse dto = new CommonDTO.ShowResponse(userDTO, userProfileDTO);

        return dto;
    }

    // 회원정보 변경 기능
    @Transactional
    public Boolean changeInfo(CustomUserDetails userInfo, CommonDTO.ChangeInfoRequest changeInfoRequest) throws PasswordMismatchException {
        if (userInfo == null) {
            throw new AuthenticationCredentialsNotFoundException("Token is null");
        }

        // 현재 인증된 사용자의 정보 가져오기 (클라이언트쪽에서 JWT 토큰을 넣어줘야 인증이 됨)
        String username = userInfo.getUsername();

        // 현재 인증된 사용자의 정보 담기
        User targetUser = userRepository.findByUsername(username);
        UserProfile targetUserProfile = targetUser.getUserProfile();

        // 클라이언트에서 입력한 데이터 담기
        UserProfileDTO.ChangeInfo userProfileDTO = changeInfoRequest.getUserProfileDTO();


        targetUserProfile.setUser(targetUser);
        targetUserProfile.setName(userProfileDTO.getName());
        targetUserProfile.setEmail(userProfileDTO.getEmail());
        targetUserProfile.setPhoneNumber(userProfileDTO.getPhoneNumber());
        targetUserProfile.setGender(userProfileDTO.getGender());
        targetUserProfile.setBirthDate(userProfileDTO.getBirthDate());
        targetUserProfile.setPostNumber(userProfileDTO.getPostNumber());
        targetUserProfile.setAddress(userProfileDTO.getAddress());
        targetUserProfile.setAddressDetail(userProfileDTO.getAddressDetail());


        // 사용자의 정보 변경하기
        userProfileRepository.save(targetUserProfile); // UserProfile 엔티티 먼저 저장

        return true;
    }



    // 적립금 충전 기능
    @Transactional
    public Boolean addPoint(CustomUserDetails userInfo, UserProfileDTO.AddPoint addPointDTO) {
        if(userInfo == null){
            throw new AuthenticationCredentialsNotFoundException("Token is null");
        }

        // 현재 인증된 사용자의 정보 가져오기 (클라이언트쪽에서 JWT 토큰을 넣어줘야 인증이 됨)
        String username = userInfo.getUsername();

        // 현재 인증된 사용자의 정보 담기
        User targetUser = userRepository.findByUsername(username);
        UserProfile targetUserProfile = targetUser.getUserProfile();

        // 포인트 충전
        Integer targetPoint = targetUserProfile.getPoint();
        targetPoint += addPointDTO.getPoint();
        targetUserProfile.setPoint(targetPoint);

        // 포인트 충전 후 저장
        userProfileRepository.save(targetUserProfile);

        return true;
    }

    public Boolean deleteUser(CustomUserDetails userInfo, UserDTO.DeleteUser deleteUserDTO) throws PasswordMismatchException {
        if(userInfo == null){
            throw new AuthenticationCredentialsNotFoundException("Token is null");
        }

        // 현재 인증된 사용자의 정보 가져오기 (클라이언트쪽에서 JWT 토큰을 넣어줘야 인증이 됨)
        String username = userInfo.getUsername();

        // 현재 인증된 사용자의 정보 담기
        User target = userRepository.findByUsername(username);

        String password = deleteUserDTO.getPassword();
        String secondPassword = deleteUserDTO.getSecondPassword();

        // 비밀번호를 제대로 두 번 입력했는지 확인
        if(!password.equals(secondPassword)){
            throw new PasswordMismatchException("입력한 비밀번호가 일치하지 않습니다.");
        }

        // 입력한 비밀번호가 DB에 저장된 비밀번호와 같은지 확인하기
        if (!bCryptPasswordEncoder.matches(password, target.getPassword())) {
            throw new PasswordMismatchException("입력한 비밀번호가 현재 비밀번호와 같지 않습니다");
        }

        target.setStatus(User.Status.DELETED);

        userRepository.save(target);

        return true;
    }

    /////////////////////////////////// 관리자 기능 /////////////////////////////////////
    // 사용자 정보 조회 기능
    public List<UserDTO.ShowAllUser> showUserList() {

        int page = 0;
        int size = 5;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        PageRequest pageRequest = PageRequest.of(page,size,sort);
        Page<User> userPage = userRepository.findAll(pageRequest);

        List<UserDTO.ShowAllUser> dtos = userPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return dtos;
    }

    private UserDTO.ShowAllUser convertToDto(User user) {
        UserDTO.ShowAllUser userDTO = new UserDTO.ShowAllUser();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setStatus(user.getStatus());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setRole(user.getUserRole());
        userDTO.setUserProfileDTO(userProfileShowMapper.toDto(user.getUserProfile()));
        return userDTO;
    }


    public boolean adminChangeStatus(UserDTO.adminChangeStatus dto) {
        // DTO 에서 데이터 담기
        Long id = dto.getId();
        String status = dto.getStatus();

        // Id 로 유저 정보 가져오기
        User user = userRepository.findById(id).orElse(null);

        // 유저 정보가 없으면 예외처리 진행
        if(user == null){
            throw new NoSuchElementException("사용자를 찾을 수 없습니다.");
        }

        // 상태 업데이트
        if(status.equals("ACTIVE")) {
            user.setStatus(User.Status.ACTIVE);
        } else if (status.equals("DEACTIVE")) {
            user.setStatus(User.Status.DEACTIVE);
        } else if (status.equals("DELETED")) {
            user.setStatus(User.Status.DELETED);
        } else {
            throw new IllegalArgumentException("상태 업데이트 에러 발생");
        }

        // DB에 저장
        userRepository.save(user);

        return true;
    }
}
