package com.sofka.msa.auth;

import com.sofka.msa.entity.view.CustomerView;
import com.sofka.msa.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <b> Descripcion de la clase, interface o enumeracion. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
@Component
public class InfoAdionalToken implements TokenEnhancer {

	@Autowired
	private ICustomerService userService;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		CustomerView user = userService.findCustomerByUsername(authentication.getName()).orElse(null);
		if (user != null) {
			Map<String, Object> additionalInformation = new HashMap<>();
			additionalInformation.put("status", user.getStatus());
			additionalInformation.put("name", user.getPerson().getName());
			additionalInformation.put("lastname", user.getPerson().getLastname());
			additionalInformation.put("username", user.getUsername());
			additionalInformation.put("identification", user.getPerson().getIdentification());

			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
		}
		return accessToken;
	}
}
