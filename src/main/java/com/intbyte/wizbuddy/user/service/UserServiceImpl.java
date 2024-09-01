package com.intbyte.wizbuddy.user.service;

import com.intbyte.wizbuddy.exception.user.EmailDuplicateException;
import com.intbyte.wizbuddy.mapper.EmployeeMapper;
import com.intbyte.wizbuddy.mapper.EmployerMapper;
import com.intbyte.wizbuddy.mapper.UserAndEmployeeMapper;
import com.intbyte.wizbuddy.mapper.UserAndEmployerMapper;
import com.intbyte.wizbuddy.user.domain.RegisterEmployeeInfo;
import com.intbyte.wizbuddy.user.domain.RegisterEmployerInfo;
import com.intbyte.wizbuddy.user.domain.SignInUserInfo;
import com.intbyte.wizbuddy.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserAndEmployerMapper userAndEmployerMapper;
    private final UserAndEmployeeMapper userAndEmployeeMapper;
    private final EmployerMapper employerMapper;
    private final EmployeeMapper employeeMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public void signInEmployer(SignInUserInfo signInUserInfo, RegisterEmployerInfo registerEmployerInfo) {
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String uuid = UUID.randomUUID().toString();
        String customUserCode = currentDate + uuid.substring(8);  // uuid의 9번째 문자부터 끝까지 사용

        signInUserInfo.setUserCode(customUserCode);

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        if (employerMapper.getEmployerByEmail(registerEmployerInfo.getEmployerEmail()) != null) throw new EmailDuplicateException();

        registerEmployerInfo.setEmployerCode(customUserCode);

        signInUserInfo.setUserPassword(bCryptPasswordEncoder.encode(signInUserInfo.getUserPassword()));
        registerEmployerInfo.setEmployerPassword(bCryptPasswordEncoder.encode(registerEmployerInfo.getEmployerPassword()));

        if(signInUserInfo.getUserType().equals("EMPLOYER")) signInUserInfo.setUserType("Employer");

        userAndEmployerMapper.insertUser(signInUserInfo);
        userAndEmployerMapper.insertEmployer(registerEmployerInfo);
    }

    @Transactional
    @Override
    public void signInEmployee(SignInUserInfo signInUserInfo, RegisterEmployeeInfo registerEmployeeInfo) {
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String uuid = UUID.randomUUID().toString();
        String customUserCode = currentDate + uuid.substring(8);  // uuid의 9번째 문자부터 끝까지 사용

        signInUserInfo.setUserCode(customUserCode);

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        if (employeeMapper.getEmployeeByEmail(registerEmployeeInfo.getEmployeeEmail()) != null) throw new EmailDuplicateException();

        registerEmployeeInfo.setEmployeeCode(signInUserInfo.getUserCode());
        registerEmployeeInfo.setEmployeePassword(bCryptPasswordEncoder.encode(registerEmployeeInfo.getEmployeePassword()));

        if(signInUserInfo.getUserType().equals("EMPLOYEE")) signInUserInfo.setUserType("Employee");

        userAndEmployeeMapper.insertUser(signInUserInfo);
        userAndEmployeeMapper.insertEmployee(registerEmployeeInfo);
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        /* 설명. 넘어온 email이 사용자가 입력한 id로써 eamil로 회원을 조회하는 쿼리 메소드 작성 */
        com.intbyte.wizbuddy.user.domain.entity.User loginUser = userRepository.findByUserEmail(userEmail);

        if (loginUser == null) {
            throw new UsernameNotFoundException(userEmail + " 이메일 아이디의 유저는 존재하지 않습니다.");
        }

        /* 설명. 사용자의 권한들을 가져왔다는 가정 */
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYER"));
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));

        return new User(loginUser.getUserEmail(), loginUser.getUserPassword(),
                true, true, true, true,
                grantedAuthorities);
    }
}
