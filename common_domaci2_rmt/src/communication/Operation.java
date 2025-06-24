package communication;

import java.io.Serializable;

public enum Operation implements Serializable {
	REGISTER,	// data -> User
	LOGIN,		// data -> User
	GET_PUT,	// data -> User id 
	UPDATE_PUT,	// // data -> Putovanje
	GET_TRANS,	// data -> String
	GET_ZEMLJA,// data -> String
	UPDATE_PUTOVANJE // data > Putovanje
}