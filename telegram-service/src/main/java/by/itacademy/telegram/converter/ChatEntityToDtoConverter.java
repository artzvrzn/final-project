package by.itacademy.telegram.converter;

import by.itacademy.telegram.dao.entity.ChatEntity;
import by.itacademy.telegram.model.Chat;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ChatEntityToDtoConverter implements Converter<ChatEntity, Chat> {

    @Override
    public Chat convert(ChatEntity source) {
        Chat chat = new Chat();
        chat.setId(source.getId());
        chat.setState(source.getMenuState());
        chat.setJwtToken(source.getJwtToken());
        chat.setChosenAccount(source.getChosenAccount());
        chat.setChosenOperation(source.getChosenOperation());
        return chat;
    }
}
