package org.anonymous.member.test.annotations;

import org.anonymous.member.contants.Authority;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockSecurityContextFactory.class)
public @interface MockMember {
    long seq() default 1L;
    String email() default "user2@test.org";
    String name() default "사용자1";
    Authority[] authority() default { Authority.USER };
}
