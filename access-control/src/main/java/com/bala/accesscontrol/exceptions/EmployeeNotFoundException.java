package com.bala.accesscontrol.exceptions;
class userNotFoundException extends RuntimeException {

	userNotFoundException(Long id) {
		super("Could not find user " + id);
	}
}