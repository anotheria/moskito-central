package org.moskito.central.storage.rabbitmq;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import org.configureme.ConfigurationManager;
import org.moskito.central.Snapshot;
import org.moskito.central.storage.SnapshotWithStatsNumbers;
import org.moskito.central.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.*;

import com.rabbitmq.client.Channel;

/**
 * Created by Roman Stetsiuk on 2/1/16.
 */
public class RabbitStorage implements Storage {
    private static Logger log = LoggerFactory.getLogger(RabbitStorage.class);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Storage config.
     */
    private RabbitStorageConfig config;

    /**
     * Channel to rabbitMQ broker
     */
    private Channel channel;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void configure(String configurationName) {
        config = new RabbitStorageConfig();

        if (configurationName == null)
            return;
        try {
            ConfigurationManager.INSTANCE.configureAs(config, configurationName);
        } catch (IllegalArgumentException e) {
            log.warn("Couldn't configure RabbitStorage with " + configurationName + " , working with default values");
        }
    }

    @Override
    public void processSnapshot(final Snapshot target) {
        try {
            initRabbitChannel();
            RabbitPublisher task = new RabbitPublisher(target);
            executorService.submit(task).get();
        } catch (InterruptedException e) {
            log.warn("RabbitStorage.processSnapshot(): rabbit publisher interrupted", e);
        } catch (ExecutionException e) {
            log.error("RabbitStorage.processSnapshot(): error while rabbitmq message processing", e);
        } finally {
            closeChannel();
        }
    }

    private void closeChannel() {
        if (channel != null || channel.isOpen()) {
            try {
                channel.close();
                channel = null;
            } catch (IOException e) {
                log.warn("couldn't close rabbitMQ channel", e);
            } catch (TimeoutException e) {
                log.warn("couldn't close rabbitMQ channel with timeout", e);
            }
        }
    }

    private synchronized void initRabbitChannel() {
        if (channel == null || !channel.isOpen()) {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(config.getHost());
            factory.setPort(config.getPort());
            factory.setUsername(config.getUser());
            factory.setPassword(config.getPassword());

            try {
                channel = factory.newConnection().createChannel();
                channel.queueDeclarePassive(config.getQueueName());
            } catch (IOException e) {
                log.error("RabbitPublisher():Error while channel creation", e);
            } catch (TimeoutException e) {
                log.error("RabbitPublisher(): Timeout expired", e);
            }
        }
    }

    private class RabbitPublisher implements Runnable {

        private final SnapshotWithStatsNumbers target;

        public RabbitPublisher(Snapshot target) {
            this.target = new SnapshotWithStatsNumbers(target);
        }

        @Override
        public void run() {
            String message = "";
            try {
                message = gson.toJson(target);
                AMQP.BasicProperties.Builder basicProperties = new AMQP.BasicProperties.Builder();
                channel.basicPublish("", config.getQueueName(), basicProperties.build(), message.getBytes("UTF-8"));

            } catch (IOException e) {
                log.error("Error while " + message + "proceeding", e);
            }
        }
    }

}
