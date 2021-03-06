package se.nackademin.backend2.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.nackademin.backend2.user.security.JWTAuthenticationFilter;
import se.nackademin.backend2.user.security.JWTAuthorizationFilter;
import se.nackademin.backend2.user.security.JWTIssuer;
import se.nackademin.backend2.user.domain.UserRepository;

import static java.lang.String.format;

@Configuration
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JWTIssuer jwtIssuer;

    @Autowired
    public SecurityConfiguration(UserRepository userRepo, PasswordEncoder passwordEncoder, JWTIssuer jwtIssuer) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtIssuer = jwtIssuer;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final JWTAuthenticationFilter filter = new JWTAuthenticationFilter(authenticationManager(), jwtIssuer, new ObjectMapper());
        final JWTAuthorizationFilter jwtAuthorizationFilter = new JWTAuthorizationFilter(authenticationManager(), jwtIssuer);

        http
                .csrf()
                .disable()
                .cors()
                .disable()
                .authorizeRequests()
                /* TODO: uppgift 1:
                    Testa att skapa en anv??ndare genom swagger. Du f??r ett fel. Endpointen kan inte kommas ??t f??r att
                    spring security har l??st ner din app.
                    Vi m??ste ber??tta att vi f??r komma ??t allt under /user utan n??gon s??kerhet

                    Att ??ppna en route kan man g??ra med permitAll()
                    .antMatchers(url-pattern).permitAll()

                    Se till att du kan skapa anv??ndare genom swagger.

                    200 ok! perfekt men jag f??r inget svar tillbaka?
                    Kolla i loggarna, vi f??r ett l??skigt fel som vi ska se till att l??sa i uppgift 2
                 */
                .antMatchers("/swagger-ui/*", "/swagger-ui.html", "/webjars/**", "/v2/**", "/swagger-resources/**").permitAll()
                /*
                TODO: Upppgift 4:
                    Arbeta med roller
                    1. Skapa en anv??ndare i swagger med roll "ADMIN"
                    2. Skapa en anv??nadre i swagger med roll "CUSTOMER"

                    Titta i UserResource l??ngst ned finns det tv?? endpoints
                    - helloAdmin
                    - helloCustomer

                   Bekanta dig med @AuthenticationPrincipal och se hur man g??r f??r att f?? tillg??ng till den
                   inloggade anv??ndaren.

                   Begr??nsa dina endpoints!
                   En admin ska enbart kunna se helloAdmin
                   En customer ska enbart kunna se helloCustomer

                   Att begr??nsa en route till en viss roll g??rs genom
                   .antMatchers(url-pattern).hasRole(role-name)

                   testa att anv??nda swagger
                   1. ropa p?? login och du ska d?? f?? en token tillbaka
                   2. logga in genom att trycka p?? Authorize uppe till h??ger och skriv Bearer din-token s??
                   kommer den att l??ggas till i alla n??stkommande anrop
                   3. testa att ropa p?? helloAdmin.
                   4. Det funkar inte :( men du borde se i loggarna att vi printar ut ditt inloggningsf??rs??k
                   med din token.

                   Tror det ??r dags att leta efter uppgift 5.
                 */
                .anyRequest().authenticated().and()
                .addFilter(filter)
                .addFilter(jwtAuthorizationFilter)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*
            TODO: Uppgift 2
                Det kraschar, tr??kigt. Om vi kollar i stacktracen s?? f??r vi en nullpointer i
                JWTAuthenticationFilter.java p?? rad 38
                l??t oss se vad den g??r

                1. Vi deserialiserar anv??ndaren och det verkar g?? bra (getPrincipal)
                2. Vi f??r s??ker authentisera anv??ndaren med ett UsernamePasswordAuthenticationToken och d??r verkar det
                sm??lla f??r att authmanagern ??r null. L??t oss konfigurera den,

                Vi beh??ver
                1. ber??tta hur vi h??mtar anv??ndare
                2. ber??tta hur vi hanterar l??senord

                auth.userDetailsService(hur-du-l??ser-upp-din anv??ndare))
                    .passwordEncoder(din-password-encoder);

                Vi anv??nder en password encoder f??r att inte spara l??senord i klartext!

                Konfigurera en authManagern enligt exemplet ovan. Testa sen att logga in via swagger

                Du ska f?? "Du ??r inloggad!" som svar.
         */
        auth.userDetailsService((s) -> null);
    }

}