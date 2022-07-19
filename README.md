# restful-web-service
Restful API Web Service 

<SpringBoot에서 Log를 남기는 다양한 방법>

#1. SpringBoot에서 로그를 남기는 사례 소개
[1] Interceptor

[2] Filter

[3] AOP

[4] 각 개발자에게 파라미터 설정 요청

#2. Interceptor vs. Filter vs. AOP
[1] Filter
필터(Filter)는 J2EE 표준 스펙 기능으로 디스패처 서블릿(Dispatcher Servlet)에 요청이 전달되기 전/후에 url 패턴에 맞는 모든 요청에 대해 부가작업을 처리할 수 있는 기능을 제공한다. 
디스패처 서블릿은 스프링의 가장 앞단에 존재하는 프론트 컨트롤러이므로, 필터는 스프링 범위 밖에서 처리가 되는 것이다. 

즉, 스프링 컨테이너가 아닌 톰캣과 같은 웹 컨테이너에 의해 관리가 되는 것이고(스프링 빈으로 등록은 된다), 디스패처 서블릿 밖에서 처리되므로 디스패처 서블릿의 전/후에 처리하는 것이라 볼 수 있다.

필터 메서드의 종류는 세가지이다.

public interface Filter {

    public default void init(FilterConfig filterConfig) throws ServletException {}

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException;

    public default void destroy() {}
}


 (1) init 메소드
 : init 메소드는 필터 객체를 초기화하고 서비스에 추가하기 위한 메소드이다. 웹 컨테이너가 1회 init 메소드를 호출하여 필터 객체를 초기화하면 이후의 요청들은 doFilter를 통해 처리함
 (2) doFilter 메소드
 : doFilter 메소드는 url-pattern에 맞는 모든 HTTP 요청이 디스패처 서블릿으로 전달되기 전에 웹 컨테이너에 의해 실행되는 메소드이다. 
 doFilter의 파라미터로는 FilterChain이 있는데, FilterChain의 doFilter 통해 다음 대상으로 요청을 전달하게 된다. 
 chain.doFilter() 전/후에 우리가 필요한 처리 과정을 넣어줌으로써 원하는 처리를 진행함
 (3) destroy 메소드
 : destroy 메소드는 필터 객체를 서비스에서 제거하고 사용하는 자원을 반환하기 위한 메소드이다. 이는 웹 컨테이너에 의해 1번 호출되며 이후에는 이제 doFilter에 의해 처리되지 않는다.

출처: https://mangkyu.tistory.com/173 [MangKyu's Diary:티스토리]
(사진 참고하면 이해 더 쉬움)


[2] Interceptor
인터셉터(Interceptor)는 J2EE 표준 스펙인 필터(Filter)와 달리 Spring이 제공하는 기술로써, 디스패처 서블릿(Dispatcher Servlet)이 컨트롤러를 호출하기 전과 후에 요청과 응답을 참조하거나 가공할 수 있는 기능을 제공한다. 
즉, 웹 컨테이너에서 동작하는 필터와 달리 인터셉터는 스프링 컨텍스트에서 동작을 하는 것이다.
디스패처 서블릿은 핸들러 매핑을 통해 적절한 컨트롤러를 찾도록 요청하는데, 그 결과로 실행 체인(HandlerExecutionChain)을 돌려준다. 
그래서 이 실행 체인은 1개 이상의 인터셉터가 등록되어 있다면 순차적으로 인터셉터들을 거쳐 컨트롤러가 실행되도록 하고, 인터셉터가 없다면 바로 컨트롤러를 실행한다.
인터셉터는 스프링 컨테이너 내에서 동작하므로 필터를 거쳐 프론트 컨트롤러인 디스패처 서블릿이 요청을 받은 이후에 동작하게 된다. (얘 그림 참고)
(실제로는 Interceptor가 Controller로 요청을 위임하지는 않는다. 
인터셉터를 추가하기 위해서는 org.springframework.web.servlet의 HandlerInterceptor 인터페이스를 구현(implements)해야 하며, 이는 다음의 3가지 메소드를 가지고 있다.

 >> public interface HandlerInterceptor {

    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        
        return true;
    }

    default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        @Nullable ModelAndView modelAndView) throws Exception {
    }

    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
        @Nullable Exception ex) throws Exception {
    }
}


(1) preHandle 메소드
 : preHandle 메소드는 컨트롤러가 호출되기 전에 실행된다. 그렇기 때문에 컨트롤러 이전에 처리해야 하는 전처리 작업이나 요청 정보를 가공하거나 추가하는 경우에 사용할 수 있다.
preHandle의 3번째 파라미터인 handler 파라미터는 핸들러 매핑이 찾아준 컨트롤러 빈에 매핑되는 HandlerMethod라는 새로운 타입의 객체로써, @RequestMapping이 붙은 메소드의 정보를 추상화한 객체이다.
또한 preHandle의 반환 타입은 boolean인데 반환값이 true이면 다음 단계로 진행이 되지만, false라면 작업을 중단하여 이후의 작업(다음 인터셉터 또는 컨트롤러)은 진행되지 않는다.
(2) postHandle 메소드
 : postHandle 메소드는 컨트롤러를 호출된 후에 실행된다. 그렇기 때문에 컨트롤러 이후에 처리해야 하는 후처리 작업이 있을 때 사용할 수 있다. 이 메소드에는 컨트롤러가 반환하는 ModelAndView 타입의 정보가 제공되는데, 최근에는 Json 형태로 데이터를 제공하는 RestAPI 기반의 컨트롤러(@RestController)를 만들면서 자주 사용되지는 않는다.
