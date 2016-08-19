package io.github.expansionteam.battleships.logic.event;

import com.google.inject.Inject;
import io.github.expansionteam.battleships.common.events.*;
import io.github.expansionteam.battleships.logic.AsyncTask;
import io.github.expansionteam.battleships.logic.message.Message;
import io.github.expansionteam.battleships.logic.message.MessageFactory;
import io.github.expansionteam.battleships.logic.message.MessageProcessor;
import io.github.expansionteam.battleships.logic.message.ProcessMessageTask;
import org.apache.log4j.Logger;

class EventProcessor {

    private final static Logger log = Logger.getLogger(EventProcessor.class);

    private final AsyncTask asyncTask;
    private final MessageFactory messageFactory;
    private final MessageProcessor messageProcessor;

    @Inject
    public EventProcessor(AsyncTask asyncTask, MessageFactory messageFactory, MessageProcessor messageProcessor) {
        this.asyncTask = asyncTask;
        this.messageFactory = messageFactory;
        this.messageProcessor = messageProcessor;
    }

    void processEvent(EventBase event){
        Message message = messageFactory.createFromEvent(event);
        asyncTask.runLater(new ProcessMessageTask(messageProcessor, message));
    }
}
