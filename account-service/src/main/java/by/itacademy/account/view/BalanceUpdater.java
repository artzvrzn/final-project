package by.itacademy.account.view;

import by.itacademy.account.dao.api.BalanceRepository;
import by.itacademy.account.view.api.AggregateBalance;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class BalanceUpdater implements AggregateBalance {

    @Autowired
    private BalanceRepository balanceRepository;

    @Override
    @Scheduled(fixedRateString = "${balance-update-rate}")
    public void updateAll() {
        log.info("Balance updated");
        balanceRepository.updateBalance();
    }
}
