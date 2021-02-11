package com.mir.core.services;

import com.mir.core.models.Message;
import com.mir.core.repositories.IMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService implements IMessageService{

    private IMessageRepository repository;

    @Autowired
    public MessageService(IMessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Message> getMessages() {
        return repository.findAll();
    }

    @Override
    public void addMessage(Message message) {

        if(message.getText() == null || message.getText().isEmpty())
            throw new IllegalStateException("Text of message cannot be null or empty");

        repository.save(message);
    }

    @Override
    public void deleteMessageById(Integer id) {
        if(!repository.existsById(id))
            throw new IllegalStateException("Message with " + id + " doesn't exists");

        repository.deleteById(id);
    }

    @Transactional
    @Override
    public void updateMessage(Integer id, String text) {
        if(text == null || text.isEmpty())
            throw new IllegalStateException("Text of message cannot be null or empty");

        var message = repository.findById(id).orElseThrow(
                ()->new IllegalStateException("Message with " + id + " doesn't exists"));

        message.setText(text);
    }
}
