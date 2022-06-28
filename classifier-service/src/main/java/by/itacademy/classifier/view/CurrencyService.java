package by.itacademy.classifier.view;

import by.itacademy.classifier.dao.api.CurrencyRepository;
import by.itacademy.classifier.dao.entity.CurrencyEntity;
import by.itacademy.classifier.exception.RecordNotFoundException;
import by.itacademy.classifier.model.Currency;
import by.itacademy.classifier.view.api.ClassifierService;
import by.itacademy.classifier.view.api.UserService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@Transactional(rollbackFor = Exception.class)
public class CurrencyService implements ClassifierService<Currency> {

    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private UserService userService;

    @Override
    public void create(Currency dto) {
        checkAuthority();
        if (currencyRepository.existsByTitle(dto.getTitle())) {
            throw new IllegalArgumentException("Record already exist");
        }
        CurrencyEntity entity = mapper.map(dto, CurrencyEntity.class);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        entity.setCreated(currentTime);
        entity.setUpdated(currentTime);
        entity.setId(UUID.randomUUID());
        currencyRepository.save(entity);
        log.info("currency {} with id {} has been created", entity.getTitle(), entity.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Currency get(UUID id) {
        Optional<CurrencyEntity> optional = currencyRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RecordNotFoundException(String.format("Currency with id %s doesn't exist", id));
        }
        return mapper.map(optional.get(), Currency.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Currency> get(int page, int size) {
        Pageable request = PageRequest.of(page, size, Sort.by("title").ascending());
        return currencyRepository.findAll(request).map(e -> mapper.map(e, Currency.class));
    }

    private void checkAuthority() {
        UserDetails userDetails = userService.getUserDetails();
        if (!userService.isAdmin(userDetails)) {
            log.error("unauthorized access attempt: User {}", userDetails.getUsername());
            throw new AccessDeniedException("access denied");
        }
    }
}
