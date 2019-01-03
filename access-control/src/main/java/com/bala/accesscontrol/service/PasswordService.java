package com.bala.accesscontrol.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.bala.accesscontrol.model.User;

@Service
public interface PasswordService extends BaseService<User> {
/*
  PasswordGetResponseBody getDefaultPassword(HttpServletRequest httpServletRequest);

  PasswordPostResponseBody verifyPassword(PasswordPostRequestBody passwordPostRequestBody,
      HttpServletRequest httpServletRequest);
*/
}
