package com.spring.boot.timeoffapp.config;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	/*@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.mvcMatcher("/**")
				.authorizeRequests()
				.mvcMatchers("/**")
				.access("hasAuthority('SCOPE_trust')")
				.and()
				.oauth2ResourceServer()
				.jwt();
		return httpSecurity.build();
	}*/

	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
        .withUser("user").password("{noop}password").roles("USER")
        .and()
        .withUser("manager").password("{noop}password").roles("MANAGER")
		.and()
        .withUser("admin").password("{noop}password").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.cors().and().csrf().disable()
		 .authorizeRequests()
		 .and()
        	.httpBasic()
        	.and()
        	.authorizeRequests()
		    .antMatchers(HttpMethod.GET,"/timeOffs").hasAnyRole("ADMIN", "MANAGER", "USER") 
		    .antMatchers(HttpMethod.POST,"/timeOffs").hasAnyRole("ADMIN")
		    .antMatchers(HttpMethod.GET,"/timeOffs/{id}").hasAnyRole("MANAGER","USER","ADMIN")
		    .antMatchers(HttpMethod.PATCH, "/timeOffs/{id}").hasAnyRole("ADMIN")
		    
		    .antMatchers("/managers/{managerID}/employees/{employeeID}/**").hasAnyRole("MANAGER","USER","ADMIN")
		    .antMatchers("/managers/{managerID}/timeOffRequests/**").hasAnyRole("MANAGER")
		    .antMatchers(HttpMethod.DELETE,"/employees/{employeeID}/timeOffRequests/{timeOffRequestID}").hasAnyRole("USER")
		    .antMatchers(HttpMethod.GET,"/employees/{employeeID}/availableTimes").hasAnyRole("USER")
		    .anyRequest().authenticated()
		    .and().formLogin().disable();
    }
}
