package com.example.itspower.controller;

import com.example.itspower.filter.JwtToken;
import com.example.itspower.model.resultset.UserDto;
import com.example.itspower.model.usertoken.UserRefreshToken;
import com.example.itspower.model.usertoken.UserResponse;
import com.example.itspower.request.search.UserSearchRequest;
import com.example.itspower.request.userrequest.UserDeleteRequest;
import com.example.itspower.request.userrequest.UserUpdateRequest;
import com.example.itspower.response.SuccessResponse;
import com.example.itspower.response.search.UserAulogin;
import com.example.itspower.response.search.UserRequest;
import com.example.itspower.service.UserService;
import com.example.itspower.service.impl.UserLoginConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.GeneralSecurityException;
import java.util.Optional;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private UserLoginConfig userLoginConfig;

    private final static String BEARER = "Bearer";


    @PostMapping("/api/save")
    @CrossOrigin
    public ResponseEntity<SuccessResponse<Object>> saveData(@Validated @RequestBody UserRequest userRequest) throws GeneralSecurityException {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userRequest));
        } catch (Exception ex) {
            throw new GeneralSecurityException(ex.getMessage());
        }
    }

    @PostMapping("/api/update")
    @CrossOrigin
    public ResponseEntity<Object> update(@Valid @RequestBody UserUpdateRequest userRequest, @RequestParam("userId") int id) {
        return userService.update(userRequest, id);
    }

    @PostMapping("/api/delete")
    @CrossOrigin
    public ResponseEntity<Object> delete(@RequestParam("id")Integer id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(HttpStatus.OK.value(), "delete success", ""));
    }

    @PostMapping("/api/login")
    @CrossOrigin
    public ResponseEntity<Object> login(@Validated @RequestBody UserAulogin userAulogin) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAulogin.getUserLogin(), userAulogin.getPassword()));
            UserDetails userDetails = userLoginConfig.loadUserByUsername(userAulogin.getUserLogin());
            String token = jwtToken.generateToken(userDetails);
            String refreshToken = jwtToken.generateRefreshToken(userDetails);
            UserDto loginInfor = userService.loginInfor(userDetails.getUsername());
            boolean checkReport = userService.isCheckReport(loginInfor.getGroupId());
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(HttpStatus.OK.value(), "login success", new UserResponse(userDetails.getUsername(), loginInfor, token, refreshToken, checkReport)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SuccessResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "login is not success", null));
        }
    }

    @PostMapping("/api/refresh-token")
    @CrossOrigin
    public ResponseEntity<Object> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        try {
            String accessToken = jwtToken.getUserNameFromJWT(refreshToken);
            UserDetails userDetails = userLoginConfig.loadUserByUsername(accessToken);
            String newToken = jwtToken.generateToken(userDetails);
            UserDto loginInfor = userService.loginInfor(userDetails.getUsername());
            boolean checkReport = userService.isCheckReport(loginInfor.getGroupId());
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(HttpStatus.OK.value(), "login success", new UserRefreshToken(BEARER, newToken, loginInfor, userDetails.getUsername(), checkReport)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SuccessResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "login is not success", null));
        }
    }

    @PostMapping("/getAllUser")
    @CrossOrigin
    public Object getAllDemarcation(@RequestBody Optional<UserSearchRequest> request,
                                    @RequestParam(defaultValue = "10") int pageSize,
                                    @RequestParam(defaultValue = "1") int pageNo) {
        try {
            UserSearchRequest searchForm = request.orElse(new UserSearchRequest());
            return ResponseEntity.ok(userService.getAllUser(searchForm, pageSize, pageNo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
