package TeamUp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserAuthenticationEntryPoint userAuthenticationEntryPoint;
	
	@Autowired
	private UserAuthenticationProvider userAuthenticationProvider;
	
	@Bean
	SecurityFilterChain securityFilterChainOwn(HttpSecurity http) throws Exception {
		http
			.exceptionHandling(handling -> handling.authenticationEntryPoint(userAuthenticationEntryPoint))
			.addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
			.csrf(csrf -> csrf.disable())
			.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(requests -> {
				requests.requestMatchers("/login/register", "/login/tryLogin").permitAll();
				requests.anyRequest().authenticated();
			});
		
		return http.build();
	}
}
