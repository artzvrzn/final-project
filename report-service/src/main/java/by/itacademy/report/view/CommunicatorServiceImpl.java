package by.itacademy.report.view;

import by.itacademy.report.model.Account;
import by.itacademy.report.model.Category;
import by.itacademy.report.model.Currency;
import by.itacademy.report.model.Operation;
import by.itacademy.report.utils.JwtTokenUtil;
import by.itacademy.report.view.api.CommunicatorService;
import by.itacademy.report.view.api.UserService;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Log4j2
public class CommunicatorServiceImpl implements CommunicatorService {

    private final Map<UUID, Category> categories;
    private final Map<UUID, Currency> currencies;
    private final RestTemplate restTemplate;
    @Value("${urls.classifier-service}")
    private String classifierServiceUrl;
    @Value("${urls.account-service}")
    private String accountServiceUrl;
    @Autowired
    private UserService userService;

    public CommunicatorServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.categories = new ConcurrentHashMap<>();
        this.currencies = new ConcurrentHashMap<>();
    }

    @Override
    public Category getCategory(UUID id) {
        Category category = categories.get(id);
        if (category != null) {
            return category;
        }
        String url = classifierServiceUrl + "/operation/category/" + id;
        try {
            category = getResponseBody(url, Category.class);
            categories.put(id, category);
        } catch (HttpStatusCodeException exc) {
            log.error(exc.getMessage(), exc.getCause());
            category = new Category();
            category.setId(id);
            category.setTitle(id.toString());
        }
        return category;
    }

    @Override
    public Currency getCurrency(UUID id) {
        Currency currency = currencies.get(id);
        if (currency != null) {
            return currency;
        }
        String url = classifierServiceUrl + "/currency/" + id;
        try {
            currency = getResponseBody(url, Currency.class);
            currencies.put(id, currency);
        } catch (HttpStatusCodeException exc) {
            log.error(exc.getMessage(), exc.getCause());
            currency = new Currency();
            currency.setId(id);
            currency.setTitle(id.toString());
            currency.setDescription("");
        }
        return currency;
    }

    @Override
    public Account getAccount(UUID id) {
        String url = accountServiceUrl + "/" + id;
        Account account;
        try {
            account = getResponseBody(url, Account.class);
        } catch (HttpStatusCodeException exc) {
            log.error(exc.getMessage(), exc.getCause());
            return null;
        }
        return account;
    }

    public List<Operation> getOperations(UUID accountId, LocalDate from, LocalDate to, List<UUID> categories) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(accountServiceUrl)
                .path("/")
                .path(accountId.toString())
                .path("/operation")
                .queryParam("page", 0)
                .queryParam("size", 100)
                .queryParam("from", dateQueryParamFromLocalDate(from))
                .queryParam("to", dateQueryParamFromLocalDate(to))
                .queryParam("category", categories);
        OperationPage page = getResponseBody(uriBuilder.build().toUriString(), OperationPage.class);
        if (page == null) {
            return Collections.emptyList();
        }
        List<Operation> operations = page.getContent();
        int totalPages = page.getTotalPages();
        if (totalPages > 1) {
            for (int pageNumber = 0; pageNumber < totalPages; pageNumber++) {
                uriBuilder.replaceQueryParam("page", pageNumber);
                page = getResponseBody(uriBuilder.build().toUriString(), OperationPage.class);
                operations.addAll(page.getContent());
            }
        }
        return operations;
    }

    private <T> T getResponseBody(String url, Class<T> clazz) throws HttpStatusCodeException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, getToken());
        HttpEntity<T> httpEntity = new HttpEntity<>(headers);
        log.info("Knocking on {}", url);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, clazz).getBody();
    }

    private String dateQueryParamFromLocalDate(LocalDate localDate) {
        return Long.toString(localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
    }

    private String getToken() {
        UserDetails userDetails = userService.getUserDetails();
        return "Bearer " + JwtTokenUtil.generateAccessToken(userDetails);
    }

    @ToString
    private static class OperationPage extends PageImpl<Operation> {

        @JsonCreator
        public OperationPage(@JsonProperty("content") List<Operation> content,
                             @JsonProperty("number") int number,
                             @JsonProperty("size") int size,
                             @JsonProperty("total_pages") long total) {
            super(content, PageRequest.of(number, size), total);
        }
    }
}