(3) afterCompletion 메소드
 : afterCompletion 메소드는 이름 그대로 모든 뷰에서 최종 결과를 생성하는 일을 포함해 모든 작업이 완료된 후에 실행된다. 요청 처리 중에 사용한 리소스를 반환할 때 사용하기에 적합하다.
 
 
 ** 여기서 주의할 것!!!
 UserInfoInterceptor.java 를 선언해주고, 해당 인터셉터로 넘겨주는 부분이 SpringWebConfig.java이다.
 
  (기존)
// SpringWebConfig.java 
 @Configuration
public class SpringWebConfig implements WebMvcConfigurer {
 
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInfoInterceptor())
                .addPathPatterns("/**")//해당경로 접근전에 인터셉터가 가로첸다
                .excludePathPatterns("/css/**", "/fonts/**", "/img/**", "/js/**","/scss/**",
                                    "/vendor/**", "/login/**","/new/**"); // 정적메서드, 로그인 제외 
    }
    
}

//UserInfoInterceptor.java
publiv class UserInfoInterceptor extends HandlerInterceptor {

    public preHandle(){}
    // 이 세개중 여기 postHandle 해야함
    // 그 이유는 얘는 컨트롤러 호출 이후 인터셉트해주는데 ModelAndView 값도 반환해주기 때문에 View 즉 화면이 어떤건지도 알 수 있어,
    // 어떤 화면의 어떤 API를 호출하는지 알 수 있음
    public postHandle(){}
    public afterCompletion(){}
    
}
 
 
 registry.addInterceptor() 안에 파라미터로 new UserInfoInterceptor()를 넘겨주는 걸 볼 수 있다.
 그런데 이렇게 인터셉터 객체를 new 선언하여 넣어주면 Interceptor 안에서 의존성 주입 오류가 발생한다.
 Spring Container에서 이 Interceptor를 관리하지 못하는 것이다.
 
 고로 아래와 같이 @AutoWired 의존성을 주입한 interceptor를 선언하여 넣어주도록 한다.

// SpringWebConfig.java
 public class SpringWebConfig implements WebMvcConfigurer {
 
    @Autowired
    private UserInfoInterceptor userInfoInterceptor;
 
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInfoInterceptor)
                .addPathPatterns("/**")//해당경로 접근전에 인터셉터가 가로첸다
                .excludePathPatterns("/css/**", "/fonts/**", "/img/**", "/js/**","/scss/**",
                                    "/vendor/**", "/login/**","/new/**"); // 정적메서드, 로그인 제외
    }
    
}

//UserInfoInterceptor.java
publiv class UserInfoInterceptor extends HandlerInterceptor {

    public preHandle(){}
    // 이 세개중 여기 postHandle 해야함
    // 그 이유는 얘는 컨트롤러 호출 이후 인터셉트해주는데 ModelAndView 값도 반환해주기 때문에 View 즉 화면이 어떤건지도 알 수 있어,
    // 어떤 화면의 어떤 API를 호출하는지 알 수 있음
    public postHandle(){}
    public afterCompletion(){}
    
}


그런데 이렇게 하면 또 문제가 있는데, 그거슨 바로 이렇게되면 logout은 session이 끊겨버리기 때문에 컨트롤러 호출 이후인 postHandle에서 처리해주면
session 값을 못가져옴..
그러므로 애는 컨트롤러 호출 전엔 preHandle 에서 해주도록 함.

//UserInfoInterceptor.java
publiv class UserInfoInterceptor extends HandlerInterceptor {

     // 로그아웃은 preHandle 해야함
    // 그 이유는 얘는 컨트롤러 호출 이전에 인터셉트해주므로 session 이 끊기기 전에 로그를 남길수있음
    public preHandle(){}
    // 이 세개중 여기 postHandle 해야함
    // 그 이유는 얘는 컨트롤러 호출 이후 인터셉트해주는데 ModelAndView 값도 반환해주기 때문에 View 즉 화면이 어떤건지도 알 수 있어,
    // 어떤 화면의 어떤 API를 호출하는지 알 수 있음
    public postHandle(){}
    public afterCompletion(){}
    
}

그 외 두가지 방법이 있다.

첫째, 
인터셉터를 새로 만들어 준다.(ex. LoginOutInfoInterceptor)
그럼 위의 소스는 아래처러 더 깔끔함
 
 public class MvcConfig implements WebMvcConfigurer {
 
    @Autowired
    private UserInfoInterceptor userInfoInterceptor;
    // 추가
    @Autowired
    private LoginOutInfoInterceptor loginoutInfoInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInfoInterceptor)
                .addPathPatterns("/**")//해당경로 접근전에 인터셉터가 가로첸다
                .excludePathPatterns("/css/**", "/fonts/**", "/img/**", "/js/**","/scss/**",
                                    "/vendor/**", "/login/**","/new/**", "/loginAction", "/logout"); 
                
        // 추가
        registry.addInterceptor(loginoutInfoInterceptor)
                .addPathPatterns("/loginAction", "/logout");
        
    }
    
}

publiv class LoginOutInfoInterceptor extends HandlerInterceptor {

    public preHandle(){}
    public postHandle(){}
    public afterCompletion(){}
    
}
publiv class UserInfoInterceptor extends HandlerInterceptor {

    public preHandle(){}
    public postHandle(){}
    public afterCompletion(){}
    
}



둘째,
로그인핸들러 부분에서 파라미터를 받아 로그등록 서비스를 직접 호출한다.

그럼 DB service 호출도 무리없이 가능하다.

#3. AOP


