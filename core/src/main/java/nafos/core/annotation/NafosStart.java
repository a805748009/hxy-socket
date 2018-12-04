package nafos.core.annotation;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.AliasFor;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.annotation.*;

@SpringBootApplication(scanBasePackages = {"com","nafos"})
@EnableScheduling
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface NafosStart {

    @AliasFor(
            annotation = SpringBootApplication.class,
            attribute = "scanBasePackages"
    )
    String[] basePackages() default {"com","nafos"};
}
