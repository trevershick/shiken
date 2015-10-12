package io.shick.shiken.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.annotation.Jsr250Voter;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class CustomPermissionEvaluatorSecurityConfig extends GlobalMethodSecurityConfiguration {

	@Override
    public MethodSecurityExpressionHandler createExpressionHandler() {
		DefaultMethodSecurityExpressionHandler h = (DefaultMethodSecurityExpressionHandler) super.createExpressionHandler();
    	h.setPermissionEvaluator(permissionEvaluator());
    	return h;
    }
	
	@Override
	protected AccessDecisionManager accessDecisionManager() {
		List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<AccessDecisionVoter<? extends Object>>();
		ExpressionBasedPreInvocationAdvice expressionAdvice = new ExpressionBasedPreInvocationAdvice();
		expressionAdvice.setExpressionHandler(getExpressionHandler());
		decisionVoters.add(new PreInvocationAuthorizationAdviceVoter(expressionAdvice));
		RoleVoter roleVoter = new RoleVoter();
		roleVoter.setRolePrefix("");
		decisionVoters.add(roleVoter);
		decisionVoters.add(new AuthenticatedVoter());
		return new AffirmativeBased(decisionVoters);
	}
    
    public PermissionEvaluator permissionEvaluator() {
    	return new PermissionEvaluator() {
			@Override
			public boolean hasPermission(Authentication auth, Serializable id, String targetType, Object permission) {
				System.out.println(auth);
				System.out.println(id);
				System.out.println(targetType);
				System.out.println(permission);
				return true;
			}
			
			@Override
			public boolean hasPermission(Authentication arg0, Object arg1, Object arg2) {
				System.out.println(arg0);
				System.out.println(arg1);
				System.out.println(arg2);
				return true;
			}
		};
    }
}
