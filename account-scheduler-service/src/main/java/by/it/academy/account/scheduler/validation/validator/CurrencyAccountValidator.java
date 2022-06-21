package by.it.academy.account.scheduler.validation.validator;

import by.it.academy.account.scheduler.model.Operation;
import by.it.academy.account.scheduler.validation.anno.CurrencyBelongsAccount;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.*;
import java.util.UUID;

@Log4j2
public class CurrencyAccountValidator implements ConstraintValidator<CurrencyBelongsAccount, Operation> {

    @Value("${urls.account-service}")
    private String accountServiceUrl;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean isValid(Operation operation, ConstraintValidatorContext context) {
        if (operation == null) {
            return true;
        }
        Account account = getAccount(operation.getAccount());
        return account.getCurrency().equals(operation.getCurrency());
    }

    private Account getAccount(UUID id) {
        try {
            String url = accountServiceUrl + "/" + id;
            return restTemplate.getForObject(url, Account.class);
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("account doesn't exist");
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class Account {
        private UUID uuid;
        private UUID currency;
    }
}
