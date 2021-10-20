package com.gunyoung.info.error.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gunyoung.info.error.ErrorController;
import com.gunyoung.info.error.ErrorMsg;
import com.gunyoung.info.error.code.ContentErrorCode;
import com.gunyoung.info.error.code.PersonErrorCode;
import com.gunyoung.info.error.code.PrivacyPolicyErrorCode;
import com.gunyoung.info.error.exceptions.access.NotMyResourceException;
import com.gunyoung.info.error.exceptions.duplication.PersonDuplicateException;
import com.gunyoung.info.error.exceptions.exceed.ContentNumLimitExceedException;
import com.gunyoung.info.error.exceptions.nonexist.ContentNotFoundedException;
import com.gunyoung.info.error.exceptions.nonexist.PersonNotFoundedException;
import com.gunyoung.info.error.exceptions.nonexist.PrivacyPolicyNotFoundedException;

/**
 * {@link ErrorController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ErrorController only <br>
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class ErrorControllerUnitTest {
	
	@InjectMocks
	private ErrorController errorController;
	
	/*
	 * ErrorMsg personNotFounded(PersonNotFoundedException e)
	 */
	
	@Test
	void personNotFoundedTest() {
		//Given
		String errorMsg = "person not founded";
		PersonNotFoundedException exception = new PersonNotFoundedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.personNotFounded(exception);
		
		//Then
		assertEquals(PersonErrorCode.PERSON_NOT_FOUNDED_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * ErrorMsg contentNotFounded(PersonNotFoundedException e)
	 */
	
	@Test
	void contentNotFoundedTest() {
		//Given
		String errorMsg = "content not founded";
		ContentNotFoundedException exception = new ContentNotFoundedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.contentNotFounded(exception);
		
		//Then
		assertEquals(ContentErrorCode.CONTENT_NOT_FOUNDED_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * ErrorMsg privacyPolicyNotFounded(PrivacyPolicyNotFoundedException e)
	 */
	
	@Test
	void privacyPolicyNotFoundedTest() {
		//Given
		String errorMsg = "privacyPolicy not founded";
		PrivacyPolicyNotFoundedException exception = new PrivacyPolicyNotFoundedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.privacyPolicyNotFounded(exception);
		
		//Then
		assertEquals(PrivacyPolicyErrorCode.PRIVACY_POLICY_VERSION_IS_NOT_VALID_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * ErrorMsg personDuplicated(PersonDuplicateException e)
	 */
	
	@Test
	void personDuplicatedTest() {
		//Given
		String errorMsg = "person duplication founded";
		PersonDuplicateException exception = new PersonDuplicateException(errorMsg);
		
		//When
		ErrorMsg result = errorController.personDuplicated(exception);
		
		//Then
		assertEquals(PersonErrorCode.PERSON_DUPLICATION_FOUNDED_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * ErrorMsg contentNumLimitExceeded(ContentNumLimitExceedException e)
	 */
	
	@Test
	void contentNumLimitExceededTest() {
		//Given
		String errorMsg = "content number limited!";
		ContentNumLimitExceedException exception = new ContentNumLimitExceedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.contentNumLimitExceeded(exception);
		
		//Then
		assertEquals(ContentErrorCode.CONTENT_NUM_LIMIT_EXCEEDED_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * ErrorMsg notMyResource(NotMyResourceException e)
	 */
	
	@Test
	void notMyResourceTest() {
		//Given
		String errorMsg = "not my resource";
		NotMyResourceException exception = new NotMyResourceException(errorMsg);
		
		//When
		ErrorMsg result = errorController.notMyResource(exception);
		
		//Then
		assertEquals(PersonErrorCode.RESOURCE_IS_NOT_MINE_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
}