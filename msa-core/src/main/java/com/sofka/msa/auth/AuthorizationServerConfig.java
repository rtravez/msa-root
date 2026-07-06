package com.sofka.msa.auth;

import com.sofka.msa.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

/**
 * <b> Descripcion de la clase, interface o enumeracion.</b>
 *
 * @author renetravez
 * @version $1.0$
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;

	@Autowired
	private InfoAdionalToken infoAdionalToken;

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient(Constants.USERNAME_CLIENT).secret(Constants.PASSWORD_CLIENT)
				.scopes(Constants.SCOPE_READ, Constants.SCOPE_WRITE).authorizedGrantTypes(Constants.AUTHORIZED_GRANT_TYPES, Constants.REFRESH_TOKEN)
				.accessTokenValiditySeconds(3600).refreshTokenValiditySeconds(3600);

	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdionalToken, accessTokenConverter()));
		endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore()).accessTokenConverter(accessTokenConverter())
				.tokenEnhancer(tokenEnhancerChain);
	}

	/**
	 * <b> Incluir aqui la descripcion del metodo. </b>
	 * <p>
	 * [Author renetravez, 21 sep. 2020]
	 * </p>
	 *
	 * @return TokenStore
	 */

	@Bean
	JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	/**
	 * <b> Metodo que permite almacenar los datos de autentificacion. </b>
	 * <p>
	 * [Author renetravez, 21 sep. 2020]
	 * </p>
	 *
	 * @return AccessTokenConverter
	 */
	@Bean
	JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();

		jwtAccessTokenConverter.setSigningKey(Constants.SIGNING_KEY);
		jwtAccessTokenConverter.setVerifierKey(Constants.VERIFIER_KEY);
		return jwtAccessTokenConverter;
	}

}
