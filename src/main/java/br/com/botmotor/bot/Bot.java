package br.com.botmotor.bot;

import chatbot.Message;
import chatbot.Response;

public interface Bot {

    Response process(Message m);
}
