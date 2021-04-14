package in.softops.springsecurity.security;

import in.softops.springsecurity.security.jwt.AuthEntryPointJwt;
import in.softops.springsecurity.security.jwt.AuthTokenFilter;
import in.softops.springsecurity.security.service.CustomerDetailsServiceImpl;
import in.softops.springsecurity.security.service.EmployeeDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final AuthEntryPointJwt unauthorizedHandler;
	private final CustomerDetailsServiceImpl customerDetailsService;
	private final EmployeeDetailsServiceImpl employeeDetailsService;

	public WebSecurityConfig(AuthEntryPointJwt unauthorizedHandler, CustomerDetailsServiceImpl customerDetailsService, EmployeeDetailsServiceImpl employeeDetailsService) {
		this.unauthorizedHandler = unauthorizedHandler;
		this.customerDetailsService = customerDetailsService;
		this.employeeDetailsService = employeeDetailsService;
	}

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(customerDetailsService).passwordEncoder(passwordEncoder());
		authenticationManagerBuilder.userDetailsService(employeeDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests().antMatchers("/erp-api/auth/**").permitAll()
				.antMatchers("/api/test/**").permitAll()
				.antMatchers("/swagger-ui.html").permitAll()
				.antMatchers("/api/image/**").permitAll()
				.antMatchers("/api/category/list").permitAll()
				.antMatchers("/api/subCategory/list").permitAll()
				.antMatchers("/api/hotDeals/main").permitAll()
				.antMatchers("/api/product/**").permitAll()
				.antMatchers("/api/cart/list").permitAll()
				.antMatchers("/api/coupon/applyCoupon/**").permitAll()
				.antMatchers("/api/order/create").permitAll()
				.antMatchers("/api/customer/create").permitAll()
				.antMatchers("/api/productAttribute/list").permitAll()
				.antMatchers("/api/customer/refresh").permitAll()
				.antMatchers("/api/customer/getInfoByToken").permitAll()
				.antMatchers("/api/currency/activeList").permitAll()
				.antMatchers("/api/order/getOrderInfo/**").permitAll()
				.antMatchers("/api/payment/generateHash").permitAll()
				.antMatchers("/api/order/postOrderProcess").permitAll()
				.antMatchers("/api/siteSettings/main").permitAll()
				.antMatchers("/api/shippingCharge/countryList").permitAll()
				.antMatchers("/api/shippingCharge/**").permitAll()
				.antMatchers("/api/order/getOrdersByCustomer/**").permitAll()
				.antMatchers("/api/auth/resetPassword").permitAll()
				.anyRequest().authenticated();

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// Allow swagger to be accessed without authentication
		web.ignoring().antMatchers("/v2/api-docs")
				.antMatchers("/swagger-resources/**")
				.antMatchers("/swagger-ui.html")
				.antMatchers("/configuration/**")
				.antMatchers("/webjars/**")
				.antMatchers("/public");
	}
}
