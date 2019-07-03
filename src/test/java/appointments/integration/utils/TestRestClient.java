package appointments.integration.utils;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Служебный класс для отправки запросов на защищенные endpoints.
 * Позволяет осуществить аутентификацию через форму логина
 * и использовать полученный идентификатор сессии при выполнении запросов на защищенные endpoints.
 *
 * @author yanchenko_evgeniya
 */
public class TestRestClient {

    private TestRestTemplate restTemplate;

    public TestRestClient(TestRestTemplate testRestTemplate) {
        this.restTemplate = testRestTemplate;
    }


    /** Метод для отправки HTTP-запроса с указанными параметрами
     *  Метод создает новый объект HttpEntity, в тело которого добавляет объект из параметров,
     *  в заголовок - идентификатор сессии
     */
    public <T> ResponseEntity<T> exchange(
        String restPath, String jSessionId, HttpMethod method,
        Object requestEntityBody, Class<T> responseType, Object... urlVariables
    ) {

        HttpEntity<?> requestEntity = new HttpEntity<>(requestEntityBody, getHeadersWithCookie(jSessionId));

        return restTemplate.exchange(restPath, method, requestEntity, responseType, urlVariables);
    }

    /** Перегруженный метод для отправки HTTP-запроса с указанными параметрами,
     *  используется, когда нет объекта HttpEntity requestEntity -
     *  метод создает новый объект HttpEntity и добавляет к нему заголовок с идентификатором сессии
     */
    public <T> ResponseEntity<T> exchange(
        String restPath, String jSessionId, HttpMethod method,
        Class<T> responseType, Object... urlVariables
    ) {

        HttpEntity<?> requestEntity = new HttpEntity<>(getHeadersWithCookie(jSessionId));

        return restTemplate.exchange(restPath, method, requestEntity, responseType, urlVariables);
    }

    /** Метод для отправки GET-запроса с указанными параметрами, возвращающий список в теле ответа.
     *  В числе параметров нет объекта HttpEntity requestEntity -
     *  метод создает новый объект HttpEntity и добавляет к нему заголовок с идентификатором сессии
     */
    public <T> ResponseEntity<List<T>> getList(
            String restPath, String jSessionId,
            ParameterizedTypeReference<List<T>> responseType, Object... urlVariables
    ) {

        HttpEntity<?> requestEntity = new HttpEntity<>(getHeadersWithCookie(jSessionId));

        return restTemplate.exchange(restPath, HttpMethod.GET, requestEntity, responseType, urlVariables);
    }

    /** Метод для отправки GET-запроса с указанными параметрами, возвращающий страницу в теле ответа */
    public <T> ResponseEntity<RestPageImpl<T>> getPage(
            String restPath, String jSessionId,
            ParameterizedTypeReference<RestPageImpl<T>> responseType, Object... urlVariables
    ) {

        HttpEntity<?> requestEntity = new HttpEntity<>(getHeadersWithCookie(jSessionId));

        return restTemplate.exchange(restPath, HttpMethod.GET, requestEntity, responseType, urlVariables);
    }


    /** Служебный метод для установки идентификатора сессии в заголовки запроса */
    private HttpHeaders getHeadersWithCookie(String jSessionId) {

        HttpHeaders headersWithCookie = new HttpHeaders();
        headersWithCookie.add(HttpHeaders.COOKIE, "JSESSIONID=" + jSessionId);

        return headersWithCookie;
    }

    /** Метод для осуществления аутентификации пользователя с указанными реквизитами
     *  и получения индентфикатора сессии для использования в запросах */
    public String login(String username, String password) {

        return restTemplate.execute(
            "/login",
            HttpMethod.POST,
            request -> {

                OutputStream body = request.getBody();
                body.write(("username=" + username + "&password=" + password).getBytes());
                body.flush();
                body.close();

            },
            response -> {

                List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);

                if (cookies == null) {
                    return null;
                } else {
                    Map<String, String> cookiesMap = cookies
                        .stream()
                        .collect(toMap((cookie) -> cookie.split("=")[0], (cookie) -> cookie.split("=")[1]));

                    final String jSessionId = cookiesMap.get("JSESSIONID");

                    return jSessionId.contains(";") ? jSessionId.split(";")[0] : jSessionId;
                }
            }
        );
    }
}