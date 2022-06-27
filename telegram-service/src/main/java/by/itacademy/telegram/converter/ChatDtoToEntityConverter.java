package by.itacademy.telegram.converter;

import by.itacademy.telegram.dao.entity.ChatEntity;
import by.itacademy.telegram.model.Chat;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ChatDtoToEntityConverter implements Converter<Chat, ChatEntity> {
    @Override
    public ChatEntity convert(Chat source) {
        ChatEntity entity = new ChatEntity();
        entity.setChosenAccount(source.getChosenAccount());
        entity.setChosenOperation(source.getChosenOperation());
        entity.setJwtToken(source.getJwtToken());
        entity.setMenuState(source.getState());
        entity.setId(source.getId());
        return entity;
    }
}
