package net.lafox.ioClient;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


/**
 * Created by Alice Lafox <alice@lafox.net> on 21.01.16
 * Lafox.Net Software developers Team http://dev.lafox.net
 */
@Service
public class TokenService {
    String serviceUrl="http://localhost:8081";

public Token getToken(String siteName, String ownerName, Long ownerId){
    RestTemplate restTemplate = new RestTemplate();

    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("siteName",  siteName);
    params.add("ownerName", ownerName);
    params.add("ownerId", ""+ownerId);

    return restTemplate.postForObject(serviceUrl + "/api/token/add", params, Token.class);
}


}
