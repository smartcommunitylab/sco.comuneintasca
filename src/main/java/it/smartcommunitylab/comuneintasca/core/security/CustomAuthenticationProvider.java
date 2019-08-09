package it.smartcommunitylab.comuneintasca.core.security;

import it.smartcommunitylab.comuneintasca.core.model.app.AppSettings;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Autowired
	private AppSetup appSetup;
	
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
	}
	
	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		AppSettings app = appSetup.findAppById(username);
		if (app == null) {
			throw new UsernameNotFoundException(username);
		}
		if (!app.getPassword().equals(authentication.getCredentials().toString())) {
			throw new BadCredentialsException("Incorrect password");
		}
		return new AppDetails(app);
	}

	
	public class AppDetails implements UserDetails {
		private static final long serialVersionUID = 1970015369860723085L;

		private AppSettings app;
		
		public AppDetails() {
			super();
		}

		public AppDetails(AppSettings app) {
			super();
			this.app = app;
		}


		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return Collections.singletonList(new SimpleGrantedAuthority(app.getId()));
		}

		@Override
		public String getPassword() {
			return app.getPassword();
		}

		@Override
		public String getUsername() {
			return app.getId();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}

		public AppSettings getApp() {
			return app;
		}

		public void setApp(AppSettings app) {
			this.app = app;
		}
	}

}
