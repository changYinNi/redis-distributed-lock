package org.distributed.lock.configuration;

import org.distributed.lock.component.Receiver;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@AutoConfigureAfter({Receiver.class})
public class SubscriberConfig {

    @Bean
    public MessageListenerAdapter getMessageListenerAdapter(Receiver receiver){
        return new MessageListenerAdapter(receiver);
    }

    @Bean
    public RedisMessageListenerContainer getRedisMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory, MessageListenerAdapter messageListenerAdapter){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(messageListenerAdapter, new PatternTopic("TOPIC_USERNAME"));
        return container;
    }

}
