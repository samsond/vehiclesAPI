package com.udacity.vehicles.client.prices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

/**
 * Implements a class to interface with the Pricing Client for price data.
 */
@Component
public class PriceClient {

    @Autowired
    private DiscoveryClient discoveryClient;

    private static final Logger log = LoggerFactory.getLogger(PriceClient.class);

//    private final WebClient client;
//
//    public PriceClient(WebClient pricing) {
//        this.client = pricing;
//    }

    // In a real-world application we'll want to add some resilience
    // to this method with retries/CB/failover capabilities
    // We may also want to cache the results so we don't need to
    // do a request every time
    /**
     * Gets a vehicle price from the pricing client, given vehicle ID.
     * @param vehicleId ID number of the vehicle for which to get the price
     * @return Currency and price of the requested vehicle,
     *   error message that the vehicle ID is invalid, or note that the
     *   service is down.
     */
    public Price getPrice(Long vehicleId) {
        RestTemplate restTemplate = new RestTemplate();
        // pricing-service
        // pricing-service
        List<ServiceInstance> instances = discoveryClient.getInstances("pricing-service");

        if (instances.size()==0) return null;
        String serviceUri = String.format("%s/services/price?vehicleId=%s",instances.get(0).getUri().toString(), vehicleId);


        ResponseEntity< Price > restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null, Price.class, vehicleId);

        return restExchange.getBody();
//        try {
//            Price price = client
//                    .get()
//                    .uri(uriBuilder -> uriBuilder
//                            .path("services/price/")
//                            .queryParam("vehicleId", vehicleId)
//                            .build()
//                    )
//                    .retrieve().bodyToMono(Price.class).block();
//
//            return String.format("%s %s", price.getCurrency(), price.getPrice());
//
//        } catch (Exception e) {
//            log.error("Unexpected error retrieving price for vehicle {}", vehicleId, e);
//        }
//        return "(consult price)";
    }
}
