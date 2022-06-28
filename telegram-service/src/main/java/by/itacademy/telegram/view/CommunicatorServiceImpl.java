package by.itacademy.telegram.view;

import by.itacademy.telegram.model.Account;
import by.itacademy.telegram.model.Category;
import by.itacademy.telegram.model.Currency;
import by.itacademy.telegram.model.Operation;
import by.itacademy.telegram.view.api.ChatService;
import by.itacademy.telegram.view.api.CommunicatorService;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class CommunicatorServiceImpl implements CommunicatorService {

    private final RestTemplate restTemplate;
    @Value("${urls.account-service}")
    private String accountServiceUrl;
    @Value("${urls.classifier-service}")
    private String classifierServiceUrl;
    @Autowired
    private ChatService chatService;

    public CommunicatorServiceImpl(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Override
    public void postAccount(Account account, String chatId) throws HttpStatusCodeException {
        try {
            HttpEntity<Account> request = new HttpEntity<>(account, buildHeaders(chatId));
            log.info("POST Knocking on {} with body {}", accountServiceUrl, account);
            restTemplate.postForEntity(accountServiceUrl, request, Void.class);
        } catch (HttpStatusCodeException e) {
            log.error("Account service has returned an error: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Account getAccount(UUID id, String chatId) {
        String url = accountServiceUrl + "/" + id;
        return getRequest(url, chatId, Account.class);
    }

    @Override
    public List<Account> getAccounts(String chatId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(accountServiceUrl)
                .queryParam("page", 0)
                .queryParam("size", 100);
        return getElements(uriBuilder, chatId, new ParameterizedTypeReference<RestPage<Account>>() {});
    }

    @Override
    public void updateAccount(Account account, String chatId) throws HttpStatusCodeException {
        String url = accountServiceUrl + "/" + account.getUuid() + "/dt_update/" + account.getUpdated();
        try {
            HttpEntity<Account> request = new HttpEntity<>(account, buildHeaders(chatId));
            log.info("PUT Knocking on {} with body {}", accountServiceUrl, account);
            restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);
        } catch (HttpStatusCodeException e) {
            log.error("Account service has returned an error: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Operation getOperation(UUID id, String chatId) throws HttpStatusCodeException {
        UUID accountId = chatService.get(chatId).getChosenAccount();
        String url = accountServiceUrl + "/" + accountId + "/operation/" + id;
        return getRequest(url, chatId, Operation.class);
    }

    @Override
    public List<Operation> getOperations(String chatId) throws HttpStatusCodeException {
        UUID accountId = chatService.get(chatId).getChosenAccount();
        String url = accountServiceUrl + "/" + accountId + "/operation";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url)
                .queryParam("page", 0)
                .queryParam("size", 100);
        return getElements(uriBuilder, chatId, new ParameterizedTypeReference<RestPage<Operation>>() {});
    }

    @Override
    public void postOperation(UUID accountId, Operation operation, String chatId) throws HttpStatusCodeException {
        String url = accountServiceUrl + "/" + accountId + "/operation";
        try {
            HttpEntity<Operation> request = new HttpEntity<>(operation, buildHeaders(chatId));
            log.info("POST Knocking on {} with body {}", accountServiceUrl, operation);
            restTemplate.postForEntity(url, request, Void.class);
        } catch (HttpStatusCodeException e) {
            log.error("Account service has returned an error: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateOperation(UUID accountId, Operation operation, String chatId) throws HttpStatusCodeException {
        String url = UriComponentsBuilder.fromUriString(accountServiceUrl)
                .path("/")
                .path(accountId.toString())
                .path("/operation/")
                .path(operation.getUuid().toString())
                .path("/dt_update/")
                .path(String.valueOf(operation.getUpdated()))
                .build().toUriString();
        try {
            HttpEntity<Operation> request = new HttpEntity<>(operation, buildHeaders(chatId));
            log.info("PUT Knocking on {} with body {}", url, operation);
            restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);
        } catch (HttpStatusCodeException e) {
            log.error("Account service has returned an error: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteOperation(UUID accountId, Operation operation, String chatId) throws HttpStatusCodeException {
        String url = UriComponentsBuilder.fromUriString(accountServiceUrl)
                .path("/")
                .path(accountId.toString())
                .path("/operation/")
                .path(operation.getUuid().toString())
                .path("/dt_update/")
                .path(String.valueOf(operation.getUpdated()))
                .build().toUriString();
        try {
            HttpEntity<Operation> request = new HttpEntity<>(operation, buildHeaders(chatId));
            log.info("DELETE Knocking on {} with body {}", url, operation);
            restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class);
        } catch (HttpStatusCodeException e) {
            log.error(
                    "Account service has returned during attempt to delete an operation an error: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Currency getCurrency(UUID id, String chatId) throws HttpStatusCodeException {
        String url = classifierServiceUrl + "/currency/" + id;
        return getRequest(url, chatId, Currency.class);
    }

    @Override
    public Currency getCurrency(String title, String chatId) throws HttpStatusCodeException {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(classifierServiceUrl)
                .path("/currency")
                .queryParam("page", 0)
                .queryParam("size", 100);
        List<Currency> currencies = getElements(
                uriBuilder, chatId, new ParameterizedTypeReference<RestPage<Currency>>() {});
        Optional<Currency> optional = currencies.stream().filter(c -> c.getTitle().equalsIgnoreCase(title)).findFirst();
        if (optional.isEmpty()) {
            return null;
        }
        return optional.get();
    }

    @Override
    public List<Currency> getCurrencies(String chatId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(classifierServiceUrl)
                .path("/currency")
                .queryParam("page", 0)
                .queryParam("size", 100);
        return getElements(uriBuilder, chatId, new ParameterizedTypeReference<RestPage<Currency>>() {});
    }

    @Override
    public Category getCategory(UUID id, String chatId) throws HttpStatusCodeException {
        String url = classifierServiceUrl + "/operation/category/" + id;
        return getRequest(url, chatId, Category.class);
    }

    @Override
    public Category getCategory(String title, String chatId) throws HttpStatusCodeException {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(classifierServiceUrl)
                .path("/operation/category")
                .queryParam("page", 0)
                .queryParam("size", 100);
        List<Category> currencies = getElements(
                uriBuilder, chatId, new ParameterizedTypeReference<RestPage<Category>>() {});
        Optional<Category> optional = currencies.stream().filter(c -> c.getTitle().equalsIgnoreCase(title)).findFirst();
        if (optional.isEmpty()) {
            return null;
        }
        return optional.get();
    }

    @Override
    public List<Category> getCategories(String chatId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(classifierServiceUrl)
                .path("/operation/category")
                .queryParam("page", 0)
                .queryParam("size", 100);
        return getElements(uriBuilder, chatId, new ParameterizedTypeReference<RestPage<Category>>() {});
    }

    private <T> T getRequest(String url, String chatId, Class<T> clazz) {
        log.info("Knocking on {}", url);
        HttpEntity<T> request = new HttpEntity<>(buildHeaders(chatId));
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, request, clazz);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return response.getBody();
        }
        return null;
    }

    public <T> List<T> getElements(
            UriComponentsBuilder uriBuilder, String chatId, ParameterizedTypeReference<RestPage<T>> typeReference) {
        HttpEntity<T> request = new HttpEntity<>(buildHeaders(chatId));
        String url = uriBuilder.build().toUriString();
        log.info("Knocking on {}", url);
        ResponseEntity<RestPage<T>> response = restTemplate
                .exchange(url, HttpMethod.GET, request, typeReference);
        RestPage<T> page = response.getBody();
        if (page == null || page.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> values = page.getContent();
        int totalPages = page.getTotalPages();
        if (totalPages > 1) {
            for (int pageNumber = 0; pageNumber < totalPages; ++pageNumber) {
                uriBuilder.replaceQueryParam("page", pageNumber);
                url = uriBuilder.build().toUriString();
                log.info("Knocking on {}", url);
                response = restTemplate
                        .exchange(url, HttpMethod.GET, request, typeReference);
                page = response.getBody();
                if (page == null) {
                    return values;
                }
                values.addAll(page.getContent());
            }
        }
        return values;
    }

    private HttpHeaders buildHeaders(String chatId) {
        HttpHeaders headers = new HttpHeaders();
        String token = chatService.get(chatId).getJwtToken();
        headers.add(HttpHeaders.AUTHORIZATION, token);
        return headers;
    }

    private static class RestPage<T> extends PageImpl<T> {

        @JsonCreator
        public RestPage(@JsonProperty("content") List<T> content,
                        @JsonProperty("number") int number,
                        @JsonProperty("size") int size,
                        @JsonProperty("total_pages") long total) {
            super(content, PageRequest.of(number, size), total);
        }
    }
}
