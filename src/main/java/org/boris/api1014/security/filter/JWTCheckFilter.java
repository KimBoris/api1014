package org.boris.api1014.security.filter;

import com.google.gson.Gson;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.boris.api1014.security.auth.CustomUserPrincipal;
import org.boris.api1014.security.util.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class JWTCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        log.info("shouldNotFilter");
        //호출하는 경로를 알아야 허락할건지 안할건지
        String uri = request.getRequestURI();
//        log.info(request.getRequestURI());
        log.info("======================================");

        if (uri.equals("/api/v1/member/makeToken")) {
            return true;
        }
        if (uri.equals("/api/v1/member/refreshToken")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //
        log.info("doFilterInternal");

        log.info(request.getRequestURI());

        //토큰 헤더값을 읽는다.
        String authHeader = request.getHeader("Authorization");

        //토큰값을 사용해야 하기에 위로뺌
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else {
            //에러를 만들어야 한다.
            //401 = 인증이 안된다.
            makeError(response, Map.of("status", 401, "msg", "No Access Token"));
            return;
        }

        //JWT validate
        try {
            Map<String, Object> claims = jwtUtil.validateToken(token);
            log.info(claims);

            String email = (String) claims.get("email");
            String role = (String) claims.get("role");

            Principal customUserPrincipal = new CustomUserPrincipal(email);

            UsernamePasswordAuthenticationToken authenticationToken
            = new UsernamePasswordAuthenticationToken(customUserPrincipal, null,
            List.of(new SimpleGrantedAuthority("ROLE_"+role)));

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authenticationToken);


            filterChain.doFilter(request, response);
        }

        catch (JwtException e) {
            log.info(e.getClass().getName());
            log.info(e.getMessage());
            log.info("-----------------------------");

            String classFullName = e.getClass().getName();

            String shortClassName = classFullName.substring(classFullName.lastIndexOf(".") + 1);

            makeError(response, Map.of("status", 401, "msg", shortClassName));

            e.printStackTrace();
        }
    }
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        //
//        log.info("doFilterInternal");
//
//            filterChain.doFilter(request, response);
//    }

    private void makeError(HttpServletResponse response, Map<String, Object> map) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);

        //응답코드
        //JSON타입
        response.setContentType("application/json");
        response.setStatus((int) map.get("status"));
        try {
            PrintWriter out = response.getWriter();
            out.println(jsonStr);
            out.close(); //close중요하다.
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

//@Log4j2
//@RequiredArgsConstructor
//public class JWTCheckFilter extends OncePerRequestFilter {
//
//    private final JWTUtil jwtUtil;
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//
//        log.info("shouldNotFilter");
//
//        String uri = request.getRequestURI();
//        log.info("----------------------------------");
//
//        if(uri.equals("/api/v1/member/makeToken")){
//            return true;
//        }
//        return false;
//    }
//
////    @Override
////    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
////
////        log.info("doFilterInternal");
////        filterChain.doFilter(request, response);
////
////    }
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        log.info("doFilterInternal");
//
//        log.info(request.getRequestURI());
//
//        String authHeader = request.getHeader("Authorization");
//
//        String token = null;
//        if(authHeader != null && authHeader.startsWith("Bearer ")){
//            token = authHeader.substring(7);
//        }else {
//            makeError(response, Map.of("status",401, "msg","No Access Token") );
//            return;
//        }
//
//        //JWT validate
//        try{
//
//            Map<String, Object> claims = jwtUtil.validateToken(token);
//            log.info(claims);
//
//            String email = (String) claims.get("email");
//            String role = (String) claims.get("role");
//
//            Principal userPrincipal = new CustomUserPrincipal(email);
//
//            UsernamePasswordAuthenticationToken authenticationToken
//                    = new UsernamePasswordAuthenticationToken(userPrincipal, null,
//                    List.of(new SimpleGrantedAuthority("ROLE_"+role)));
//
//            SecurityContext context = SecurityContextHolder.getContext();
//            context.setAuthentication(authenticationToken);
//
//            filterChain.doFilter(request, response);
//
//        }catch(JwtException e){
//
//            log.info(e.getClass().getName());
//            log.info(e.getMessage());
//            log.info("-----------------------------");
//
//            String classFullName = e.getClass().getName();
//
//            String shortClassName = classFullName.substring(classFullName.lastIndexOf(".") + 1);
//
//            makeError(response, Map.of("status",401, "msg",shortClassName) );
//
//            e.printStackTrace();
//        }
//    }
//
//    private void makeError(HttpServletResponse response, Map<String, Object> map) {
//
//        Gson gson = new Gson();
//        String jsonStr = gson.toJson(map);
//
//        response.setContentType("application/json");
//        response.setStatus((int)map.get("status"));
//        try {
//            PrintWriter out = response.getWriter();
//            out.println(jsonStr);
//            out.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//
//    }
//}