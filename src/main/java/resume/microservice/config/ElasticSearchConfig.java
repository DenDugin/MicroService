package resume.microservice.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@EnableJpaRepositories(basePackages = "resume.microservice.repository")
@EnableElasticsearchRepositories(basePackages = "resume.microservice.elastic")
public class ElasticSearchConfig {


    @Value("${elasticsearch.home}")
    private String elasticSearchHome;

    @Value("${elasticsearch.cluster.name:elasticsearch}")
    private String clusterName;



    @Bean
    public Client client() throws UnknownHostException {
        Settings elasticsearchSettings = Settings.builder()
                .put("client.transport.sniff", true)
                .put("path.home", elasticSearchHome)
                .put("cluster.name", clusterName).build();
        TransportClient client = new PreBuiltTransportClient(elasticsearchSettings);
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9200));
        return client;
    }


//    @Bean(/*destroyMethod="close"*/)
//    public Node node() {
//
//        return new NodeBuilder()
//                .local(true) // нода будет работать в контексте той вирт. машины в которой работает сейчас Tom Cat
//                .settings(Settings.builder().put("path.home", elasticSearchHome)) // путь к elastic search
//                .node();
//    }



    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws UnknownHostException {
        return new ElasticsearchTemplate(client());
    }



 }