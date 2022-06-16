package by.itacademy.classifier.view;

import by.itacademy.classifier.dao.api.CategoryRepository;
import by.itacademy.classifier.dao.entity.CategoryEntity;
import by.itacademy.classifier.exception.RecordNotFoundException;
import by.itacademy.classifier.model.Category;
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
public class CategoryService implements ClassifierService<Category> {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper mapper;

    @Override
    public Category get(UUID id) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RecordNotFoundException(String.format("Category with id %s doesn't exist", id));
        }
        return mapper.map(optional.get(), Category.class);
    }

    @Override
    public void create(Category dto) {
        checkAuthority();
        if (categoryRepository.existsByTitle(dto.getTitle())) {
            throw new IllegalArgumentException("Record already exist");
        }
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        CategoryEntity categoryEntity = mapper.map(dto, CategoryEntity.class);
        categoryEntity.setCreated(currentTime);
        categoryEntity.setUpdated(currentTime);
        categoryEntity.setId(UUID.randomUUID());
        categoryRepository.save(categoryEntity);
        log.info("category {} with id {} has been created", categoryEntity.getTitle(), categoryEntity.getId());
    }

    @Override
    public Page<Category> get(int page, int size) {
        Pageable request = PageRequest.of(page, size, Sort.by("created").descending());
        return categoryRepository.findAll(request).map(e -> mapper.map(e, Category.class));
    }

    private void checkAuthority() {
        UserDetails userDetails = userService.getUserDetails();
        if (!userService.isAdmin(userDetails)) {
            log.error("unauthorized access attempt: User {}", userDetails.getUsername());
            throw new AccessDeniedException("access denied");
        }
    }
}
