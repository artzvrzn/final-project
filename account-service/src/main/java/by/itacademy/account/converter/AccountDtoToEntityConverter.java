package by.itacademy.account.converter;

import by.itacademy.account.dao.entity.AccountEntity;
import by.itacademy.account.dao.entity.BalanceEntity;
import by.itacademy.account.model.Account;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountDtoToEntityConverter implements Converter<Account, AccountEntity> {

    @Override
    public AccountEntity convert(Account dto) {
        AccountEntity entity = new AccountEntity();
        entity.setId(dto.getId());
        entity.setCreated(dto.getCreated());
        entity.setUpdated(dto.getUpdated());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setType(dto.getType());
        BalanceEntity balance = new BalanceEntity();
        balance.setAccount(entity);
        balance.setValue(dto.getValue() != null ? dto.getValue() : new BigDecimal(0));
        balance.setCurrency(dto.getCurrency());
        entity.setBalance(balance);
        return entity;
    }
}
